package com.parkfinder.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

/**
 * Objeto de transferencia de datos para el registro de vehiculos.
 * Transporta placa, identificador de usuario y tipo de vehiculo.
 *
 * @author Equipo ParkFinder
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
public class VehiculoDto {
    private String placa;
    private Long idUsuario;
    private Long idTipoVehiculo;
}