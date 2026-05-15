package com.parkfinder.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

/**
 * Objeto de transferencia de datos para la informacion de un cupo.
 * Transporta identificador, numero de cupo y estado de disponibilidad.
 *
 * @author Equipo ParkFinder
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
public class CupoDto {
    private Long idCupo;
    private Integer numeroCupo;
    private boolean disponible;
}