package com.tpopdsunomas.patterns.state;

import com.tpopdsunomas.model.Cuenta;
import com.tpopdsunomas.model.Partido;

/**
 * Patrón State - Interfaz para estados del partido
 * Define las transiciones de estado permitidas
 */
public interface IEstadoPartido {
    String getNombre();
    void agregarJugador(Partido partido, Cuenta jugador);
    void confirmar(Partido partido);
    void iniciarJuego(Partido partido);
    void finalizar(Partido partido);
    void cancelar(Partido partido);
}
