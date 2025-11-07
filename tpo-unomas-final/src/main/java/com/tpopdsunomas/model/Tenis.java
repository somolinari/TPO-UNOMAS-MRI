package com.tpopdsunomas.model;

public class Tenis extends Deporte {
    
    public Tenis(int id) {
        super(id, "Tenis", 2);
    }

    @Override
    public String getDescripcion() {
        return "El tenis es un deporte de raqueta practicado sobre una pista rectangular.";
    }

    @Override
    public String getReglas() {
        return "2 o 4 jugadores. Se juega al mejor de 3 o 5 sets.";
    }
}
