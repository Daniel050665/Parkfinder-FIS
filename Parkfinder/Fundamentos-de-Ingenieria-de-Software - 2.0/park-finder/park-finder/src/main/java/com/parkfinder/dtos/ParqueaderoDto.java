package com.parkfinder.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
public class ParqueaderoDto {
    private String nombre;
    private String direccion;
    private String zona;
    private Integer cuposTotales;
    private Double precioPorHora;
    private Double precioSuscripcion;
}
