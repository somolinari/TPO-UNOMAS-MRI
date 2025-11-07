package com.tpopdsunomas.model;

/**
 * Clase Estadistica
 * Representa estadísticas de un jugador en un partido finalizado
 */
public class Estadistica {
    private int id;
    private Cuenta cuenta;
    private Partido partido;
    private int puntos;
    private int asistencias;
    private int faltas;
    private String mvp; // "Sí" o "No" - no usamos boolean para evitar confusión
    private String descripcion;

    public Estadistica(int id, Cuenta cuenta, Partido partido) {
        this.id = id;
        this.cuenta = cuenta;
        this.partido = partido;
        this.puntos = 0;
        this.asistencias = 0;
        this.faltas = 0;
        this.mvp = "No";
        this.descripcion = "";
    }

    public Estadistica(int id, Cuenta cuenta, Partido partido, int puntos, int asistencias, int faltas) {
        this.id = id;
        this.cuenta = cuenta;
        this.partido = partido;
        this.puntos = puntos;
        this.asistencias = asistencias;
        this.faltas = faltas;
        this.mvp = "No";
        this.descripcion = "";
    }

    /**
     * Calcula una puntuación general basada en las estadísticas
     */
    public double calcularPuntuacionGeneral() {
        double puntuacion = (puntos * 2.0) + (asistencias * 1.5) - (faltas * 0.5);
        return Math.max(0, puntuacion); // No puede ser negativa
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    public Partido getPartido() {
        return partido;
    }

    public void setPartido(Partido partido) {
        this.partido = partido;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public int getAsistencias() {
        return asistencias;
    }

    public void setAsistencias(int asistencias) {
        this.asistencias = asistencias;
    }

    public int getFaltas() {
        return faltas;
    }

    public void setFaltas(int faltas) {
        this.faltas = faltas;
    }

    public String getMvp() {
        return mvp;
    }

    public void setMvp(String mvp) {
        this.mvp = mvp;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return cuenta.getNombre() + " - Pts: " + puntos + 
               ", Asist: " + asistencias + ", Faltas: " + faltas +
               (mvp.equals("Sí") ? " [MVP]" : "");
    }
}
