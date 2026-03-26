package com.parkfinder.services;

import org.springframework.stereotype.Service;

import com.parkfinder.entities.Cupo;
import com.parkfinder.entities.Parqueadero;
import com.parkfinder.repositories.CupoRepository;
import com.parkfinder.repositories.ParqueaderoRepository;

import java.util.List;
@Service
public class ParqueaderoService {
     private final ParqueaderoRepository parqueaderoRepository;
    private final CupoRepository cupoRepository;

    public ParqueaderoService(ParqueaderoRepository parqueaderoRepository,
                              CupoRepository cupoRepository) {

        this.parqueaderoRepository = parqueaderoRepository;
        this.cupoRepository = cupoRepository;
    }

    public Parqueadero crearParqueadero(Parqueadero parqueadero){

        Parqueadero parqueaderoGuardado = parqueaderoRepository.save(parqueadero);

        for(int i = 1; i <= parqueadero.getCuposTotales(); i++){

            Cupo cupo = new Cupo();
            cupo.setNumeroCupo(i);
            cupo.setDisponible(true);
            cupo.setParqueadero(parqueaderoGuardado);

            cupoRepository.save(cupo);
        }

        return parqueaderoGuardado;
    }

    public List<Parqueadero> buscarPorZona(String zona){
        return parqueaderoRepository.findByZona(zona);
    }
}
