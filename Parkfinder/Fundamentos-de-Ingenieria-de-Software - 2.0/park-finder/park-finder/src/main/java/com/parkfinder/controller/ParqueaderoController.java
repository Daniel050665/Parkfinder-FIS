package com.parkfinder.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.parkfinder.entities.Parqueadero;
import com.parkfinder.services.ParqueaderoService;

import java.util.List;

@RestController
@RequestMapping("/parqueaderos")
public class ParqueaderoController {

    private final ParqueaderoService parqueaderoService;

    public ParqueaderoController(ParqueaderoService parqueaderoService) {
        this.parqueaderoService = parqueaderoService;
    }

    @PostMapping
    public Parqueadero crearParqueadero(@RequestBody Parqueadero parqueadero){
        return parqueaderoService.crearParqueadero(parqueadero);
    }

    @GetMapping("/zona")
    public List<Parqueadero> buscarPorZona(@RequestParam String zona){
        return parqueaderoService.buscarPorZona(zona);
    }
}
