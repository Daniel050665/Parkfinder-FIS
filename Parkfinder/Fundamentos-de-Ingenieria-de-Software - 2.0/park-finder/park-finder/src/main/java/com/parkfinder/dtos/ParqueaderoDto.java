package com.parkfinder.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

/**
 * Objeto de transferencia de datos para la informacion de parqueaderos.
 * Transporta nombre, direccion, zona, cupos totales y tarifas.
 *
 * @author Equipo ParkFinder
 * @version 1.0
 */
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