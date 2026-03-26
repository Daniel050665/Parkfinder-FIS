package com.parkfinder.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class PagoDto {
    private Long idReserva;
    private String metodoPago;
    private String tipoPago;
}