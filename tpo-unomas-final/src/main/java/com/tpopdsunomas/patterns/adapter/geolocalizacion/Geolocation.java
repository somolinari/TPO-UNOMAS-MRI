package com.tpopdsunomas.patterns.adapter.geolocalizacion;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Utility class for geocoding and reverse geocoding using OpenStreetMap's Nominatim API.
 */
public class Geolocation {
    private static final OkHttpClient client = new OkHttpClient();

    /**
     * Converts a postal code or address into latitude and longitude coordinates.
     *
     * @param address postal code or street address
     * @return an array {latitude, longitude}
     * @throws Exception if an error occurs during API call or parsing
     */
    public static double[] geocodeAddress(String address) throws Exception {
        String url = "https://nominatim.openstreetmap.org/search?q="
                + java.net.URLEncoder.encode(address, "UTF-8")
                + "&format=json&limit=1";

        Request request = new Request.Builder()
                .url(url)
                // ⚠️ Nominatim requires a valid identifying User-Agent
                .header("User-Agent", "JavaGeolocationApp/1.0 (your_email@example.com)")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Geocoding failed: " + response.code() + " - " + response.message());
            }

            String body = response.body().string();

            // --- Check if the response is HTML instead of JSON ---
            if (body.trim().startsWith("<")) {
                throw new RuntimeException("Geocoding API returned HTML (possible rate limit or invalid request). Body:\n" + body);
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(body);

            if (root.isEmpty()) {
                throw new RuntimeException("No results found for address: " + address);
            }

            double lat = root.get(0).path("lat").asDouble();
            double lon = root.get(0).path("lon").asDouble();
            return new double[]{lat, lon};
        }
    }

    /**
     * Reverse geocodes a latitude and longitude into a human-readable address.
     *
     * @param lat latitude
     * @param lon longitude
     * @return the address as a string, or "Unknown address" if not found
     * @throws Exception if a network or API error occurs
     */
    public static String reverseGeocode(double lat, double lon) throws Exception {
        String url = String.format("https://nominatim.openstreetmap.org/reverse?lat=%f&lon=%f&format=json", lat, lon);

        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", "JavaGeolocationApp/1.0 (your_email@example.com)")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Reverse geocoding failed: " + response.code() + " - " + response.message());
            }

            String body = response.body().string();

            // --- Check for HTML responses (rate limit, etc.) ---
            if (body.trim().startsWith("<")) {
                throw new RuntimeException("Reverse geocoding API returned HTML. Body:\n" + body);
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(body);
            return root.path("display_name").asText("Unknown address");
        }
    }
}
