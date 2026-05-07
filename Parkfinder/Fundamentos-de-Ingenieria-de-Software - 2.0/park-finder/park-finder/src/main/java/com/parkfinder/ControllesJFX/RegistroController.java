package com.parkfinder.controller;

import com.parkfinder.entities.TipoVehiculo;
import com.parkfinder.repositories.TipoVehiculoRepository;
import com.parkfinder.services.UsuarioService;
import com.parkfinder.util.SesionActual;
import com.parkfinder.util.ViewLoader;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
public class RegistroController implements Initializable {

    @Autowired private UsuarioService usuarioService;
    @Autowired private TipoVehiculoRepository tipoVehiculoRepository;

    @FXML private TextField nombreField;
    @FXML private TextField apellidoField;
    @FXML private TextField correoField;
    @FXML private PasswordField contrasenaField;
    @FXML private PasswordField confirmarContrasenaField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    @FXML
    private void handleSiguiente(ActionEvent e) {
        String nombre = nombreField.getText().trim();
        String apellido = apellidoField.getText().trim();
        String correo = correoField.getText().trim();
        String contrasena = contrasenaField.getText();
        String confirmar = confirmarContrasenaField.getText();

        if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            mostrarError("Todos los campos son obligatorios.");
            return;
        }
        if (!contrasena.equals(confirmar)) {
            mostrarError("Las contraseñas no coinciden.");
            return;
        }
        if (contrasena.length() < 8) {
            mostrarError("La contraseña debe tener al menos 8 caracteres.");
            return;
        }

        List<TipoVehiculo> tipos = tipoVehiculoRepository.findAll();
        Optional<String[]> resultado = construirDialogVehiculo(tipos).showAndWait();

        if (resultado.isEmpty() || resultado.get() == null) return;

        String placa = resultado.get()[0];
        Long idTipo = Long.parseLong(resultado.get()[1]);

        try {
            var usuario = usuarioService.registrarUsuario(
                    nombre + " " + apellido, correo, contrasena, placa, idTipo
            );
            SesionActual.setUsuario(usuario);
            Stage stage = (Stage) nombreField.getScene().getWindow();
            ViewLoader.navegar(stage, "Dashboard.fxml");
        } catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
    }

    @FXML
    private void handleCancelar(ActionEvent e) {
        Stage stage = (Stage) nombreField.getScene().getWindow();
        ViewLoader.navegar(stage, "Login.fxml");
    }

    private Dialog<String[]> construirDialogVehiculo(List<TipoVehiculo> tipos) {
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Registrar vehículo");
        dialog.setHeaderText("Ingresa los datos de tu vehículo");

        ButtonType confirmarBtn = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmarBtn, ButtonType.CANCEL);

        TextField placaField = new TextField();
        placaField.setPromptText("Ej: ABC-123");
        placaField.setPrefWidth(200);

        ComboBox<TipoVehiculo> tipoCombo = new ComboBox<>();
        tipoCombo.setItems(FXCollections.observableArrayList(tipos));
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
        grid.add(new Label("Tipo de vehículo:"), 0, 1);
        grid.add(tipoCombo, 1, 1);
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        dialog.setResultConverter(btn -> {
            if (btn == confirmarBtn) {
                String placa = placaField.getText().trim();
                if (placa.isEmpty() || tipoCombo.getValue() == null) return null;
                return new String[]{placa, String.valueOf(tipoCombo.getValue().getIdTipoVehiculo())};
            }
            return null;
        });

        return dialog;
    }

    private void mostrarError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}