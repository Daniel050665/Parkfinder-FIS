package com.parkfinder.controller;

import com.parkfinder.entities.Cupo;
import com.parkfinder.entities.Parqueadero;
import com.parkfinder.entities.Vehiculo;
import com.parkfinder.repositories.CupoRepository;
import com.parkfinder.repositories.VehiculoRepository;
import com.parkfinder.services.ReservaService;
import com.parkfinder.util.SesionActual;
import com.parkfinder.util.ViewLoader;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import javafx.scene.control.SpinnerValueFactory;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
public class ReservaController implements Initializable {

    @Autowired private CupoRepository cupoRepository;
    @Autowired private VehiculoRepository vehiculoRepository;
    @Autowired private ReservaService reservaService;

    @FXML private Label nombreParqueaderoLabel;
    @FXML private Label nombreParqueaderoNav;
    @FXML private Label direccionLabel;
    @FXML private Label cuposDisponiblesLabel;
    @FXML private Label precioLabel;
    @FXML private Label horarioLabel;
    @FXML private Label vehiculosLabel;
    @FXML private Label tipoLabel;
    @FXML private FlowPane cuposPane;
    @FXML private Label cupoSeleccionadoLabel;
    @FXML private ComboBox<Vehiculo> vehiculoCombo;
    @FXML private DatePicker fechaInicio;
    @FXML private ComboBox<String> horaInicio;
    @FXML private Spinner<Integer> duracionSpinner;
    @FXML private Label costoEstimadoLabel;
    @FXML private Label descuentoLabel;
    @FXML private Label totalLabel;
    @FXML private Label inicialesLabel;
    @FXML private Label nombreUsuarioLabel;

    private Cupo cupoSeleccionado;
    private Parqueadero parqueadero;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        parqueadero = SesionActual.getParqueadero();

        cargarDatosParqueadero();
        configurarHoras();
        configurarVehiculos();
        generarBotonesCupos();

        fechaInicio.setValue(LocalDate.now());
        duracionSpinner.valueProperty().addListener((obs, o, n) -> actualizarCosto());
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, 2);
        duracionSpinner.setValueFactory(valueFactory);
        actualizarCosto();

        var usuario = SesionActual.getUsuario();
        if (usuario != null) {
            nombreUsuarioLabel.setText(usuario.getNombre());
            String[] partes = usuario.getNombre().split(" ");
            String iniciales = partes.length >= 2
                    ? String.valueOf(partes[0].charAt(0)) + partes[1].charAt(0)
                    : String.valueOf(partes[0].charAt(0));
            inicialesLabel.setText(iniciales.toUpperCase());
        }
    }

    private void cargarDatosParqueadero() {
        if (parqueadero == null) return;
        nombreParqueaderoLabel.setText(parqueadero.getNombre());
        if (nombreParqueaderoNav != null) nombreParqueaderoNav.setText(parqueadero.getNombre());
        direccionLabel.setText(parqueadero.getDireccion());
        precioLabel.setText("$" + (int) parqueadero.getPrecioPorHora().doubleValue());
        horarioLabel.setText(parqueadero.getHorarioApertura() + "–" + parqueadero.getHorarioCierre());
        vehiculosLabel.setText("Auto / Moto");
        tipoLabel.setText("Cubierto");

        long disponibles = cupoRepository
                .findByParqueaderoIdParqueaderoAndDisponibleTrue(parqueadero.getIdParqueadero())
                .size();
        cuposDisponiblesLabel.setText(disponibles + " cupos disponibles");
    }

    private void configurarHoras() {
        List<String> horas = List.of(
                "6:00","7:00","8:00","9:00","10:00","11:00","12:00",
                "13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00"
        );
        horaInicio.setItems(FXCollections.observableArrayList(horas));
        horaInicio.setValue("9:00");
    }

    private void configurarVehiculos() {
        var usuario = SesionActual.getUsuario();
        if (usuario == null) return;

        List<Vehiculo> vehiculos = vehiculoRepository.findByUsuario(usuario);
        vehiculoCombo.setItems(FXCollections.observableArrayList(vehiculos));
        vehiculoCombo.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(Vehiculo v, boolean empty) {
                super.updateItem(v, empty);
                setText(empty || v == null ? null : v.getPlaca());
            }
        });
        vehiculoCombo.setButtonCell(new ListCell<>() {
            @Override protected void updateItem(Vehiculo v, boolean empty) {
                super.updateItem(v, empty);
                setText(empty || v == null ? null : v.getPlaca());
            }
        });
        if (!vehiculos.isEmpty()) vehiculoCombo.setValue(vehiculos.get(0));
    }

    private void generarBotonesCupos() {
        cuposPane.getChildren().clear();
        if (parqueadero == null) return;

        List<Cupo> cupos = cupoRepository
                .findByParqueaderoIdParqueadero(parqueadero.getIdParqueadero());

        for (Cupo cupo : cupos) {
            Button btn = new Button("Cupo #" + cupo.getNumeroCupo());
            btn.setPrefSize(80, 40);

            if (Boolean.TRUE.equals(cupo.getDisponible())) {
                btn.setStyle(
                        "-fx-background-color:#EAF3DE; -fx-border-color:#52B788;" +
                                "-fx-border-radius:6; -fx-background-radius:6; -fx-cursor:hand;"
                );
                btn.setOnAction(e -> seleccionarCupo(cupo, btn));
            } else {
                btn.setStyle(
                        "-fx-background-color:#FCEBEB; -fx-border-color:#F09595;" +
                                "-fx-border-radius:6; -fx-background-radius:6; -fx-opacity:0.6;"
                );
                btn.setDisable(true);
            }
            cuposPane.getChildren().add(btn);
        }
    }

    private void seleccionarCupo(Cupo cupo, Button btnSeleccionado) {
        cupoSeleccionado = cupo;
        cupoSeleccionadoLabel.setText("Cupo #" + cupo.getNumeroCupo());

        cuposPane.getChildren().forEach(node -> {
            if (node instanceof Button b && !b.isDisabled()) {
                b.setStyle(
                        "-fx-background-color:#EAF3DE; -fx-border-color:#52B788;" +
                                "-fx-border-radius:6; -fx-background-radius:6; -fx-cursor:hand;"
                );
            }
        });
        btnSeleccionado.setStyle(
                "-fx-background-color:#1B4332; -fx-border-color:#1B4332;" +
                        "-fx-border-radius:6; -fx-background-radius:6;" +
                        "-fx-text-fill:white; -fx-cursor:hand;"
        );
    }



    private void actualizarCosto() {
        if (parqueadero == null) return;
        int horas = duracionSpinner.getValue();
        double costo = horas * parqueadero.getPrecioPorHora();
        costoEstimadoLabel.setText("$" + String.format("%,.0f", costo));
        descuentoLabel.setText("- $0");
        totalLabel.setText("$" + String.format("%,.0f", costo));
    }

    @FXML
    private void handleConfirmarReserva(ActionEvent e) {
        if (cupoSeleccionado == null) { mostrarError("Selecciona un cupo."); return; }
        if (vehiculoCombo.getValue() == null) { mostrarError("Selecciona un vehículo."); return; }
        if (fechaInicio.getValue() == null || horaInicio.getValue() == null) {
            mostrarError("Selecciona fecha y hora de inicio.");
            return;
        }

        try {
            String[] partes = horaInicio.getValue().split(":");
            LocalDateTime inicio = LocalDateTime.of(
                    fechaInicio.getValue(),
                    LocalTime.of(Integer.parseInt(partes[0]), 0)
            );

            var usuario = SesionActual.getUsuario();
            var reserva = reservaService.crearReserva(
                    usuario.getIdUsuario(),
                    vehiculoCombo.getValue().getIdVehiculo(),
                    cupoSeleccionado.getIdCupo(),
                    inicio,
                    duracionSpinner.getValue()
            );

            SesionActual.setReserva(reserva);
            Stage stage = (Stage) cuposPane.getScene().getWindow();
            ViewLoader.navegar(stage, "Pago.fxml");
        } catch (Exception ex) {
            mostrarError("Error al crear la reserva: " + ex.getMessage());
        }
    }

    @FXML private void handleCancelar(ActionEvent e) {
        Stage stage = (Stage) cuposPane.getScene().getWindow();
        ViewLoader.navegar(stage, "Dashboard.fxml");
    }

    @FXML private void handleVolver(MouseEvent e) {
        Stage stage = (Stage) cuposPane.getScene().getWindow();
        ViewLoader.navegar(stage, "Dashboard.fxml");
    }

    @FXML private void irAInicio(MouseEvent e) {
        Stage stage = (Stage) cuposPane.getScene().getWindow();
        ViewLoader.navegar(stage, "Dashboard.fxml");
    }

    @FXML private void irAReservas(MouseEvent e) {
        Stage stage = (Stage) cuposPane.getScene().getWindow();
        ViewLoader.navegar(stage, "MisReservas.fxml");
    }

    private void mostrarError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}