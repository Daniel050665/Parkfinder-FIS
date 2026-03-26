package com.parkfinder.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

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
