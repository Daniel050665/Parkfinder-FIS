package com.parkfinder.controller;

import com.parkfinder.dtos.ReservaDto;
import com.parkfinder.entities.Reserva;
import com.parkfinder.services.ReservaService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping
    public Reserva crearReserva(@RequestBody ReservaDto dto) {
        return reservaService.crearReserva(
                dto.getIdUsuario(),
                dto.getIdVehiculo(),
                dto.getIdCupo(),
                dto.getFechaInicio(),
                dto.getTiempoEstimadoHoras()
        );
    }

    @PutMapping("/{id}/cancelar")
    public Reserva cancelarReserva(@PathVariable Long id) {
        return reservaService.cancelarReserva(id);
    }

    @PutMapping("/{id}/finalizar")
    public Reserva finalizarReserva(@PathVariable Long id) {
        return reservaService.finalizarReserva(id);
    }
}