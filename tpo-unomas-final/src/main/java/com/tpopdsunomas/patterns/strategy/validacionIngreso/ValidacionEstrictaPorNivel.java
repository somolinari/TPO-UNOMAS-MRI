package com.tpopdsunomas.patterns.strategy.validacionIngreso;

import com.tpopdsunomas.model.Cuenta;
import com.tpopdsunomas.model.Partido;

public class ValidacionEstrictaPorNivel implements IStrategyValidacionIngreso{


    @Override
    public boolean esCompatible(Cuenta jugador, Partido partido) {
        
        // 1. Si el partido no definió un nivel, permite la entrada.
        if (partido.getNivelRequerido() == null) {
            return true; 
        }
        
        // 2. Si el jugador no tiene nivel, lo rechaza.
        if (jugador.getNivel() == null) {
            return false;
        }

        // 3. Compara si el nombre del nivel del jugador (ej: "Intermedio")
        //    es igual al nombre del nivel requerido por el partido.
        String nivelRequerido = partido.getNivelRequerido().getNombre();
        String nivelJugador = jugador.getNivel().getNombre();
        
        return nivelRequerido.equals(nivelJugador);
    }

    @Override
    public String getNombreRegla() {
        return "Solo para nivel específico";
    }
} 
