package com.tpopdsunomas.patterns.state;

import com.tpopdsunomas.model.Cuenta;
import com.tpopdsunomas.model.Partido;

/**
 * Patrón State - El partido tiene suficientes jugadores
 * Esperando confirmación de todos los participantes
 */
public class Armado implements IEstadoPartido {

    @Override
    public String getNombre() {
        return "Partido Armado";
    }

    @Override
    public void agregarJugador(Partido partido, Cuenta jugador) {
        System.out.println("⚠ El partido ya está completo. No se pueden agregar más jugadores.");
    }

    @Override
    public void confirmar(Partido partido) {
        System.out.println("✓ Todos los jugadores han confirmado. Partido confirmado!");
        partido.setEstado(new Confirmado());
    }

    @Override
    public void iniciarJuego(Partido partido) {
        System.out.println("⚠ No se puede iniciar: el partido aún no está confirmado por todos");
    }

    @Override
    public void finalizar(Partido partido) {
        System.out.println("⚠ No se puede finalizar: el partido aún no ha comenzado");
    }

    @Override
    public void cancelar(Partido partido) {
        System.out.println("✓ Cancelando partido armado...");
        partido.setEstado(new Cancelado());
    }
}
