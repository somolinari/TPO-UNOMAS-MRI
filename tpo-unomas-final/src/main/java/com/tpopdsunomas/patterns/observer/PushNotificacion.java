package com.tpopdsunomas.patterns.observer;

import com.tpopdsunomas.model.Cuenta;
import com.tpopdsunomas.model.Partido;

/**
 * Patrón Observer - Observador concreto para notificaciones Push
 * SIMULADO: Solo imprime en consola (Firebase requeriría configuración compleja)
 */
public class PushNotificacion implements IObserverNotificacion {

    @Override
    public void actualizar(Partido partido) {
        String estadoPartido = partido.getEstado().getNombre();
        System.out.println("\n📱 === ENVIANDO NOTIFICACIONES PUSH (Simulado) === Estado: " + estadoPartido);
        
        // Simular envío de notificación push a cada participante
        for (Cuenta participante : partido.getParticipantes()) {
            String titulo = "Partido #" + partido.getId() + " - " + partido.getTipoDeporte().getNombre();
            String mensaje = construirMensajePush(partido, estadoPartido);
            
            System.out.println("📲 PUSH a " + participante.getNombre() + ":");
            System.out.println("   Título: " + titulo);
            System.out.println("   Mensaje: " + mensaje);
            System.out.println("   ✓ Notificación push simulada enviada");
        }
        System.out.println("=== FIN DE NOTIFICACIONES PUSH ===\n");
    }

    private String construirMensajePush(Partido partido, String estadoPartido) {
        switch (estadoPartido) {
            case "Necesita Jugadores":
                return "El partido aún necesita jugadores (" + partido.getJugadores().size() + "/" + partido.getCantidadJugadores() + ")";
            case "Partido Armado":
                return "¡Partido completo! Confirma tu participación";
            case "Confirmado":
                return "¡Todos confirmaron! El partido está listo";
            case "En Juego":
                return "¡El partido ha comenzado! ¡Buena suerte!";
            case "Finalizado":
                return "Partido finalizado. ¡Gracias por jugar!";
            case "Cancelado":
                return "El partido ha sido cancelado";
            default:
                return "Actualización del partido";
        }
    }
}
