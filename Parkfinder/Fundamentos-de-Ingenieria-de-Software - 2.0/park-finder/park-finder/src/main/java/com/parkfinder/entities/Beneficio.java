package com.parkfinder.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.time.LocalDate;
@Entity
@Table(name = "BENEFICIOS")
@Getter
@Setter
@NoArgsConstructor
public class Beneficio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBeneficio;

    private Boolean beneficioDisponible;

    private LocalDate fechaGeneracion;

    private LocalDate fechaUso;

    private Integer usosAcumulados;

    @ManyToOne
    @JoinColumn(name = "ID_USUARIO")
    private Usuario usuario;

}