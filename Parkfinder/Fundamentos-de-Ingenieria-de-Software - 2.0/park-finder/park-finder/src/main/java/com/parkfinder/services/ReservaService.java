package com.parkfinder.services;

import com.parkfinder.entities.*;
import com.parkfinder.repositories.*;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final VehiculoRepository vehiculoRepository;
    private final CupoRepository cupoRepository;
    private final BeneficioService beneficioService;

    public ReservaService(ReservaRepository reservaRepository,
                          UsuarioRepository usuarioRepository,
                          VehiculoRepository vehiculoRepository,
                          CupoRepository cupoRepository,
                          BeneficioService beneficioService) {
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.cupoRepository = cupoRepository;
        this.beneficioService = beneficioService;
    }

    public Reserva crearReserva(Long idUsuario,
                               Long idVehiculo,
                               Long idCupo,
                               LocalDateTime fechaInicio,
                               Integer tiempoHoras) {

        if (idUsuario == null || idVehiculo == null || idCupo == null) {
            throw new RuntimeException("Los IDs no pueden ser null");
        }

        if (fechaInicio == null || tiempoHoras == null || tiempoHoras <= 0) {
            throw new RuntimeException("Datos de reserva inválidos");
        }

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Vehiculo vehiculo = vehiculoRepository.findById(idVehiculo)
                .orElseThrow(() -> new RuntimeException("Vehiculo no encontrado"));

        if (!vehiculo.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
            throw new RuntimeException("El vehículo no pertenece al usuario");
        }

        Cupo cupo = cupoRepository.findById(idCupo)
                .orElseThrow(() -> new RuntimeException("Cupo no encontrado"));

        if (Boolean.FALSE.equals(cupo.getDisponible())) {
            throw new RuntimeException("El cupo no está disponible");
        }

        cupo.setDisponible(false);
        cupoRepository.save(cupo);

        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setVehiculo(vehiculo);
        reserva.setCupo(cupo);
        reserva.setFechaInicio(fechaInicio);
        reserva.setTiempoEstimadoHoras(tiempoHoras);
        reserva.setFechaFin(fechaInicio.plusHours(tiempoHoras));
        reserva.setEstado("ACTIVA");
        reserva.setBeneficioAplicado(false);

        return reservaRepository.save(reserva);
    }

    public Reserva cancelarReserva(Long idReserva) {

        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        if (!reserva.getEstado().equals("ACTIVA")) {
            throw new RuntimeException("Solo se pueden cancelar reservas activas");
        }

        Cupo cupo = reserva.getCupo();
        cupo.setDisponible(true);
        cupoRepository.save(cupo);

        reserva.setEstado("CANCELADA");

        return reservaRepository.save(reserva);
    }

    public Reserva finalizarReserva(Long idReserva) {

        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        if (!reserva.getEstado().equals("ACTIVA")) {
            throw new RuntimeException("La reserva no está activa");
        }

        Cupo cupo = reserva.getCupo();
        cupo.setDisponible(true);
        cupoRepository.save(cupo);

        reserva.setEstado("FINALIZADA");

        Usuario usuario = reserva.getUsuario();
        usuario.setUsosAcumulados(usuario.getUsosAcumulados() + 1);
        usuarioRepository.save(usuario);

        beneficioService.generarBeneficioSiAplica(usuario);

        return reservaRepository.save(reserva);
    }
}