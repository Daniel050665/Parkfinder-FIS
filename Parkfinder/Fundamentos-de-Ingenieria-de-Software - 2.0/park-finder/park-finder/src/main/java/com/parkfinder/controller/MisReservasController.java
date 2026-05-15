package com.parkfinder.controller;

import com.parkfinder.entities.*;
import com.parkfinder.repositories.*;
import com.parkfinder.services.ReservaService;
import com.parkfinder.services.SuscripcionService;
import com.parkfinder.util.SesionActual;
import com.parkfinder.MainApp;
import com.parkfinder.util.ViewLoader;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
public class MisReservasController implements Initializable {

    @Autowired private ReservaRepository reservaRepository;
    @Autowired private ReservaService reservaService;
    @Autowired private VehiculoRepository vehiculoRepository;
    @Autowired private TipoVehiculoRepository tipoVehiculoRepository;
    @Autowired private SuscripcionService suscripcionService;

    @FXML private Label inicialesLabel;
    @FXML private Label nombreUsuarioLabel;
    @FXML private Label nombreReservaActivaLabel;
    @FXML private Label direccionReservaActivaLabel;
    @FXML private Label inicioReservaLabel;
    @FXML private Label finReservaLabel;
    @FXML private Label vehiculoReservaLabel;
    @FXML private Label fidelizacionLabel;
    @FXML private Label placaLabel;
    @FXML private Label tipoVehiculoLabel;
    @FXML private Label planLabel;
    @FXML private Label vencimientoLabel;
    @FXML private TableView<Reserva> tablaHistorial;
    @FXML private TableColumn<Reserva, String> colParqueadero;
    @FXML private TableColumn<Reserva, String> colFecha;
    @FXML private TableColumn<Reserva, String> colDuracion;
    @FXML private TableColumn<Reserva, String> colTotal;
    @FXML private TableColumn<Reserva, String> colEstado;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final List<String> HORAS = List.of(
            "6:00","7:00","8:00","9:00","10:00","11:00","12:00",
            "13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00"
    );

    private Reserva reservaActiva;
    private Usuario usuario;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usuario = SesionActual.getUsuario();
        if (usuario == null) return;

        cargarDatosUsuario();
        cargarReservaActiva();
        cargarSuscripcion();
        cargarVehiculo();
        configurarTabla();
        cargarHistorial();
    }

    private void cargarDatosUsuario() {
        nombreUsuarioLabel.setText(usuario.getNombre());
        String[] partes = usuario.getNombre().split(" ");
        String iniciales = partes.length >= 2
                ? String.valueOf(partes[0].charAt(0)) + partes[1].charAt(0)
                : String.valueOf(partes[0].charAt(0));
        inicialesLabel.setText(iniciales.toUpperCase());

        int usos = usuario.getUsosAcumulados();
        int restantes = 3 - (usos % 3);
        fidelizacionLabel.setText(restantes == 0
                ? "¡Tienes una reserva gratis disponible!"
                : restantes + " uso(s) más para reserva gratis");
    }

    private void cargarReservaActiva() {
        Optional<Reserva> activa = reservaRepository
                .findFirstByUsuarioAndEstado(usuario, "ACTIVA");

        if (activa.isPresent()) {
            reservaActiva = activa.get();
            nombreReservaActivaLabel.setText(
                    reservaActiva.getCupo().getParqueadero().getNombre()
                            + " · Cupo #" + reservaActiva.getCupo().getNumeroCupo()
            );
            direccionReservaActivaLabel.setText(
                    reservaActiva.getCupo().getParqueadero().getDireccion()
            );
            inicioReservaLabel.setText(reservaActiva.getFechaInicio().format(FMT));
            finReservaLabel.setText(reservaActiva.getFechaFin().format(FMT));
            vehiculoReservaLabel.setText(reservaActiva.getVehiculo().getPlaca());
        } else {
            reservaActiva = null;
            nombreReservaActivaLabel.setText("Sin reserva activa");
            direccionReservaActivaLabel.setText("");
            inicioReservaLabel.setText("-");
            finReservaLabel.setText("-");
            vehiculoReservaLabel.setText("-");
        }
    }

    private void cargarSuscripcion() {
        suscripcionService.obtenerSuscripcionActiva(usuario).ifPresentOrElse(
                s -> {
                    planLabel.setText("Plan " + s.getTipoPlan() + " Activo");
                    vencimientoLabel.setText("Vence: " + s.getFechaFin().format(
                            DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                },
                () -> {
                    planLabel.setText("Sin suscripción activa");
                    vencimientoLabel.setText("Activa un plan mensual");
                }
        );
    }

    private void cargarVehiculo() {
        vehiculoRepository.findByUsuario(usuario).stream().findFirst().ifPresent(v -> {
            placaLabel.setText(v.getPlaca());
            tipoVehiculoLabel.setText(v.getTipoVehiculo() != null
                    ? v.getTipoVehiculo().getNombre() : "Automóvil");
        });
    }

    private void cargarHistorial() {
        List<Reserva> historial = reservaRepository.findByUsuario(usuario).stream()
                .filter(r -> !r.getEstado().equals("ACTIVA"))
                .toList();
        tablaHistorial.setItems(FXCollections.observableArrayList(historial));
    }

    // ─── Cancelar reserva ─────────────────────────────────────────────────────

    @FXML
    private void handleCancelarReserva(ActionEvent e) {
        if (reservaActiva == null) {
            mostrarInfo("No tienes ninguna reserva activa para cancelar.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Cancelar reserva");
        confirm.setHeaderText("¿Estás seguro que deseas cancelar esta reserva?");
        confirm.setContentText(
                reservaActiva.getCupo().getParqueadero().getNombre()
                        + " — " + reservaActiva.getFechaInicio().format(FMT)
        );

        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    reservaService.cancelarReserva(reservaActiva.getIdReserva());
                    mostrarInfo("Reserva cancelada exitosamente.");
                    recargar();
                } catch (Exception ex) {
                    mostrarError("Error al cancelar: " + ex.getMessage());
                }
            }
        });
    }

    // ─── Modificar horario ────────────────────────────────────────────────────

    @FXML
    private void handleModificarReserva(ActionEvent e) {
        if (reservaActiva == null) {
            mostrarInfo("No tienes ninguna reserva activa para modificar.");
            return;
        }

        Dialog<LocalDateTime> dialog = new Dialog<>();
        dialog.setTitle("Modificar horario");
        dialog.setHeaderText("Selecciona el nuevo horario de inicio");

        ButtonType confirmarBtn = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmarBtn, ButtonType.CANCEL);

        DatePicker datePicker = new DatePicker(reservaActiva.getFechaInicio().toLocalDate());
        ComboBox<String> horaCombo = new ComboBox<>(FXCollections.observableArrayList(HORAS));
        horaCombo.setValue(reservaActiva.getFechaInicio().getHour() + ":00");
        horaCombo.setPrefWidth(140);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setStyle("-fx-padding: 20;");
        grid.add(new Label("Nueva fecha:"), 0, 0);
        grid.add(datePicker, 1, 0);
        grid.add(new Label("Nueva hora:"), 0, 1);
        grid.add(horaCombo, 1, 1);
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        dialog.setResultConverter(btn -> {
            if (btn == confirmarBtn && datePicker.getValue() != null) {
                String[] partes = horaCombo.getValue().split(":");
                return LocalDateTime.of(datePicker.getValue(),
                        LocalTime.of(Integer.parseInt(partes[0]), 0));
            }
            return null;
        });

        dialog.showAndWait().ifPresent(nuevoInicio -> {
            reservaActiva.setFechaInicio(nuevoInicio);
            reservaActiva.setFechaFin(
                    nuevoInicio.plusHours(reservaActiva.getTiempoEstimadoHoras())
            );
            reservaRepository.save(reservaActiva);
            mostrarInfo("Horario actualizado correctamente.");
            recargar();
        });
    }

    // ─── Suscripción ──────────────────────────────────────────────────────────

    @FXML
    private void handleGestionarSuscripcion(ActionEvent e) {
        boolean tieneActiva = suscripcionService.obtenerSuscripcionActiva(usuario).isPresent();

        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Suscripción ParkFinder");

        if (tieneActiva) {
            dialog.setHeaderText("Plan Mensual Activo — $80.000/mes");
            dialog.setContentText(
                    "Tu suscripción incluye: usos acumulados reiniciados, acceso prioritario.\n\n" +
                            "¿Deseas cancelar tu suscripción actual?"
            );
            ButtonType cancelarSus = new ButtonType("Cancelar suscripción", ButtonBar.ButtonData.LEFT);
            ButtonType cerrar = new ButtonType("Cerrar", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getButtonTypes().setAll(cancelarSus, cerrar);

            dialog.showAndWait().ifPresent(btn -> {
                if (btn == cancelarSus) {
                    suscripcionService.cancelarSuscripcion(usuario);
                    mostrarInfo("Suscripción cancelada.");
                    recargar();
                }
            });
        } else {
            dialog.setHeaderText("Activar suscripción mensual — $80.000/mes");
            dialog.setContentText(
                    "Beneficios del plan mensual:\n" +
                            "• Acceso a todos los parqueaderos\n" +
                            "• Reservas sin recargo\n" +
                            "• Historial de usos reiniciado\n\n" +
                            "Vigencia: 30 días desde hoy. Pago simulado."
            );
            ButtonType activar = new ButtonType("Activar plan", ButtonBar.ButtonData.OK_DONE);
            dialog.getButtonTypes().setAll(activar, ButtonType.CANCEL);

            dialog.showAndWait().ifPresent(btn -> {
                if (btn == activar) {
                    suscripcionService.activarSuscripcion(usuario, "Mensual");
                    mostrarInfo("¡Suscripción activada! Vigente por 30 días.");
                    recargar();
                }
            });
        }
    }

    // ─── Agregar vehículo ─────────────────────────────────────────────────────

    @FXML
    private void handleAgregarVehiculo(MouseEvent e) {
        List<TipoVehiculo> tipos = tipoVehiculoRepository.findAll();

        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Agregar vehículo");
        dialog.setHeaderText("Ingresa los datos del nuevo vehículo");

        ButtonType guardarBtn = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(guardarBtn, ButtonType.CANCEL);

        TextField placaField = new TextField();
        placaField.setPromptText("Ej: XYZ-456");
        placaField.setPrefWidth(200);

        ComboBox<TipoVehiculo> tipoCombo = new ComboBox<>(
                FXCollections.observableArrayList(tipos));
        tipoCombo.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(TipoVehiculo t, boolean empty) {
                super.updateItem(t, empty);
                setText(empty || t == null ? null : t.getNombre());
            }
        });
        tipoCombo.setButtonCell(new ListCell<>() {
            @Override protected void updateItem(TipoVehiculo t, boolean empty) {
                super.updateItem(t, empty);
                setText(empty || t == null ? null : t.getNombre());
            }
        });
        if (!tipos.isEmpty()) tipoCombo.setValue(tipos.get(0));
        tipoCombo.setPrefWidth(200);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setStyle("-fx-padding: 20;");
        grid.add(new Label("Placa:"), 0, 0);
        grid.add(placaField, 1, 0);
        grid.add(new Label("Tipo:"), 0, 1);
        grid.add(tipoCombo, 1, 1);
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        dialog.setResultConverter(btn -> {
            if (btn == guardarBtn) {
                String placa = placaField.getText().trim();
                if (placa.isEmpty() || tipoCombo.getValue() == null) return null;
                return new String[]{placa, String.valueOf(tipoCombo.getValue().getIdTipoVehiculo())};
            }
            return null;
        });

        dialog.showAndWait().ifPresent(datos -> {
            TipoVehiculo tipo = tipos.stream()
                    .filter(t -> t.getIdTipoVehiculo() == Long.parseLong(datos[1]))
                    .findFirst().orElse(null);

            Vehiculo nuevo = new Vehiculo();
            nuevo.setPlaca(datos[0]);
            nuevo.setUsuario(usuario);
            nuevo.setTipoVehiculo(tipo);
            vehiculoRepository.save(nuevo);

            mostrarInfo("Vehículo " + datos[0] + " agregado correctamente.");
            cargarVehiculo();
        });
    }

    // ─── Tabla historial ──────────────────────────────────────────────────────

    private void configurarTabla() {
        colParqueadero.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getCupo().getParqueadero().getNombre()));
        colFecha.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getFechaInicio().format(FMT)));
        colDuracion.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getTiempoEstimadoHoras() + "h"));
        colTotal.setCellValueFactory(c -> {
            Reserva r = c.getValue();
            double t = r.getTiempoEstimadoHoras() * r.getCupo().getParqueadero().getPrecioPorHora();
            return new SimpleStringProperty("$" + String.format("%,.0f", t));
        });
        colEstado.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getEstado()));
    }

    // ─── Navegación ───────────────────────────────────────────────────────────

    @FXML private void irAInicio(MouseEvent e)      { navegar("Dashboard.fxml"); }
    @FXML private void irASuscripcion(MouseEvent e) { handleGestionarSuscripcion(null); }

    private void recargar() {
        Stage stage = (Stage) tablaHistorial.getScene().getWindow();
        ViewLoader.navegar(stage, "MisReservas.fxml");
    }

    private void navegar(String fxmlName) {
        Stage stage = (Stage) tablaHistorial.getScene().getWindow();
        ViewLoader.navegar(stage, fxmlName);
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private void mostrarInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void mostrarError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}