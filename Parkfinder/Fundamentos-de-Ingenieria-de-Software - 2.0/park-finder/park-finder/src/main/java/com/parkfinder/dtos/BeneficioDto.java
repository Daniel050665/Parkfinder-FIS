package com.parkfinder.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * Objeto de transferencia de datos para operaciones de beneficios.
 * Transporta el identificador del usuario asociado.
 *
 * @author Equipo ParkFinder
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
public class BeneficioDto {

    private Long idUsuario;
}