package com.parkfinder.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.time.LocalDateTime;
/**
 * Entidad JPA que representa una reserva de cupo de parqueadero.
 * Registra fecha de inicio, fin, tiempo estimado, estado y si se
 * aplico un beneficio de fidelizacion.
 *
 * @author Equipo ParkFinder
 * @version 1.0
 */
@Entity
@Table(name = "RESERVAS")
@Getter
@Setter
@NoArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReserva;

    private LocalDateTime fechaInicio;

    private LocalDateTime fechaFin;

    private Integer tiempoEstimadoHoras;

    private String estado;

    private Boolean beneficioAplicado;

    @ManyToOne
    @JoinColumn(name = "ID_USUARIO")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "ID_VEHICULO")
    private Vehiculo vehiculo;

    @ManyToOne
    @JoinColumn(name = "ID_CUPO")
    private Cupo cupo;
}