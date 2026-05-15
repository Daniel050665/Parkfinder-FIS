package com.parkfinder.services;

import com.parkfinder.entities.Cupo;
import com.parkfinder.entities.Pago;
import com.parkfinder.entities.Reserva;
import com.parkfinder.entities.Usuario;
import com.parkfinder.repositories.PagoRepository;
import com.parkfinder.repositories.ReservaRepository;
import com.parkfinder.repositories.UsuarioRepository;
import com.parkfinder.repositories.CupoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Servicio que gestiona el procesamiento de pagos simulados.
 * Calcula montos, libera cupos, actualiza el historial de uso
 * y aplica el sistema de fidelizacion.
 *
 * @author Equipo ParkFinder
 * @version 1.0
 */
@Service
@Transactional
public class PagoService {

    private final PagoRepository pagoRepository;
    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final CupoRepository cupoRepository;
    private final BeneficioService beneficioService;

    public PagoService(PagoRepository pagoRepository,
                       ReservaRepository reservaRepository,
                       UsuarioRepository usuarioRepository,
                       CupoRepository cupoRepository,
                       BeneficioService beneficioService) {

        this.pagoRepository = pagoRepository;
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
        this.cupoRepository = cupoRepository;
        this.beneficioService = beneficioService;
    }

        /**
     * Procesa un pago simulado para una reserva activa.
     * Calcula el monto, libera el cupo, finaliza la reserva y
     * aplica el sistema de fidelizacion.
     *
     * @param idReserva  identificador de la reserva a pagar
     * @param metodoPago metodo de pago (EFECTIVO, TARJETA, PSE)
     * @param tipoPago   tipo de pago (PAGO_TIEMPO o SUSCRIPCION)
     * @return el pago registrado con estado COMPLETADO
     * @throws RuntimeException si la reserva no existe o no esta activa
     */
public Pago crearPago(Long idReserva, String metodoPago, String tipoPago) {

        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        if (!reserva.getEstado().equals("ACTIVA")) {
            throw new RuntimeException("La reserva no está activa");
        }

        double monto = calcularMonto(reserva, tipoPago);

        Pago pago = new Pago();
        pago.setReserva(reserva);
        pago.setUsuario(reserva.getUsuario());
        pago.setMonto(monto);
        pago.setMetodoPago(metodoPago);
        pago.setTipoPago(tipoPago);
        pago.setEstadoPago("COMPLETADO");
        pago.setFechaPago(LocalDateTime.now());

        // La reserva queda ACTIVA - se finaliza cuando el usuario termina de usar el cupo
        reservaRepository.save(reserva);

        Pago saved = pagoRepository.save(pago);
        System.out.println("[PagoService] Pago procesado ID=" + saved.getIdPago()
            + " | Monto=$" + String.format("%,.0f", monto)
            + " | Metodo=" + metodoPago + " | Reserva=" + idReserva);
        return saved;
    }

    /**
     * Calcula el monto a pagar segun el tipo de pago y la tarifa del parqueadero.
     *
     * @param reserva  reserva asociada al pago
     * @param tipoPago tipo de pago (PAGO_TIEMPO o SUSCRIPCION)
     * @return monto calculado en pesos colombianos
     */
    private double calcularMonto(Reserva reserva, String tipoPago) {

        int horas = reserva.getTiempoEstimadoHoras();
        double precioPorHora = reserva.getCupo().getParqueadero().getPrecioPorHora();

        if ("PAGO_TIEMPO".equals(tipoPago)) {
            return horas * precioPorHora;
        }

        if ("SUSCRIPCION".equals(tipoPago)) {
            return reserva.getCupo().getParqueadero().getPrecioSuscripcion();
        }

        throw new RuntimeException("Tipo de pago invalido");
    }
}