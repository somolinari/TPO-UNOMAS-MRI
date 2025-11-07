package com.tpopdsunomas.patterns.adapter.geolocalizacion;

import com.tpopdsunomas.model.Ubicacion;

public class OpenStreetMapAdapter implements IGeolocalizacionAdapter {

   @Override
    public double calcularDistancia(Ubicacion origen, Ubicacion destino) throws Exception {
        
        // 1. Asegurarse de que el origen tenga coordenadas
        asegurarCoordenadas(origen);
        
        // 2. Asegurarse de que el destino tenga coordenadas
        asegurarCoordenadas(destino);

        // 3. ¡DELEGAR el cálculo a tu clase Ubicacion!
        // Ya no calculamos Haversine aquí.
        return origen.calcularDistancia(destino);
    }

    /**
     * Método helper privado que "oculta" la complejidad de Geolocation.
     * Si la ubicación no tiene lat/lon, llama a la API para obtenerlos.
     */
    private void asegurarCoordenadas(Ubicacion ubicacion) throws Exception {
        // Si la latitud es 0.0 (o no está seteada), asumimos que necesita geocodificación
        if (ubicacion.getLatitud() == 0.0 || ubicacion.getLongitud() == 0.0) {
            
            System.out.println("ADAPTADOR: Geocodificando dirección: " + ubicacion.getDireccionCompletaParaAPI());
            
            // 1. Llama a tu clase Geolocation (el Adaptee)
            double[] coords = Geolocation.geocodeAddress(ubicacion.getDireccionCompletaParaAPI());
            
            // 2. Actualiza el objeto Ubicacion con los datos obtenidos
            ubicacion.setLatitud(coords[0]);
            ubicacion.setLongitud(coords[1]);
        }
    }
}