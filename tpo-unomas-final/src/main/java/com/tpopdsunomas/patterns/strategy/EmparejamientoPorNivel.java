package com.tpopdsunomas.patterns.strategy;

import com.tpopdsunomas.model.Cuenta;
import com.tpopdsunomas.model.Partido;
import java.util.ArrayList;
import java.util.List;

/**
 * Patrón Strategy - Emparejamiento por nivel de habilidad
 * Busca partidos donde el nivel del usuario sea compatible con el nivel requerido
 */
public class EmparejamientoPorNivel implements IStrategyEmparejamiento {

    @Override
    public List<Partido> buscar(Cuenta buscador, List<Partido> partidosDisponibles) {
        List<Partido> partidosCompatibles = new ArrayList<>();
        
        System.out.println("\n🔍 Aplicando estrategia: Emparejamiento por Nivel");
        System.out.println("Nivel del buscador: " + buscador.getNivel().getNombre() + 
                         " (" + buscador.getNivel().getNivel() + " pts)");
        
        for (Partido partido : partidosDisponibles) {
            // Si el partido no requiere nivel específico, es compatible
            if (partido.getNivelRequerido() == null) {
                partidosCompatibles.add(partido);
                continue;
            }
            
            // Verificar si el nivel del buscador es compatible con el requerido
            int nivelBuscador = buscador.getNivel().getNivel();
            int nivelRequerido = partido.getNivelRequerido().getNivel();
            
            // Permitir una diferencia de ±5 puntos
            if (Math.abs(nivelBuscador - nivelRequerido) <= 5) {
                partidosCompatibles.add(partido);
            }
        }
        
        System.out.println("Partidos encontrados por nivel: " + partidosCompatibles.size());
        return partidosCompatibles;
    }

    @Override
    public String getNombreEstrategia() {
        return "Emparejamiento por Nivel de Habilidad";
    }
}
