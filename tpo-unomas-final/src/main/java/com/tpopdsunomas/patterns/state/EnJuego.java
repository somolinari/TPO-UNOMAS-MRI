package com.tpopdsunomas.patterns.state;

import com.tpopdsunomas.model.Cuenta;
import com.tpopdsunomas.model.Partido;

/**
 * Patrón State - El partido está en juego
 * Se transiciona automáticamente cuando llega la fecha y hora del encuentro
 */
public class EnJuego implements IEstadoPartido {

    @Override
    public String getNombre() {
        return "En Juego";
    }

    @Override
    public void agregarJugador(Partido partido, Cuenta jugador) {
        System.out.println("⚠ El partido ya comenzó. No se pueden agregar jugadores.");
    }

    @Override
    public void confirmar(Partido partido) {
        System.out.println("⚠ El partido ya está en curso");
    }

    @Override
    public void iniciarJuego(Partido partido) {
        System.out.println("⚠ El partido ya está en juego");
    }

    @Override
    public void finalizar(Partido partido) {
        System.out.println("✓ ¡Partido finalizado! Pueden registrarse estadísticas y comentarios.");
        partido.setEstado(new Finalizado());
    }

    @Override
    public void cancelar(Partido partido) {
        System.out.println("⚠ No se puede cancelar: el partido ya está en juego");
    }
}
