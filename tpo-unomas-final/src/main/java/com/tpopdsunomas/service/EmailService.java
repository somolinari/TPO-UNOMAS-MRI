package com.tpopdsunomas.service;

import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

/**
 * Servicio de Email usando Jakarta Mail (antes JavaMail)
 * Esta clase tiene una interfaz diferente que será adaptada por JavaMailSender
 */
public class EmailService {
    private String username;
    private String password;
    private Properties props;

    public EmailService(String username, String password) {
        this.username = username;
        this.password = password;

        props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
    }

    /**
     * Método con nombre diferente al de nuestra interfaz IMail
     * El adapter lo adaptará a enviarMail()
     */
    public void enviarCorreo(String destinatario, String asunto, String mensajeTexto) {
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message mensaje = new MimeMessage(session);
            mensaje.setFrom(new InternetAddress(username));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            mensaje.setSubject(asunto);
            mensaje.setText(mensajeTexto);

            Transport.send(mensaje);
            System.out.println("   ✓ Correo enviado exitosamente a " + destinatario);
        } catch (MessagingException e) {
            System.err.println("   ✗ Error al enviar correo: " + e.getMessage());
            throw new RuntimeException("Error al enviar email", e);
        }
    }
}
