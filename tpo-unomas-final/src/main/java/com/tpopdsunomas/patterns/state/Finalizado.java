package com.tpopdsunomas.patterns.state;

import com.tpopdsunomas.model.Cuenta;
import com.tpopdsunomas.model.Partido;

/**
 * Patrón State - El partido ha concluido
 * Se pueden registrar estadísticas y comentarios
 */
public class Finalizado implements IEstadoPartido {

    @Override
    public String getNombre() {
        return "Finalizado";
    }

    @Override
    public void agregarJugador(Partido partido, Cuenta jugador) {
        System.out.println("⚠ El partido ya finalizó. No se pueden agregar jugadores.");
    }

    @Override
    public void confirmar(Partido partido) {
        System.out.println("⚠ El partido ya finalizó");
    }

    @Override
    public void iniciarJuego(Partido partido) {
        System.out.println("⚠ El partido ya finalizó");
    }

    @Override
    public void finalizar(Partido partido) {
        System.out.println("⚠ El partido ya está finalizado");
    }

    @Override
    public void cancelar(Partido partido) {
        System.out.println("⚠ No se puede cancelar: el partido ya finalizó");
    }
}
