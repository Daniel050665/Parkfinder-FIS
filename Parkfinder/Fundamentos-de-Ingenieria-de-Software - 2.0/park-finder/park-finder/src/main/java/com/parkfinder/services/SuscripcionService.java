package com.parkfinder.services;

import com.parkfinder.entities.Suscripcion;
import com.parkfinder.entities.Usuario;
import com.parkfinder.repositories.SuscripcionRepository;
import com.parkfinder.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Servicio que gestiona la activacion, cancelacion y consulta
 * de suscripciones mensuales de los usuarios.
 *
 * @author Equipo ParkFinder
 * @version 1.0
 */
@Service
@Transactional
public class SuscripcionService {

    private final SuscripcionRepository suscripcionRepository;
    private final UsuarioRepository usuarioRepository;

    public SuscripcionService(SuscripcionRepository suscripcionRepository,
                              UsuarioRepository usuarioRepository) {
        this.suscripcionRepository = suscripcionRepository;
        this.usuarioRepository = usuarioRepository;
    }

        /**
     * Activa una suscripcion mensual para un usuario.
     * Cancela cualquier suscripcion activa previa.
     *
     * @param usuario  el usuario que adquiere la suscripcion
     * @param tipoPlan nombre del plan (ej: "Mensual")
     * @return la suscripcion creada con vigencia de 30 dias
     */
public Suscripcion activarSuscripcion(Usuario usuario, String tipoPlan) {
        // Cancelar suscripción activa previa si existe
        suscripcionRepository
                .findFirstByUsuarioAndEstado(usuario, "ACTIVA")
                .ifPresent(s -> {
                    s.setEstado("CANCELADA");
                    suscripcionRepository.save(s);
                });

        Suscripcion nueva = new Suscripcion();
        nueva.setUsuario(usuario);
        nueva.setTipoPlan(tipoPlan);
        nueva.setFechaInicio(LocalDate.now());
        nueva.setFechaFin(LocalDate.now().plusMonths(1));
        nueva.setEstado("ACTIVA");

        usuario.setUsosAcumulados(0);
        usuarioRepository.save(usuario);

        return suscripcionRepository.save(nueva);
    }

        /**
     * Cancela la suscripcion activa de un usuario.
     *
     * @param usuario el usuario cuya suscripcion se cancela
     */
public void cancelarSuscripcion(Usuario usuario) {
        suscripcionRepository
                .findFirstByUsuarioAndEstado(usuario, "ACTIVA")
                .ifPresent(s -> {
                    s.setEstado("CANCELADA");
                    suscripcionRepository.save(s);
                });
    }

        /**
     * Consulta la suscripcion activa de un usuario.
     *
     * @param usuario el usuario a consultar
     * @return Optional con la suscripcion activa, o vacio si no tiene
     */
public Optional<Suscripcion> obtenerSuscripcionActiva(Usuario usuario) {
        return suscripcionRepository.findFirstByUsuarioAndEstado(usuario, "ACTIVA");
    }
}