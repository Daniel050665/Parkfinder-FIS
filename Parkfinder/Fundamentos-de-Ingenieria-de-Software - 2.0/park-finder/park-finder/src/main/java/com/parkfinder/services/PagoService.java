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

import java.time.LocalDateTime;

@Service
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

        Cupo cupo = reserva.getCupo();
        cupo.setDisponible(true);
        cupoRepository.save(cupo);

        reserva.setEstado("FINALIZADA");

        Usuario usuario = reserva.getUsuario();
        usuario.setUsosAcumulados(usuario.getUsosAcumulados() + 1);
        usuarioRepository.save(usuario);

        beneficioService.generarBeneficioSiAplica(usuario);

        reservaRepository.save(reserva);

        return pagoRepository.save(pago);
    }

    private double calcularMonto(Reserva reserva, String tipoPago) {

        int horas = reserva.getTiempoEstimadoHoras();

        if ("PAGO_TIEMPO".equals(tipoPago)) {
            return horas * 4000;
        }

        if ("SUSCRIPCION".equals(tipoPago)) {
            return 80000;
        }

        throw new RuntimeException("Tipo de pago inválido");
    }
}