package com.tpopdsunomas.model;

public class Basquet extends Deporte {
    
    public Basquet(int id) {
        super(id, "Básquet", 10);
    }

    @Override
    public String getDescripcion() {
        return "El básquetbol es un deporte de equipo donde dos conjuntos de cinco jugadores intentan anotar en la canasta contraria.";
    }

    @Override
    public String getReglas() {
        return "5 jugadores por equipo. Duración: 40 minutos (4 cuartos de 10 min).";
    }
}
