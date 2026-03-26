package com.parkfinder.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.parkfinder.entities.Parqueadero;

import java.util.List;

@Repository
public interface ParqueaderoRepository extends JpaRepository<Parqueadero, Long> {

    List<Parqueadero> findByZona(String zona);

}