package com.tpopdsunomas.patterns.observer;

import com.tpopdsunomas.model.Cuenta;
import com.tpopdsunomas.model.Partido;
import com.tpopdsunomas.patterns.adapter.IMail;
import com.tpopdsunomas.patterns.adapter.JavaMailSender;
import com.tpopdsunomas.util.ConfigLoader;

/**
 * Patrón Observer - Observador concreto para notificaciones por email
 * Utiliza el Adapter JavaMailSender para enviar correos
 */
public class EmailNotificacion implements IObserverNotificacion {
    private IMail mailSender;

    public EmailNotificacion() {
        // Cargar configuración desde ConfigLoader
        String emailFrom = ConfigLoader.getProperty("email.username");
        String password = ConfigLoader.getProperty("email.password");
        
        if (emailFrom != null && password != null) {
            this.mailSender = new JavaMailSender(emailFrom, password);
        } else {
            System.err.println("⚠ Configuración de email no encontrada en config.properties");
        }
    }

    @Override
    public void actualizar(Partido partido) {
        if (mailSender == null) {
            System.out.println("⚠ No se puede enviar email: configuración no válida");
            return;
        }

        String estadoPartido = partido.getEstado().getNombre();
        System.out.println("\n📧 === ENVIANDO EMAILS === Estado: " + estadoPartido);
        
        // Enviar email a cada participante del partido
        for (Cuenta participante : partido.getParticipantes()) {
            String asunto = "Actualización de Partido #" + partido.getId() + " - " + estadoPartido;
            String mensaje = construirMensaje(participante, partido, estadoPartido);
            
            try {
                mailSender.enviarMail(participante.getEmail(), asunto, mensaje);
                System.out.println("✓ Email enviado a: " + participante.getEmail());
            } catch (Exception e) {
                System.err.println("✗ Error enviando email a " + participante.getEmail() + ": " + e.getMessage());
            }
        }
        System.out.println("=== FIN DE ENVÍO DE EMAILS ===\n");
    }

    private String construirMensaje(Cuenta participante, Partido partido, String estadoPartido) {
        StringBuilder sb = new StringBuilder();
        sb.append("Hola ").append(participante.getNombre()).append(",\n\n");
        sb.append("El partido #").append(partido.getId());
        sb.append(" de ").append(partido.getTipoDeporte().getNombre());
        sb.append(" ha cambiado a estado: ").append(estadoPartido).append("\n\n");
        
        sb.append("Detalles del partido:\n");
        sb.append("- Fecha: ").append(partido.getFechaHora()).append("\n");
        sb.append("- Ubicación: ").append(partido.getUbicacion() != null ? partido.getUbicacion() : "No especificada").append("\n");
        sb.append("- Jugadores confirmados: ").append(partido.getJugadores().size()).append("/").append(partido.getCantidadJugadores()).append("\n\n");
        
        switch (estadoPartido) {
            case "Necesita Jugadores":
                sb.append("El partido aún necesita más jugadores para completarse.\n");
                break;
            case "Partido Armado":
                sb.append("¡El partido está completo! Por favor confirma tu participación.\n");
                break;
            case "Confirmado":
                sb.append("¡Todos los jugadores han confirmado! El partido está listo.\n");
                break;
            case "En Juego":
                sb.append("¡El partido ha comenzado! ¡Buena suerte!\n");
                break;
            case "Finalizado":
                sb.append("El partido ha finalizado. ¡Esperamos que lo hayas disfrutado!\n");
                break;
            case "Cancelado":
                sb.append("Lamentablemente, el partido ha sido cancelado.\n");
                break;
        }
        
        sb.append("\nSaludos,\nEquipo Uno Mas");
        return sb.toString();
    }
}
