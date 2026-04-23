package com.parkfinder.util;

import com.parkfinder.entities.Parqueadero;
import com.parkfinder.entities.Reserva;
import com.parkfinder.entities.Usuario;

public class SesionActual {

    private static Usuario usuarioActual;
    private static Parqueadero parqueaderoSeleccionado;
    private static Reserva reservaActual;

    public static Usuario getUsuario() { return usuarioActual; }
    public static void setUsuario(Usuario u) { usuarioActual = u; }

    public static Parqueadero getParqueadero() { return parqueaderoSeleccionado; }
    public static void setParqueadero(Parqueadero p) { parqueaderoSeleccionado = p; }

    public static Reserva getReserva() { return reservaActual; }
    public static void setReserva(Reserva r) { reservaActual = r; }

    public static void cerrarSesion() {
        usuarioActual = null;
        parqueaderoSeleccionado = null;
        reservaActual = null;
    }
}