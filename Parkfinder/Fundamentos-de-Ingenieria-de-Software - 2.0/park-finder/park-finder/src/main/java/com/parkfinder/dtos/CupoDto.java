package com.parkfinder.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
public class CupoDto {
    private Long idCupo;
    private Integer numeroCupo;
    private boolean disponible;
}
