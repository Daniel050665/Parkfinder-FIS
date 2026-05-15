package com.parkfinder.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

/**
 * Entidad JPA que representa un establecimiento de parqueadero.
 * Contiene informacion de ubicacion, tarifas, horarios y capacidad
 * de cupos disponibles.
 *
 * @author Equipo ParkFinder
 * @version 1.0
 */
@Entity
@Table(name = "PARQUEADEROS")
@Getter
@Setter
@NoArgsConstructor
public class Parqueadero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idParqueadero;

    private String nombre;

    private String direccion;

    private String zona;

    private Integer cuposTotales;

    private Integer cuposDisponibles;

    private Double precioPorHora;

    private Double precioSuscripcion;

    private String horarioApertura;

    private String horarioCierre;

    private String estado;

    private Double latitud;

    private Double longitud;

}