package com.tpopdsunomas.model;

public class Futbol extends Deporte {
    
    public Futbol(int id) {
        super(id, "Fútbol", 10);
    }

    @Override
    public String getDescripcion() {
        return "El fútbol es un deporte de equipo jugado entre dos conjuntos de once jugadores cada uno.";
    }

    @Override
    public String getReglas() {
        return "11 jugadores por equipo. Duración: 90 minutos (2 tiempos de 45 min).";
    }
}
