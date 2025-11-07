package com.tpopdsunomas.patterns.adapter;

import com.tpopdsunomas.service.Geolocation;
import com.tpopdsunomas.model.Ubicacion;
import java.io.IOException;

/**
 * Patrón Adapter - Adaptador para el servicio de Geolocalización de OpenStreetMap
 * Adapta la API de OpenStreetMap para calcular distancias entre ubicaciones
 */
public class OpenStreetMapAdapter {
    
    /**
     * Calcula la distancia en kilómetros entre dos ubicaciones
     * Usa el servicio Geolocation que consume la API de OpenStreetMap
     */
    public double calcularDistancia(Ubicacion origen, Ubicacion destino) {
        // Si las ubicaciones ya tienen coordenadas, usarlas directamente
        if (origen.getLatitud() != 0.0 && destino.getLatitud() != 0.0) {
            return Geolocation.calcularDistancia(
                origen.getLatitud(), origen.getLongitud(),
                destino.getLatitud(), destino.getLongitud()
            );
        }
        
        // Si no tienen coordenadas, intentar obtenerlas mediante geocoding
        try {
            double[] coordsOrigen = Geolocation.geocodeAddress(
                origen.getDireccion() + ", " + origen.getCiudad()
            );
            double[] coordsDestino = Geolocation.geocodeAddress(
                destino.getDireccion() + ", " + destino.getCiudad()
            );
            
            // Actualizar las coordenadas en las ubicaciones
            origen.setLatitud(coordsOrigen[0]);
            origen.setLongitud(coordsOrigen[1]);
            destino.setLatitud(coordsDestino[0]);
            destino.setLongitud(coordsDestino[1]);
            
            return Geolocation.calcularDistancia(
                coordsOrigen[0], coordsOrigen[1],
                coordsDestino[0], coordsDestino[1]
            );
            
        } catch (IOException e) {
            System.err.println("⚠ Error al calcular distancia: " + e.getMessage());
            // En caso de error, retornar una distancia arbitraria grande
            return 999999.0;
        }
    }

    /**
     * Obtiene las coordenadas de una dirección
     */
    public double[] obtenerCoordenadas(String direccion) throws IOException {
        return Geolocation.geocodeAddress(direccion);
    }
}
