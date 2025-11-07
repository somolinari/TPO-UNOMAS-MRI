package com.tpopdsunomas.patterns.strategy;

import com.tpopdsunomas.model.Cuenta;

/**
 * Patrón Strategy - Nivel Avanzado
 * Representa jugadores con nivel mayor a 20 puntos
 */
public class Avanzado implements INivelJugador {
    private int puntosNivel;
    private Cuenta cuenta;

    public Avanzado(int puntosNivel, Cuenta cuenta) {
        this.puntosNivel = puntosNivel;
        this.cuenta = cuenta;
        cuenta.setNivel(this);
    }

    @Override
    public String getNombre() {
        return "Avanzado";
    }

    @Override
    public int getNivel() {
        return puntosNivel;
    }

    @Override
    public boolean puedeJugarCon(INivelJugador otroNivel) {
        // Un avanzado prefiere jugar con otros avanzados o intermedios avanzados
        return otroNivel.getNivel() >= 15;
    }

    @Override
    public void subirNivel(Cuenta cuenta) {
        this.puntosNivel++;
        // El avanzado no tiene nivel superior, solo acumula puntos
        if (this.puntosNivel % 10 == 0) {
            System.out.println("¡Excelente! Has alcanzado " + puntosNivel + " puntos como Avanzado");
        }
    }

    @Override
    public void bajarNivel(Cuenta cuenta) {
        this.puntosNivel--;
        if (this.puntosNivel <= 20) {
            // Volver a Intermedio
            new Intermedio(this.puntosNivel, cuenta);
            System.out.println("Has bajado a nivel Intermedio");
        }
    }

    @Override
    public String toString() {
        return getNombre() + " (" + puntosNivel + " pts)";
    }
}
