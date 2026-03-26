package com.parkfinder.services;

import com.parkfinder.dtos.CupoDto;
import com.parkfinder.entities.Cupo;
import com.parkfinder.repositories.CupoRepository;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CupoService {

    private final CupoRepository cupoRepository;

    public CupoService(CupoRepository cupoRepository) {
        this.cupoRepository = cupoRepository;
    }

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