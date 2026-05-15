package com.parkfinder.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parkfinder.entities.Cupo;
import com.parkfinder.entities.Parqueadero;
import com.parkfinder.repositories.CupoRepository;
import com.parkfinder.repositories.ParqueaderoRepository;

import java.util.List;
/**
 * Servicio que gestiona la creacion de parqueaderos y sus cupos,
 * asi como la busqueda por zona geografica.
 *
 * @author Equipo ParkFinder
 * @version 1.0
 */
@Service
@Transactional
public class ParqueaderoService {
     private final ParqueaderoRepository parqueaderoRepository;
    private final CupoRepository cupoRepository;

    public ParqueaderoService(ParqueaderoRepository parqueaderoRepository,
                              CupoRepository cupoRepository) {

        this.parqueaderoRepository = parqueaderoRepository;
        this.cupoRepository = cupoRepository;
    }

        /**
     * Crea un nuevo parqueadero y genera automaticamente sus cupos.
     *
     * @param parqueadero entidad con los datos del parqueadero
     * @return el parqueadero creado y persistido
     */
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

        /**
     * Busca parqueaderos por zona geografica.
     *
     * @param zona nombre de la zona o barrio
     * @return lista de parqueaderos en la zona indicada
     */
public List<Parqueadero> buscarPorZona(String zona){
        return parqueaderoRepository.findByZona(zona);
    }
}