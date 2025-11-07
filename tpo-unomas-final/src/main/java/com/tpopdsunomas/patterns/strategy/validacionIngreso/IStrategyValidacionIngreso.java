package com.tpopdsunomas.patterns.strategy.validacionIngreso;

import com.tpopdsunomas.model.Cuenta;
import com.tpopdsunomas.model.Partido;

public interface IStrategyValidacionIngreso {
    boolean esCompatible(Cuenta jugador, Partido partido);
    String getNombreRegla();
}
