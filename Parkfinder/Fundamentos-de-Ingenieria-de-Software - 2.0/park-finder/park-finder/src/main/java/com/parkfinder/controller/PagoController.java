package com.parkfinder.controller;

import com.parkfinder.entities.Reserva;
import com.parkfinder.services.PagoService;
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
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
public class PagoController implements Initializable {

    @Autowired private PagoService pagoService;

    @FXML private Label nombreParqueaderoPago;
    @FXML private Label cupoPago;
    @FXML private Label entradaPago;
    @FXML private Label salidaPago;
    @FXML private Label duracionPago;
    @FXML private Label tarifaPago;
    @FXML private Label desglosePago;
    @FXML private Label subtotalLabel;
    @FXML private Label descuentoPagoLabel;
    @FXML private Label totalPagoLabel;
    @FXML private Label avisoFidelizacion;
    @FXML private ToggleButton btnEfectivo;
    @FXML private ToggleButton btnTarjeta;
    @FXML private ToggleButton btnPSE;

    private Reserva reserva;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy · HH:mm");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        reserva = SesionActual.getReserva();
        if (reserva == null) return;

        var parqueadero = reserva.getCupo().getParqueadero();
        double precioHora = parqueadero.getPrecioPorHora();
        int horas = reserva.getTiempoEstimadoHoras();
        double total = horas * precioHora;

        nombreParqueaderoPago.setText(parqueadero.getNombre());
        cupoPago.setText("Cupo #" + reserva.getCupo().getNumeroCupo());
        entradaPago.setText(reserva.getFechaInicio().format(FMT));
        salidaPago.setText(reserva.getFechaFin().format(FMT));
        duracionPago.setText(horas + (horas == 1 ? " hora" : " horas"));
        tarifaPago.setText("$" + (int) precioHora + "/hora");
        desglosePago.setText(horas + " horas × $" + (int) precioHora);
        subtotalLabel.setText("$" + String.format("%,.0f", total));
        descuentoPagoLabel.setText("- $0");
        totalPagoLabel.setText("$" + String.format("%,.0f", total));

        int usos = reserva.getUsuario().getUsosAcumulados();
        int restantes = 3 - (usos % 3);
        avisoFidelizacion.setText(restantes == 1
                ? "Este pago completa 3 usos. ¡Próxima reserva gratis!"
                : restantes + " usos más para reserva gratis.");

        ToggleGroup group = new ToggleGroup();
        btnEfectivo.setToggleGroup(group);
        btnTarjeta.setToggleGroup(group);
        btnPSE.setToggleGroup(group);
        btnEfectivo.setSelected(true);
    }

    @FXML
    private void handleConfirmarPago(ActionEvent e) {
        if (reserva == null) return;

        String metodoPago = "EFECTIVO";
        if (btnTarjeta.isSelected()) metodoPago = "TARJETA";
        else if (btnPSE.isSelected()) metodoPago = "PSE";

        try {
            pagoService.crearPago(reserva.getIdReserva(), metodoPago, "PAGO_TIEMPO");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("¡Pago exitoso!");
            alert.setHeaderText("Reserva confirmada");
            alert.setContentText("Tu reserva ha sido procesada exitosamente. ¡Buen viaje!");
            alert.showAndWait();

            Stage stage = (Stage) totalPagoLabel.getScene().getWindow();
            ViewLoader.navegar(stage, "MisReservas.fxml");
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Error al procesar el pago: " + ex.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleCancelarPago(ActionEvent e) {
        Stage stage = (Stage) totalPagoLabel.getScene().getWindow();
        ViewLoader.navegar(stage, "Dashboard.fxml");
    }
}