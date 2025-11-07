package com.tpopdsunomas.patterns.strategy;

import com.tpopdsunomas.model.Cuenta;
import com.tpopdsunomas.model.Partido;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Patrón Strategy - Emparejamiento por historial de partidos previos
 * Busca partidos donde hayan jugado compañeros conocidos
 */
public class EmparejamientoPorHistorial implements IStrategyEmparejamiento {

    @Override
    public List<Partido> buscar(Cuenta buscador, List<Partido> partidosDisponibles) {
        List<Partido> partidosConConocidos = new ArrayList<>();
        
        System.out.println("\n🔍 Aplicando estrategia: Emparejamiento por Historial");
        
        // Obtener conjunto de jugadores con los que ya ha jugado
        Set<Cuenta> jugadoresConocidos = obtenerJugadoresConocidos(buscador);
        System.out.println("Jugadores conocidos: " + jugadoresConocidos.size());
        
        // Buscar partidos donde haya al menos un jugador conocido
        for (Partido partido : partidosDisponibles) {
            boolean tieneConocidos = false;
            
            for (Cuenta jugador : partido.getJugadores()) {
                if (jugadoresConocidos.contains(jugador)) {
                    tieneConocidos = true;
                    break;
                }
            }
            
            if (tieneConocidos) {
                partidosConConocidos.add(partido);
                System.out.println("  Partido #" + partido.getId() + " - Tiene jugadores conocidos");
            }
        }
        
        System.out.println("Partidos encontrados con conocidos: " + partidosConConocidos.size());
        return partidosConConocidos;
    }

    @Override
    public String getNombreEstrategia() {
        return "Emparejamiento por Historial de Partidos";
    }

    /**
     * Obtiene el conjunto de jugadores con los que el buscador ya ha jugado
     */
    private Set<Cuenta> obtenerJugadoresConocidos(Cuenta buscador) {
        Set<Cuenta> conocidos = new HashSet<>();
        
        // Agregar jugadores de partidos donde el buscador estuvo inscrito
        for (Partido partido : buscador.getPartidosInscritos()) {
            for (Cuenta jugador : partido.getJugadores()) {
                if (!jugador.equals(buscador)) {
                    conocidos.add(jugador);
                }
            }
        }
        
        // Agregar jugadores de partidos que el buscador creó
        for (Partido partido : buscador.getPartidosCreados()) {
            for (Cuenta jugador : partido.getJugadores()) {
                if (!jugador.equals(buscador)) {
                    conocidos.add(jugador);
                }
            }
        }
        
        return conocidos;
    }
}
