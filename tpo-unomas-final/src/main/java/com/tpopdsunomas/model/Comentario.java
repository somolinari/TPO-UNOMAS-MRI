package com.tpopdsunomas.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase Comentario
 * Representa un comentario hecho por un usuario en un partido
 */
public class Comentario {
    private int id;
    private Cuenta cuenta;
    private Partido partido;
    private String texto;
    private LocalDateTime fechaHora;

    public Comentario(int id, Cuenta cuenta, Partido partido, String texto) {
        this.id = id;
        this.cuenta = cuenta;
        this.partido = partido;
        this.texto = texto;
        this.fechaHora = LocalDateTime.now();
    }

    public Comentario(int id, Cuenta cuenta, Partido partido, String texto, LocalDateTime fechaHora) {
        this.id = id;
        this.cuenta = cuenta;
        this.partido = partido;
        this.texto = texto;
        this.fechaHora = fechaHora;
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

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "[" + fechaHora.format(formatter) + "] " + 
               cuenta.getNombre() + ": " + texto;
    }
}
