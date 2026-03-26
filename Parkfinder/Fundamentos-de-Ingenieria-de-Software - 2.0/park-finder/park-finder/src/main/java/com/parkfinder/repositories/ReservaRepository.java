package com.parkfinder.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.parkfinder.entities.Reserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    
}
