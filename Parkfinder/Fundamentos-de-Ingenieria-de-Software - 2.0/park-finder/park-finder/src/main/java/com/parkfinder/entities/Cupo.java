package com.parkfinder.entities;


import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

/**
 * Entidad JPA que representa un espacio individual de estacionamiento
 * dentro de un parqueadero. Controla la disponibilidad mediante un
 * atributo booleano.
 *
 * @author Equipo ParkFinder
 * @version 1.0
 */
@Entity
@Table(name = "CUPOS")
@Getter
@Setter
@NoArgsConstructor
public class Cupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCupo;

    private Integer numeroCupo;

    private Boolean disponible;

    @ManyToOne
    @JoinColumn(name = "ID_PARQUEADERO")
    private Parqueadero parqueadero;

}