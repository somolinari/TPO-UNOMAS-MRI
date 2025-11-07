package com.tpopdsunomas.patterns.adapter;

/**
 * Patrón Adapter - Interfaz target para el envío de emails
 * Define el método que el sistema espera usar
 */
public interface IMail {
    void enviarMail(String correoDestinatario, String asunto, String mensaje);
}
