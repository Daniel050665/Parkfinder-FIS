package com.parkfinder.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.time.LocalDate;
/**
 * Entidad JPA que representa una suscripcion mensual de un usuario.
 * Permite acceso a beneficios adicionales durante su periodo de vigencia.
 *
 * @author Equipo ParkFinder
 * @version 1.0
 */
@Entity
@Table(name = "SUSCRIPCIONES")
@Getter
@Setter
@NoArgsConstructor
public class Suscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSuscripcion;

    private String tipoPlan;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private String estado;

    @ManyToOne
    @JoinColumn(name = "ID_USUARIO")
    private Usuario usuario;

}