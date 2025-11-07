package com.tpopdsunomas.model;

public class PingPong extends Deporte {
    
    public PingPong(int id) {
        super(id, "Ping Pong", 2);
    }

    @Override
    public String getDescripcion() {
        return "El tenis de mesa o ping pong es un deporte de raqueta que se disputa entre dos jugadores.";
    }

    @Override
    public String getReglas() {
        return "2 jugadores. Se juega al mejor de 5 o 7 sets de 11 puntos.";
    }
}
