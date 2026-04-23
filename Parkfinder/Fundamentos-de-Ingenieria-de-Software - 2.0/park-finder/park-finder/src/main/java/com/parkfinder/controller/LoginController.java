package com.parkfinder.controller;

import com.parkfinder.services.UsuarioService;
import com.parkfinder.util.SesionActual;
import com.parkfinder.util.ViewLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
public class LoginController implements Initializable {

    @Autowired private UsuarioService usuarioService;

    @FXML private TextField correoField;
    @FXML private PasswordField contrasenaField;
    @FXML private Label errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorLabel.setVisible(false);
    }

    @FXML
    private void handleLogin(ActionEvent e) {
        String correo = correoField.getText().trim();
        String contrasena = contrasenaField.getText();

        if (correo.isEmpty() || contrasena.isEmpty()) {
            mostrarError("Por favor completa todos los campos.");
            return;
        }

        try {
            var usuario = usuarioService.iniciarSesion(correo, contrasena);
            SesionActual.setUsuario(usuario);
            Stage stage = (Stage) correoField.getScene().getWindow();
            ViewLoader.navegar(stage, "Dashboard.fxml");
        } catch (Exception ex) {
            mostrarError("Credenciales incorrectas. Verifica tu correo y contraseña.");
        }
    }

    @FXML
    private void handleIrARegistro(ActionEvent e) {
        Stage stage = (Stage) correoField.getScene().getWindow();
        ViewLoader.navegar(stage, "Registro.fxml");
    }

    @FXML
    private void handleOlvidoContrasena(ActionEvent e) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Recuperar contraseña");
        alert.setHeaderText(null);
        alert.setContentText("Función no disponible en esta versión.");
        alert.showAndWait();
    }

    private void mostrarError(String mensaje) {
        errorLabel.setText(mensaje);
        errorLabel.setVisible(true);
    }
}