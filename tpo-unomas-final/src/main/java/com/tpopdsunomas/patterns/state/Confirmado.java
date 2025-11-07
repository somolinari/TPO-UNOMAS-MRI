package com.tpopdsunomas.patterns.state;

import com.tpopdsunomas.model.Cuenta;
import com.tpopdsunomas.model.Partido;

/**
 * Patrón State - El partido está confirmado
 * Todos los jugadores aceptaron y está listo para jugar
 */
public class Confirmado implements IEstadoPartido {

    @Override
    public String getNombre() {
        return "Confirmado";
    }

    @Override
    public void agregarJugador(Partido partido, Cuenta jugador) {
        System.out.println("⚠ El partido ya está confirmado. No se pueden agregar jugadores.");
    }

    @Override
    public void confirmar(Partido partido) {
        System.out.println("⚠ El partido ya está confirmado");
    }

    @Override
    public void iniciarJuego(Partido partido) {
        System.out.println("✓ ¡El partido está comenzando!");
        partido.setEstado(new EnJuego());
    }

    @Override
    public void finalizar(Partido partido) {
        System.out.println("⚠ No se puede finalizar: el partido aún no ha comenzado");
    }

    @Override
    public void cancelar(Partido partido) {
        System.out.println("✓ Cancelando partido confirmado...");
        partido.setEstado(new Cancelado());
    }
}
