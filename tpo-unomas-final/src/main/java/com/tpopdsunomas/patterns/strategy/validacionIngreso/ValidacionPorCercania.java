package com.tpopdsunomas.patterns.strategy.validacionIngreso;

import com.tpopdsunomas.model.Cuenta;
import com.tpopdsunomas.model.Partido;
import com.tpopdsunomas.model.Ubicacion;
import com.tpopdsunomas.patterns.adapter.geolocalizacion.OpenStreetMapAdapter;
import com.tpopdsunomas.patterns.adapter.geolocalizacion.IGeolocalizacionAdapter;

public class ValidacionPorCercania implements IStrategyValidacionIngreso{
    private double radioMaximoKm;
    private IGeolocalizacionAdapter geoAdapter;

    public ValidacionPorCercania(double radioMaximoKm) {
        this.radioMaximoKm = radioMaximoKm;
        // Instanciamos el adaptador que calcula la distancia
        this.geoAdapter = new OpenStreetMapAdapter(); 
    }

    @Override
    public boolean esCompatible(Cuenta jugador, Partido partido) {
        
        // 1. Obtener las ubicaciones
        Ubicacion ubicacionPartido = partido.getUbicacion();
        Ubicacion ubicacionJugador = jugador.getUbicacion();

        if (ubicacionPartido == null || ubicacionJugador == null) {
            System.out.println("Validacion: No se puede validar por cercanía, faltan datos de ubicación.");

            return true; 
        }

        try {
            double distancia = geoAdapter.calcularDistancia(ubicacionJugador, ubicacionPartido);
            
            boolean esCompatible = (distancia <= this.radioMaximoKm);
            
            if (!esCompatible) {
                System.out.println("Validacion: Jugador a " + String.format("%.2f", distancia) + 
                                   " km. (Límite: " + radioMaximoKm + " km)");
            }
            return esCompatible;
            
        } catch (Exception e) {
            System.err.println("Error al calcular distancia en validación: " + e.getMessage());
            return false; 
        }
    }

    @Override
    public String getNombreRegla() {
        return "Sólo jugadores a " + radioMaximoKm + " km a la redonda";
    }
}
