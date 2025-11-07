package com.tpopdsunomas.model;

public class Padel extends Deporte {
    
    public Padel(int id) {
        super(id, "Pádel", 4);
    }

    @Override
    public String getDescripcion() {
        return "El pádel es un deporte de raqueta que se juega en parejas.";
    }

    @Override
    public String getReglas() {
        return "4 jugadores (2 parejas). Se juega al mejor de 3 sets.";
    }
}
