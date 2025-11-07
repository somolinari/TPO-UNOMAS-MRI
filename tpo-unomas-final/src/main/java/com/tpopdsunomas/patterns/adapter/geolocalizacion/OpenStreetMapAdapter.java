package com.tpopdsunomas.patterns.adapter.geolocalizacion;

import com.tpopdsunomas.model.Ubicacion;

public class OpenStreetMapAdapter implements IGeolocalizacionAdapter {

    @Override
    public double calcularDistancia(Ubicacion origen, Ubicacion destino) throws Exception {
        
        // 1. Obtener coordenadas de la Ubicacion de origen
        double[] coordsOrigen = obtenerCoordenadas(origen);
        
        // 2. Obtener coordenadas de la Ubicacion de destino
        double[] coordsDestino = obtenerCoordenadas(destino);

        // 3. Calcular la distancia (la parte que te faltaba)
        return calcularDistanciaHaversine(coordsOrigen[0], coordsOrigen[1], coordsDestino[0], coordsDestino[1]);
    }

    /**
     * Método privado que usa tu clase Geolocation para obtener lat/lon.
     * Esto "oculta" la complejidad.
     */
    private double[] obtenerCoordenadas(Ubicacion u) throws Exception {
        // Si la ubicación ya tiene lat/lon, los usamos.
        if (u.getLatitud() != 0 && u.getLongitud() != 0) {
            return new double[]{u.getLatitud(), u.getLongitud()};
        }
        
        // Si no, usamos tu clase Geolocation para buscarlos
        // (Asumiendo que Ubicacion tiene getDireccionCompleta())
        double[] coords = Geolocation.geocodeAddress(u.getDireccionCompleta());
        
        // Opcional: Guardar las coordenadas en la ubicación para no buscarlas de nuevo
        u.setLatitud(coords[0]);
        u.setLongitud(coords[1]);
        
        return coords;
    }

    /**
     * FÓRMULA DE HAVERSINE
     * Calcula la distancia en KM entre dos puntos (lat/lon) en la Tierra.
     */
    private double calcularDistanciaHaversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radio de la Tierra en kilómetros

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                 + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                 * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c; // Distancia en KM
    }
}