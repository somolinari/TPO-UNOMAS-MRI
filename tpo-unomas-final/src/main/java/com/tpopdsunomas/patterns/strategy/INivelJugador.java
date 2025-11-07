package com.tpopdsunomas.patterns.strategy;

import com.tpopdsunomas.model.Cuenta;

/**
 * Patrón Strategy - Interfaz para niveles de jugador
 * Evita usar enum y permite comportamiento polimórfico
 */
public interface INivelJugador {
    String getNombre();
    int getNivel();
    boolean puedeJugarCon(INivelJugador otroNivel);
    void subirNivel(Cuenta cuenta);
    void bajarNivel(Cuenta cuenta);
}
