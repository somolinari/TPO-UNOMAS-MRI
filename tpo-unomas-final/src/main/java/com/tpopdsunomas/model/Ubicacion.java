package com.tpopdsunomas.model;

/**
 * Clase Ubicacion
 * Representa una ubicación geográfica con latitud, longitud y dirección
 */
public class Ubicacion {
    private String ciudad;
    private String direccion;
    private double latitud;
    private double longitud;
    private String codigoPostal;

    public Ubicacion(String ciudad, String direccion) {
        this.ciudad = ciudad;
        this.direccion = direccion;
        this.latitud = 0.0;
        this.longitud = 0.0;
        this.codigoPostal = "";
    }

    public Ubicacion(String ciudad, String direccion, double latitud, double longitud) {
        this.ciudad = ciudad;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.codigoPostal = "";
    }

    public Ubicacion(String ciudad, String direccion, double latitud, double longitud, String codigoPostal) {
        this.ciudad = ciudad;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.codigoPostal = codigoPostal;
    }

    /**
     * Calcula la distancia en kilómetros entre esta ubicación y otra
     * Usa la fórmula de Haversine
     */
    public double calcularDistancia(Ubicacion otra) {
        final int R = 6371; // Radio de la Tierra en km
        
        double latDistance = Math.toRadians(otra.latitud - this.latitud);
        double lonDistance = Math.toRadians(otra.longitud - this.longitud);
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(this.latitud)) * Math.cos(Math.toRadians(otra.latitud))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c;
    }

    public String getDireccionCompletaParaAPI() {
    String direccionCompleta = this.direccion + ", " + this.ciudad;
    if (this.codigoPostal != null && !this.codigoPostal.isEmpty()) {
        direccionCompleta += ", " + this.codigoPostal;
    }
    // Opcional: puedes agregar ", Argentina" para mejorar la precisión
    return direccionCompleta + ", Argentina";
}

    // Getters y Setters
    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    @Override
    public String toString() {
        return direccion + ", " + ciudad + 
               (codigoPostal.isEmpty() ? "" : " (" + codigoPostal + ")");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Ubicacion ubicacion = (Ubicacion) obj;
        return Double.compare(ubicacion.latitud, latitud) == 0 &&
               Double.compare(ubicacion.longitud, longitud) == 0;
    }

    @Override
    public int hashCode() {
        int result = Double.hashCode(latitud);
        result = 31 * result + Double.hashCode(longitud);
        return result;
    }
}
