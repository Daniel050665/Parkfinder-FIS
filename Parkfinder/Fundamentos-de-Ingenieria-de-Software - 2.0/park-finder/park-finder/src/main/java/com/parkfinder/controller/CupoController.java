package com.parkfinder.controller;

import com.parkfinder.dtos.CupoDto;
import com.parkfinder.services.CupoService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cupos")
public class CupoController {

    private final CupoService cupoService;

    public CupoController(CupoService cupoService) {
        this.cupoService = cupoService;
    }

    @GetMapping("/disponibles/{idParqueadero}")
    public List<CupoDto> verDisponibles(@PathVariable Long idParqueadero){
        return cupoService.verCuposDisponibles(idParqueadero);
    }
}