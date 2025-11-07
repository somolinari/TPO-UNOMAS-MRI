package com.tpopdsunomas.patterns.strategy;

import com.tpopdsunomas.model.Cuenta;

/**
 * Patrón Strategy - Nivel Intermedio
 * Representa jugadores con nivel de 11 a 20 puntos
 */
public class Intermedio implements INivelJugador {
    private int puntosNivel;
    private Cuenta cuenta;

    public Intermedio(int puntosNivel, Cuenta cuenta) {
        this.puntosNivel = puntosNivel;
        this.cuenta = cuenta;
        cuenta.setNivel(this);
    }

    @Override
    public String getNombre() {
        return "Intermedio";
    }

    @Override
    public int getNivel() {
        return puntosNivel;
    }

    @Override
    public boolean puedeJugarCon(INivelJugador otroNivel) {
        // Un intermedio puede jugar con principiantes e intermedios, pero no siempre con avanzados
        return otroNivel.getNivel() <= 25;
    }

    @Override
    public void subirNivel(Cuenta cuenta) {
        this.puntosNivel++;
        if (this.puntosNivel > 20) {
            // Cambiar a Avanzado
            new Avanzado(this.puntosNivel, cuenta);
            System.out.println("¡Felicitaciones! Has subido a nivel Avanzado");
        }
    }

    @Override
    public void bajarNivel(Cuenta cuenta) {
        this.puntosNivel--;
        if (this.puntosNivel <= 10) {
            // Volver a Principiante
            new Principiante(this.puntosNivel, cuenta);
            System.out.println("Has bajado a nivel Principiante");
        }
    }

    @Override
    public String toString() {
        return getNombre() + " (" + puntosNivel + " pts)";
    }
}
