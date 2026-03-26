package com.parkfinder.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.time.LocalDateTime;
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