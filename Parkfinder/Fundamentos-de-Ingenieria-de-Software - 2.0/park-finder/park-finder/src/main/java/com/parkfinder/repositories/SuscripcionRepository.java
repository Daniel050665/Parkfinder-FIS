package com.parkfinder.repositories;

import com.parkfinder.entities.Suscripcion;
import com.parkfinder.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SuscripcionRepository extends JpaRepository<Suscripcion, Long> {
    Optional<Suscripcion> findFirstByUsuarioAndEstado(Usuario usuario, String estado);
    Optional<Suscripcion> findFirstByUsuarioOrderByFechaInicioDesc(Usuario usuario);
}