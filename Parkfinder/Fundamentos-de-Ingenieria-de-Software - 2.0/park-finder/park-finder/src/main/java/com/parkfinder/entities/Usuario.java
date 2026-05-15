package com.parkfinder.entities;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

/**
 * Entidad JPA que representa a un usuario del sistema ParkFinder.
 * Almacena credenciales de acceso, fecha de registro y contador de usos
 * acumulados para el sistema de fidelizacion.
 *
 * @author Equipo ParkFinder
 * @version 1.0
 */
@Entity
@Table(name = "USUARIOS")
@Getter
@Setter
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    private String nombre;

    private String correo;

    private String contrasena;

    private Boolean estadoCuenta;

    private LocalDate fechaRegistro;

    private Integer usosAcumulados;

}