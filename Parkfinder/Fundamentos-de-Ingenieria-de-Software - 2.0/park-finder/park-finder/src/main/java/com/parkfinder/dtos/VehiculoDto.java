package com.parkfinder.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
public class VehiculoDto {
    private String placa;
    private Long idUsuario;
    private Long idTipoVehiculo;
}
