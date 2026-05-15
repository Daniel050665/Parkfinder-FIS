package com.parkfinder.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * Objeto de transferencia de datos para el procesamiento de pagos.
 * Transporta identificador de reserva, metodo y tipo de pago.
 *
 * @author Equipo ParkFinder
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
public class PagoDto {
    private Long idReserva;
    private String metodoPago;
    private String tipoPago;
}