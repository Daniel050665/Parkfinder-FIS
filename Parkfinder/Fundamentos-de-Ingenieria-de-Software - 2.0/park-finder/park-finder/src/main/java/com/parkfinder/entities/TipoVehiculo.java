package com.parkfinder.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
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