package com.parkfinder.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Entidad JPA que representa una transaccion de pago simulada.
 * Registra el monto, metodo de pago, tipo, estado y su relacion
 * con la reserva y el usuario correspondientes.
 *
 * @author Equipo ParkFinder
 * @version 1.0
 */
@Entity
@Table(name = "PAGOS")
@Getter
@Setter
@NoArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPago;

    private Double monto;

    private String metodoPago;

    private String tipoPago;

    private String estadoPago;

    private LocalDateTime fechaPago;

    @ManyToOne
    @JoinColumn(name = "ID_RESERVA")
    private Reserva reserva;

    @ManyToOne
    @JoinColumn(name = "ID_USUARIO")
    private Usuario usuario;

}