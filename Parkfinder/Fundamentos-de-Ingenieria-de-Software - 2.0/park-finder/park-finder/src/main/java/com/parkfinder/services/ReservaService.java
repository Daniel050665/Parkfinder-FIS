package com.parkfinder.services;

import com.parkfinder.entities.*;
import com.parkfinder.repositories.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Servicio que gestiona la logica de negocio para la creacion,
 * cancelacion y finalizacion de reservas de cupos de parqueadero.
 *
 * @author Equipo ParkFinder
 * @version 1.0
 */
@Service
@Transactional
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

        /**
     * Crea una nueva reserva de cupo de parqueadero.
     * Verifica disponibilidad del cupo y lo bloquea automaticamente.
     *
     * @param idUsuario   identificador del usuario que reserva
     * @param idVehiculo  identificador del vehiculo asociado
     * @param idCupo      identificador del cupo a reservar
     * @param fechaInicio fecha y hora de inicio de la reserva
     * @param tiempoHoras duracion estimada en horas
     * @return la reserva creada con estado ACTIVA
     * @throws RuntimeException si el cupo no esta disponible o los datos son invalidos
     */
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

        Reserva saved = reservaRepository.save(reserva);
        System.out.println("[ReservaService] Reserva creada ID=" + saved.getIdReserva()
            + " | Usuario=" + idUsuario + " | Cupo=" + idCupo
            + " | Horas=" + tiempoHoras + " | Estado=" + saved.getEstado());
        return saved;
    }

        /**
     * Cancela una reserva activa y libera el cupo asociado.
     *
     * @param idReserva identificador de la reserva a cancelar
     * @return la reserva actualizada con estado CANCELADA
     * @throws RuntimeException si la reserva no existe o no esta activa
     */
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

        Reserva saved = reservaRepository.save(reserva);
        System.out.println("[ReservaService] Reserva cancelada ID=" + idReserva);
        return saved;
    }

        /**
     * Finaliza una reserva activa, libera el cupo y actualiza
     * el contador de usos acumulados del usuario.
     *
     * @param idReserva identificador de la reserva a finalizar
     * @return la reserva actualizada con estado FINALIZADA
     * @throws RuntimeException si la reserva no existe o no esta activa
     */
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