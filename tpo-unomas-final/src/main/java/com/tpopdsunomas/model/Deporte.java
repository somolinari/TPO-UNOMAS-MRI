package com.tpopdsunomas.model;

/**
 * Clase abstracta Deporte
 * Cada deporte específico hereda de esta clase
 * NO se usa enum para cumplir con los requisitos del TPO
 */
public abstract class Deporte {
    private int id;
    private String nombre;
    private int cantidadJugadoresPorDefecto;

    public Deporte(int id, String nombre, int cantidadJugadoresPorDefecto) {
        this.id = id;
        this.nombre = nombre;
        this.cantidadJugadoresPorDefecto = cantidadJugadoresPorDefecto;
    }

    // Método abstracto que cada deporte puede implementar con sus reglas específicas
    public abstract String getDescripcion();
    
    // Método abstracto para obtener reglas específicas del deporte
    public abstract String getReglas();

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidadJugadoresPorDefecto() {
        return cantidadJugadoresPorDefecto;
    }

    public void setCantidadJugadoresPorDefecto(int cantidadJugadoresPorDefecto) {
        this.cantidadJugadoresPorDefecto = cantidadJugadoresPorDefecto;
    }

    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Deporte deporte = (Deporte) obj;
        return id == deporte.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
