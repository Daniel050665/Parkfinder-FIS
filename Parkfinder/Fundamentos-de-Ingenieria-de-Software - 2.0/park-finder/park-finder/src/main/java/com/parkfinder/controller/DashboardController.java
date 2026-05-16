package com.parkfinder.controller;

import com.parkfinder.entities.Parqueadero;
import com.parkfinder.repositories.CupoRepository;
import com.parkfinder.repositories.ParqueaderoRepository;
import com.parkfinder.util.SesionActual;
import com.parkfinder.util.ViewLoader;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controlador JavaFX para el dashboard principal del sistema.
 * Muestra la lista de parqueaderos disponibles con filtros
 * de busqueda por zona, nombre y rango de precio, junto con
 * un mapa interactivo basado en OpenStreetMap (Leaflet).
 *
 * @author Equipo ParkFinder
 * @version 2.0
 */
@Component
@Scope("prototype")
public class DashboardController implements Initializable {

    @Autowired private ParqueaderoRepository parqueaderoRepository;
    @Autowired private CupoRepository cupoRepository;

    @FXML private TextField busquedaField;
    @FXML private ComboBox<String> filtroTipo;
    @FXML private ComboBox<String> filtroPrecio;
    @FXML private Label resultadosLabel;
    @FXML private ListView<Parqueadero> listaParqueaderos;
    @FXML private Label inicialesLabel;
    @FXML private Label nombreUsuarioLabel;
    @FXML private StackPane mapContainer;

    private WebEngine webEngine;
    private List<Parqueadero> parqueaderosActuales;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        filtroTipo.setItems(FXCollections.observableArrayList("Todos", "Automovil", "Motocicleta"));
        filtroTipo.setValue("Todos");

        filtroPrecio.setItems(FXCollections.observableArrayList(
                "Cualquiera", "Menos de $3.000/h", "$3.000-$5.000/h", "Mas de $5.000/h"
        ));
        filtroPrecio.setValue("Cualquiera");

        var usuario = SesionActual.getUsuario();
        if (usuario != null) {
            nombreUsuarioLabel.setText(usuario.getNombre());
            String[] partes = usuario.getNombre().split(" ");
            String iniciales = partes.length >= 2
                    ? String.valueOf(partes[0].charAt(0)) + partes[1].charAt(0)
                    : String.valueOf(partes[0].charAt(0));
            inicialesLabel.setText(iniciales.toUpperCase());
        }

        inicializarMapa();
        configurarCeldas();

        parqueaderosActuales = parqueaderoRepository.findAll();
        cargarParqueaderos(parqueaderosActuales);
    }

    /**
     * Inicializa el WebView con un mapa Leaflet (OpenStreetMap) centrado en Bogota.
     */
    private void inicializarMapa() {
        try {
            WebView webView = new WebView();
            webEngine = webView.getEngine();
            mapContainer.getChildren().add(webView);
            webEngine.loadContent(generarHtmlMapa());
        } catch (Exception ex) {
            System.out.println("[Dashboard] No se pudo cargar el mapa: " + ex.getMessage());
        }
    }

    /**
     * Genera el HTML completo con Leaflet.js para renderizar el mapa
     * con los marcadores de los parqueaderos.
     *
     * @return contenido HTML del mapa
     */
    private String generarHtmlMapa() {
        List<Parqueadero> todos = parqueaderoRepository.findAll();

        StringBuilder markers = new StringBuilder();
        for (Parqueadero p : todos) {
            if (p.getLatitud() == null || p.getLongitud() == null) continue;
            long disponibles = cupoRepository
                    .findByParqueaderoIdParqueaderoAndDisponibleTrue(p.getIdParqueadero())
                    .size();
            String color = disponibles > 0 ? "#52B788" : "#888780";
            markers.append(String.format(
                "L.circleMarker([%f, %f], {radius: 10, fillColor: '%s', color: '#1B4332', weight: 2, fillOpacity: 0.9})" +
                ".addTo(map).bindPopup('<b>%s</b><br>%s<br>$%d/h - %d cupos');\n",
                p.getLatitud(), p.getLongitud(), color,
                p.getNombre(), p.getDireccion(),
                p.getPrecioPorHora().intValue(), disponibles
            ));
        }

        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="utf-8"/>
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css"/>
                <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
                <style>
                    body { margin: 0; padding: 0; }
                    #map { width: 100%%; height: 100vh; }
                    .leyenda {
                        background: white; padding: 10px 14px; border-radius: 6px;
                        box-shadow: 0 1px 4px rgba(0,0,0,0.3); font-size: 12px;
                        font-family: Arial, sans-serif; line-height: 1.8;
                    }
                    .leyenda i { width: 12px; height: 12px; display: inline-block;
                        border-radius: 50%%; margin-right: 6px; vertical-align: middle; }
                </style>
            </head>
            <body>
                <div id="map"></div>
                <script>
                    var map = L.map('map', {attributionControl: false}).setView([4.660, -74.055], 13);
                    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                        maxZoom: 19
                    }).addTo(map);
                    %s
                    var leyenda = L.control({position: 'bottomright'});
                    leyenda.onAdd = function(map) {
                        var div = L.DomUtil.create('div', 'leyenda');
                        div.innerHTML =
                            '<b>ParkFinder</b><br>' +
                            '<i style="background:#52B788"></i> Disponible<br>' +
                            '<i style="background:#888780"></i> Sin cupos<br>' +
                            '<i style="background:#E63946"></i> Seleccionado';
                        return div;
                    };
                    leyenda.addTo(map);

                    function centrarEn(lat, lng) {
                        map.setView([lat, lng], 16);
                    }
                </script>
            </body>
            </html>
            """.formatted(markers.toString());
    }

    /**
     * Centra el mapa en las coordenadas del parqueadero seleccionado.
     *
     * @param p parqueadero a enfocar
     */
    private void centrarMapaEn(Parqueadero p) {
        try {
            if (p.getLatitud() != null && p.getLongitud() != null && webEngine != null) {
                webEngine.executeScript(
                    String.format("centrarEn(%f, %f)", p.getLatitud(), p.getLongitud())
                );
            }
        } catch (Exception ex) {
            System.out.println("[Dashboard] Mapa no disponible: " + ex.getMessage());
        }
    }

    private void configurarCeldas() {
        listaParqueaderos.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Parqueadero p, boolean empty) {
                super.updateItem(p, empty);
                if (empty || p == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    long disponibles = cupoRepository
                            .findByParqueaderoIdParqueaderoAndDisponibleTrue(p.getIdParqueadero())
                            .size();

                    VBox box = new VBox(4);
                    box.setPadding(new Insets(10, 12, 10, 12));

                    Label nombre = new Label(p.getNombre());
                    nombre.setStyle("-fx-font-weight:bold; -fx-font-size:13px;");

                    Label dir = new Label(p.getDireccion() + " - " + p.getZona());
                    dir.setStyle("-fx-text-fill:#6b7280; -fx-font-size:11px;");

                    Label precio = new Label(
                            "$" + (int) p.getPrecioPorHora().doubleValue() + "/h - "
                                    + disponibles + " cupos disponibles"
                    );
                    precio.setStyle("-fx-text-fill:#1B4332; -fx-font-size:12px;");

                    box.getChildren().addAll(nombre, dir, precio);
                    setGraphic(box);
                }
            }
        });
    }

    @FXML
    private void handleBuscar(ActionEvent e) {
        String texto = busquedaField.getText().trim().toLowerCase();
        String precio = filtroPrecio.getValue();

        List<Parqueadero> resultados = parqueaderoRepository.findAll().stream()
                .filter(p -> texto.isEmpty()
                        || p.getNombre().toLowerCase().contains(texto)
                        || p.getDireccion().toLowerCase().contains(texto)
                        || p.getZona().toLowerCase().contains(texto))
                .filter(p -> aplicarFiltroPrecio(p, precio))
                .toList();

        cargarParqueaderos(resultados);
    }

    private boolean aplicarFiltroPrecio(Parqueadero p, String filtro) {
        if (filtro == null || filtro.equals("Cualquiera")) return true;
        double precio = p.getPrecioPorHora();
        return switch (filtro) {
            case "Menos de $3.000/h" -> precio < 3000;
            case "$3.000-$5.000/h"   -> precio >= 3000 && precio <= 5000;
            case "Mas de $5.000/h"   -> precio > 5000;
            default -> true;
        };
    }

    /**
     * Selecciona un parqueadero y navega a la pantalla de reserva.
     * Usa Platform.runLater para asegurar que la seleccion del ListView
     * se haya procesado antes de leer el item seleccionado.
     */
    @FXML
    private void handleSeleccionarParqueadero(MouseEvent e) {
        Platform.runLater(() -> {
            try {
                Parqueadero seleccionado = listaParqueaderos.getSelectionModel().getSelectedItem();
                if (seleccionado == null) {
                    System.out.println("[Dashboard] Clic en lista pero sin seleccion");
                    return;
                }
                System.out.println("[Dashboard] Navegando a reserva: " + seleccionado.getNombre());
                SesionActual.setParqueadero(seleccionado);
                Stage stage = (Stage) listaParqueaderos.getScene().getWindow();
                ViewLoader.navegar(stage, "Reserva.fxml");
            } catch (Exception ex) {
                System.out.println("[Dashboard] Error al navegar: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }

    @FXML
    private void irAReservas(MouseEvent e) {
        Stage stage = (Stage) listaParqueaderos.getScene().getWindow();
        ViewLoader.navegar(stage, "MisReservas.fxml");
    }

    @FXML
    private void irAHistorial(MouseEvent e) {
        Stage stage = (Stage) listaParqueaderos.getScene().getWindow();
        ViewLoader.navegar(stage, "MisReservas.fxml");
    }

    @FXML
    private void irASuscripcion(MouseEvent e) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Suscripcion");
        alert.setHeaderText(null);
        alert.setContentText("Gestiona tu suscripcion desde 'Mis Reservas'.");
        alert.showAndWait();
    }

    private void cargarParqueaderos(List<Parqueadero> lista) {
        parqueaderosActuales = lista;
        listaParqueaderos.setItems(FXCollections.observableArrayList(lista));
        resultadosLabel.setText(lista.size() + " parqueadero(s) encontrado(s)");
    }
}