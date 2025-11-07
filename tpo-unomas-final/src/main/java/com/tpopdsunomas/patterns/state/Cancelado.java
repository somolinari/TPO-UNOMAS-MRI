package com.tpopdsunomas.patterns.state;

import com.tpopdsunomas.model.Cuenta;
import com.tpopdsunomas.model.Partido;

/**
 * Patrón State - El partido fue cancelado
 * El organizador canceló el partido antes de su inicio
 */
public class Cancelado implements IEstadoPartido {

    @Override
    public String getNombre() {
        return "Cancelado";
    }

    @Override
    public void agregarJugador(Partido partido, Cuenta jugador) {
        System.out.println("⚠ El partido está cancelado. No se pueden agregar jugadores.");
    }

    @Override
    public void confirmar(Partido partido) {
        System.out.println("⚠ El partido está cancelado");
    }

    @Override
    public void iniciarJuego(Partido partido) {
        System.out.println("⚠ El partido está cancelado");
    }

    @Override
    public void finalizar(Partido partido) {
        System.out.println("⚠ El partido está cancelado");
    }

    @Override
    public void cancelar(Partido partido) {
        System.out.println("⚠ El partido ya está cancelado");
    }
}
