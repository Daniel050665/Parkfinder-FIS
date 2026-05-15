package com.parkfinder.controller;

import com.parkfinder.entities.Parqueadero;
import com.parkfinder.repositories.CupoRepository;
import com.parkfinder.repositories.ParqueaderoRepository;
import com.parkfinder.util.SesionActual;
import com.parkfinder.util.ViewLoader;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        filtroTipo.setItems(FXCollections.observableArrayList("Todos", "Automóvil", "Motocicleta"));
        filtroTipo.setValue("Todos");

        filtroPrecio.setItems(FXCollections.observableArrayList(
                "Cualquiera", "Menos de $3.000/h", "$3.000-$5.000/h", "Más de $5.000/h"
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

        configurarCeldas();
        cargarParqueaderos(parqueaderoRepository.findAll());
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

                    Label dir = new Label(p.getDireccion() + " · " + p.getZona());
                    dir.setStyle("-fx-text-fill:#6b7280; -fx-font-size:11px;");

                    Label precio = new Label(
                            "$" + (int) p.getPrecioPorHora().doubleValue() + "/h · "
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
            case "Más de $5.000/h"   -> precio > 5000;
            default -> true;
        };
    }

    @FXML
    private void handleSeleccionarParqueadero(MouseEvent e) {
        Parqueadero seleccionado = listaParqueaderos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;
        SesionActual.setParqueadero(seleccionado);
        Stage stage = (Stage) listaParqueaderos.getScene().getWindow();
        ViewLoader.navegar(stage, "Reserva.fxml");
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
        alert.setTitle("Suscripción");
        alert.setHeaderText(null);
        alert.setContentText("Gestiona tu suscripción desde 'Mis Reservas'.");
        alert.showAndWait();
    }

    private void cargarParqueaderos(List<Parqueadero> lista) {
        listaParqueaderos.setItems(FXCollections.observableArrayList(lista));
        resultadosLabel.setText(lista.size() + " parqueadero(s) encontrado(s)");
    }
}