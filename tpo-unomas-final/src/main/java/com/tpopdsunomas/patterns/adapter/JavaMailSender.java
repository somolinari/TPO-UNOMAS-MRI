package com.tpopdsunomas.patterns.adapter;

import com.tpopdsunomas.service.EmailService;

/**
 * Patrón Adapter - Adaptador para EmailService
 * Adapta la interfaz de EmailService (enviarCorreo) a nuestra interfaz IMail (enviarMail)
 */
public class JavaMailSender implements IMail {
    private EmailService emailService;

    public JavaMailSender(String username, String password) {
        this.emailService = new EmailService(username, password);
    }

    @Override
    public void enviarMail(String correoDestinatario, String asunto, String mensaje) {
        // Adaptamos el método enviarCorreo() de EmailService a nuestro método enviarMail()
        this.emailService.enviarCorreo(correoDestinatario, asunto, mensaje);
    }
}
