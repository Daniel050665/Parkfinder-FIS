package com.parkfinder.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parkfinder.dtos.VehiculoDto;
import com.parkfinder.entities.Vehiculo;
import com.parkfinder.services.VehiculoService;

@RestController
@RequestMapping("/vehiculos")
public class VehiculoController {
    private final VehiculoService vehiculoService;

    public VehiculoController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    @PostMapping
    public Vehiculo registrarVehiculo(@RequestBody VehiculoDto vehiculoDto){

        return vehiculoService.registrarVehiculo(
                vehiculoDto.getPlaca(),
                vehiculoDto.getIdUsuario(),
                vehiculoDto.getIdTipoVehiculo()
        );
    }
}
