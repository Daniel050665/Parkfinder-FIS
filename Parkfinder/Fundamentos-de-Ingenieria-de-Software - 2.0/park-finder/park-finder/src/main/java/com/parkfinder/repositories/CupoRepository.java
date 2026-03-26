package com.parkfinder.repositories;

import com.parkfinder.entities.Cupo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CupoRepository extends JpaRepository<Cupo, Long> {

    List<Cupo> findByParqueaderoIdParqueadero(Long idParqueadero);

    List<Cupo> findByParqueaderoIdParqueaderoAndDisponibleTrue(Long idParqueadero);

}