package com.parkfinder.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
/**
 * Entidad JPA que clasifica los tipos de vehiculo aceptados
 * en el sistema (Automovil, Motocicleta, etc.).
 *
 * @author Equipo ParkFinder
 * @version 1.0
 */
@Entity
@Table(name = "TIPO_VEHICULO")
@Getter
@Setter
@NoArgsConstructor
public class TipoVehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTipoVehiculo;

    private String nombre;

}