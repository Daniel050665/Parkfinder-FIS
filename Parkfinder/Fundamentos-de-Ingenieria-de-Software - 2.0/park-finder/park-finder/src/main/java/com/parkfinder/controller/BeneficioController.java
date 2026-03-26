package com.parkfinder.controller;

import com.parkfinder.dtos.BeneficioDto;
import com.parkfinder.entities.Beneficio;
import com.parkfinder.services.BeneficioService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/beneficios")
public class BeneficioController {

    private final BeneficioService beneficioService;

    public BeneficioController(BeneficioService beneficioService) {
        this.beneficioService = beneficioService;
    }

    @PostMapping
    public Beneficio crear(@RequestBody BeneficioDto dto) {

        return beneficioService.crearBeneficio(dto.getIdUsuario());
    }
    
    @GetMapping("/usuario/{idUsuario}")
    public List<Beneficio> listar(@PathVariable Long idUsuario) {

        return beneficioService.obtenerBeneficios(idUsuario);
    }

    @PutMapping("/{id}/usar")
    public Beneficio usar(@PathVariable Long id) {

        return beneficioService.usarBeneficio(id);
    }
}