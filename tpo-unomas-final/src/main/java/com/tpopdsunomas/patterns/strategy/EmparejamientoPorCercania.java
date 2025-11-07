package com.tpopdsunomas.patterns.strategy;

import com.tpopdsunomas.model.Cuenta;
import com.tpopdsunomas.model.Partido;
import com.tpopdsunomas.model.Ubicacion;
import com.tpopdsunomas.patterns.adapter.OpenStreetMapAdapter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Patrón Strategy - Emparejamiento por cercanía geográfica
 * Usa el adapter de OpenStreetMap para calcular distancias
 */
public class EmparejamientoPorCercania implements IStrategyEmparejamiento {
    private OpenStreetMapAdapter geoAdapter;
    private double radioMaximoKm;

    public EmparejamientoPorCercania(double radioMaximoKm) {
        this.geoAdapter = new OpenStreetMapAdapter();
        this.radioMaximoKm = radioMaximoKm;
    }

    @Override
    public List<Partido> buscar(Cuenta buscador, List<Partido> partidosDisponibles) {
        List<Partido> partidosCercanos = new ArrayList<>();
        
        System.out.println("\n🔍 Aplicando estrategia: Emparejamiento por Cercanía");
        System.out.println("Radio máximo: " + radioMaximoKm + " km");
        
        // Verificar que el buscador tenga ubicación
        if (buscador.getUbicaciones().isEmpty()) {
            System.out.println("⚠ El buscador no tiene ubicaciones registradas");
            return partidosCercanos;
        }
        
        Ubicacion ubicacionBuscador = buscador.getUbicaciones().get(0);
        
        // Filtrar partidos por distancia
        for (Partido partido : partidosDisponibles) {
            if (partido.getUbicacion() == null) {
                continue;
            }
            
            try {
                double distancia = geoAdapter.calcularDistancia(
                    ubicacionBuscador, 
                    partido.getUbicacion()
                );
                
                if (distancia <= radioMaximoKm) {
                    partidosCercanos.add(partido);
                    System.out.println("  Partido #" + partido.getId() + " - " + 
                                     String.format("%.2f", distancia) + " km");
                }
            } catch (Exception e) {
                System.err.println("  Error calculando distancia para partido #" + partido.getId());
            }
        }
        
        // Ordenar por distancia (más cercano primero)
        partidosCercanos.sort(Comparator.comparingDouble(p -> {
            try {
                return geoAdapter.calcularDistancia(ubicacionBuscador, p.getUbicacion());
            } catch (Exception e) {
                return Double.MAX_VALUE;
            }
        }));
        
        System.out.println("Partidos encontrados por cercanía: " + partidosCercanos.size());
        return partidosCercanos;
    }

    @Override
    public String getNombreEstrategia() {
        return "Emparejamiento por Cercanía (" + radioMaximoKm + " km)";
    }
}
