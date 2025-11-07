package com.tpopdsunomas.patterns.state;

import com.tpopdsunomas.model.Cuenta;
import com.tpopdsunomas.model.Partido;
import com.tpopdsunomas.patterns.strategy.validacionIngreso.IStrategyValidacionIngreso;
import com.tpopdsunomas.patterns.strategy.validacionIngreso.ValidacionEstrictaPorNivel;

/**
 * Patrón State - Estado inicial del partido
 * El partido necesita jugadores para completarse
 */
public class NecesitaJugadores implements IEstadoPartido {

    @Override
    public String getNombre() {
        return "Necesita Jugadores";
    }

    @Override
    public void agregarJugador(Partido partido, Cuenta jugador) {
        
        // VALIDACIÓN 1: ¿YA ESTÁ INSCRITO?
        if (partido.getJugadores().contains(jugador)) {
            throw new IllegalStateException("El jugador " + jugador.getNombre() + " ya está en el partido.");
        }

        // VALIDACIÓN 2: ¿HAY CUPO?
        if (partido.getJugadores().size() >= partido.getCantidadJugadores()) {
            throw new IllegalStateException("El partido ya está lleno. No se pueden agregar más jugadores.");
        }

        // VALIDACIÓN 3: ¿ES COMPATIBLE? (Patrón Strategy de Validación)
        // Obtenemos la estrategia de validación que tiene el partido (ej: ValidacionPorCercania)
        IStrategyValidacionIngreso estrategia = partido.getEstrategiaValidacion();
        
        if (!estrategia.esCompatible(jugador, partido)) {
            // Preparamos un mensaje de error claro
            String msg = "El jugador '" + jugador.getNombre() + " no cumple con la regla del partido: " + 
                         estrategia.getNombreRegla() + "'";
            
            // Añadimos contexto (Nivel o Ubicación) si la regla falló
            if (estrategia instanceof ValidacionEstrictaPorNivel && partido.getNivelRequerido() != null) {
                msg += " (Nivel del jugador: " + jugador.getNivel().getNombre() + 
                       ", Nivel requerido: " + partido.getNivelRequerido().getNombre() + ")";
            }
            
            // (Puedes añadir un 'instanceof' similar para ValidacionPorCercania si quieres mostrar los KM)
            
            throw new IllegalStateException(msg);
        }

        // --- SI PASÓ TODAS LAS VALIDACIONES, LO AGREGAMOS ---
        
        partido.getJugadores().add(jugador);
        jugador.agregarPartidoInscrito(partido);
        System.out.println("✓ " + jugador.getNombre() + " se unió al partido");
        
        // 4. ¿SE LLENÓ? (Transición de estado)
        if (partido.getJugadores().size() >= partido.getCantidadJugadores()) {
            System.out.println("¡Partido completo! Transicionando a 'Armado'");
            partido.setEstado(new Armado());
        } else {
            System.out.println("Jugadores: " + partido.getJugadores().size() + 
                               "/" + partido.getCantidadJugadores());
        }
    }

    @Override
    public void confirmar(Partido partido) {
        System.out.println("⚠ No se puede confirmar: el partido aún necesita jugadores");
    }

    @Override
    public void iniciarJuego(Partido partido) {
        System.out.println("⚠ No se puede iniciar: el partido aún necesita jugadores");
    }

    @Override
    public void finalizar(Partido partido) {
        System.out.println("⚠ No se puede finalizar: el partido aún no ha comenzado");
    }

    @Override
    public void cancelar(Partido partido) {
        System.out.println("✓ Cancelando partido...");
        partido.setEstado(new Cancelado());
    }
}
