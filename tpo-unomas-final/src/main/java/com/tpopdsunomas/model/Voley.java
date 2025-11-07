package com.tpopdsunomas.model;

public class Voley extends Deporte {
    
    public Voley(int id) {
        super(id, "Vóley", 12);
    }

    @Override
    public String getDescripcion() {
        return "El voleibol es un deporte donde dos equipos se enfrentan sobre un terreno separado por una red central.";
    }

    @Override
    public String getReglas() {
        return "6 jugadores por equipo. Se juega al mejor de 5 sets de 25 puntos.";
    }
}
