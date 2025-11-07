package com.tpopdsunomas.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Servicio de Geolocalización usando OpenStreetMap Nominatim API
 * Permite convertir direcciones en coordenadas (geocoding)
 */
public class Geolocation {
    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search";
    private static final OkHttpClient client = new OkHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Convierte una dirección en coordenadas (latitud, longitud)
     * @param address Dirección a buscar
     * @return Array con [latitud, longitud]
     * @throws IOException Si hay error en la petición
     */
    public static double[] geocodeAddress(String address) throws IOException {
        String url = String.format("%s?q=%s&format=json&limit=1", 
                                   NOMINATIM_URL, 
                                   address.replace(" ", "+"));
        
        Request request = new Request.Builder()
            .url(url)
            .addHeader("User-Agent", "UnoMasApp/1.0")
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error en la petición: " + response.code());
            }

            String responseBody = response.body().string();
            JsonNode results = mapper.readTree(responseBody);
            
            if (results.isArray() && results.size() > 0) {
                JsonNode firstResult = results.get(0);
                double lat = firstResult.get("lat").asDouble();
                double lon = firstResult.get("lon").asDouble();
                return new double[]{lat, lon};
            } else {
                throw new IOException("No se encontraron resultados para: " + address);
            }
        }
    }

    /**
     * Calcula la distancia en kilómetros entre dos puntos usando la fórmula de Haversine
     */
    public static double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radio de la Tierra en km
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c;
    }
}
