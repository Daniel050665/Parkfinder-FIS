package com.parkfinder.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * Objeto de transferencia de datos para la creacion de reservas.
 * Transporta IDs de usuario, vehiculo y cupo, junto con fecha
 * de inicio y tiempo estimado.
 *
 * @author Equipo ParkFinder
 * @version 1.0
 */
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