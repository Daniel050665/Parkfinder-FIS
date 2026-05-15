package com.parkfinder.services;

import com.parkfinder.dtos.CupoDto;
import com.parkfinder.entities.Cupo;
import com.parkfinder.repositories.CupoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Servicio que gestiona la consulta de cupos disponibles
 * en los parqueaderos del sistema.
 *
 * @author Equipo ParkFinder
 * @version 1.0
 */
@Service
@Transactional
public class CupoService {

    private final CupoRepository cupoRepository;

    public CupoService(CupoRepository cupoRepository) {
        this.cupoRepository = cupoRepository;
    }

        /**
     * Obtiene la lista de cupos disponibles en un parqueadero.
     *
     * @param idParqueadero identificador del parqueadero
     * @return lista de DTOs con informacion de los cupos disponibles
     */
public List<CupoDto> verCuposDisponibles(Long idParqueadero){

        List<Cupo> cupos = cupoRepository
                .findByParqueaderoIdParqueaderoAndDisponibleTrue(idParqueadero);

        return cupos.stream()
                .map(c -> new CupoDto(
                        c.getIdCupo(),
                        c.getNumeroCupo(),
                        c.getDisponible()
                ))
                .toList();
    }
}