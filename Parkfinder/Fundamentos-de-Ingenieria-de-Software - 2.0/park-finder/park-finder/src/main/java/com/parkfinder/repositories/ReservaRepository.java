package com.parkfinder.repositories;

import com.parkfinder.entities.Reserva;
import com.parkfinder.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByUsuario(Usuario usuario);
    Optional<Reserva> findFirstByUsuarioAndEstado(Usuario usuario, String estado);
}