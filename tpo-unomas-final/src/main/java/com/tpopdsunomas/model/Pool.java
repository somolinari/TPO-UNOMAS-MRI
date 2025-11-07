package com.tpopdsunomas.model;

public class Pool extends Deporte {
    
    public Pool(int id) {
        super(id, "Pool", 2);
    }

    @Override
    public String getDescripcion() {
        return "El pool o billar americano es un deporte de precisión que se practica en una mesa con troneras.";
    }

    @Override
    public String getReglas() {
        return "2 jugadores. Se juega embocando bolas numeradas según las reglas.";
    }
}
