package com.parkfinder.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * Objeto de transferencia de datos para el registro de usuarios.
 * Transporta nombre, correo, contrasena, placa y tipo de vehiculo.
 *
 * @author Equipo ParkFinder
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
public class UsuarioDto {
    private String nombre;
    private String correo;
    private String contrasena;
    private String placa;
    private Long idTipoVehiculo;
}