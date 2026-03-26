package com.parkfinder.controller;

import com.parkfinder.dtos.PagoDto;
import com.parkfinder.entities.Pago;
import com.parkfinder.services.PagoService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @PostMapping
    public Pago pagar(@RequestBody PagoDto dto) {

        return pagoService.crearPago(
                dto.getIdReserva(),
                dto.getMetodoPago(),
                dto.getTipoPago()
        );
    }
}