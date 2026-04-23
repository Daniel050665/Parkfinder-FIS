package com.parkfinder.repositories;

import com.parkfinder.entities.Usuario;
import com.parkfinder.entities.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    List<Vehiculo> findByUsuario(Usuario usuario);
}