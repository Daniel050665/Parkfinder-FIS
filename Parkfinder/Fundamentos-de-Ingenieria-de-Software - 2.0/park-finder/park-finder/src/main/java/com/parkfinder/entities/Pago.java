package com.parkfinder.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.time.LocalDateTime;

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