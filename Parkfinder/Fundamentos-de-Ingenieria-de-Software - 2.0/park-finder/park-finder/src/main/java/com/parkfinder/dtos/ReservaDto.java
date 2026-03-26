package com.parkfinder.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ReservaDto {

    private Long idUsuario;
    private Long idVehiculo;
    private Long idCupo;
    private LocalDateTime fechaInicio;
    private Integer tiempoEstimadoHoras;

}