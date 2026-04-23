package com.parkfinder.services;

import com.parkfinder.entities.Suscripcion;
import com.parkfinder.entities.Usuario;
import com.parkfinder.repositories.SuscripcionRepository;
import com.parkfinder.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class SuscripcionService {

    private final SuscripcionRepository suscripcionRepository;
    private final UsuarioRepository usuarioRepository;

    public SuscripcionService(SuscripcionRepository suscripcionRepository,
                              UsuarioRepository usuarioRepository) {
        this.suscripcionRepository = suscripcionRepository;
        this.usuarioRepository = usuarioRepository;
    }

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

    public void cancelarSuscripcion(Usuario usuario) {
        suscripcionRepository
                .findFirstByUsuarioAndEstado(usuario, "ACTIVA")
                .ifPresent(s -> {
                    s.setEstado("CANCELADA");
                    suscripcionRepository.save(s);
                });
    }

    public Optional<Suscripcion> obtenerSuscripcionActiva(Usuario usuario) {
        return suscripcionRepository.findFirstByUsuarioAndEstado(usuario, "ACTIVA");
    }
}