package com.tpopdsunomas.patterns.strategy;

import com.tpopdsunomas.model.Cuenta;
import com.tpopdsunomas.model.Partido;
import java.util.List;

/**
 * Patrón Strategy - Interfaz para estrategias de emparejamiento
 * Define diferentes algoritmos para buscar/filtrar partidos
 */
public interface IStrategyEmparejamiento {
    List<Partido> buscar(Cuenta buscador, List<Partido> partidosDisponibles);
    String getNombreEstrategia();
}
