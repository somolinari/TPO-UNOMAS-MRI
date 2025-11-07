package com.tpopdsunomas.patterns.strategy;

import com.tpopdsunomas.model.Cuenta;

/**
 * Patrón Strategy - Nivel Principiante
 * Representa jugadores con nivel de 0 a 10 puntos
 */
public class Principiante implements INivelJugador {
    private int puntosNivel;
    private Cuenta cuenta;

    public Principiante(int puntosNivel, Cuenta cuenta) {
        this.puntosNivel = puntosNivel;
        this.cuenta = cuenta;
        cuenta.setNivel(this);
    }

    @Override
    public String getNombre() {
        return "Principiante";
    }

    @Override
    public int getNivel() {
        return puntosNivel;
    }

    @Override
    public boolean puedeJugarCon(INivelJugador otroNivel) {
        // Un principiante puede jugar con cualquier nivel
        return true;
    }

    @Override
    public void subirNivel(Cuenta cuenta) {
        this.puntosNivel++;
        if (this.puntosNivel > 10) {
            // Cambiar a Intermedio
            new Intermedio(this.puntosNivel, cuenta);
            System.out.println("¡Felicitaciones! Has subido a nivel Intermedio");
        }
    }

    @Override
    public void bajarNivel(Cuenta cuenta) {
        if (this.puntosNivel > 0) {
            this.puntosNivel--;
        }
    }

    @Override
    public String toString() {
        return getNombre() + " (" + puntosNivel + " pts)";
    }
}
