package com.tpopdsunomas.patterns.state;

import com.tpopdsunomas.model.Cuenta;
import com.tpopdsunomas.model.Partido;
import com.tpopdsunomas.patterns.strategy.validacionIngreso.IStrategyValidacionIngreso;

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
        if (partido.getJugadores().contains(jugador)) {
            throw new IllegalStateException("El jugador " + jugador.getNombre() + " ya está en el partido.");
        }

        if (partido.getJugadores().size() >= partido.getCantidadJugadores()) {
            throw new IllegalStateException("El partido ya está lleno. No se pueden agregar más jugadores.");
        }

       IStrategyValidacionIngreso estrategia = partido.getEstrategiaValidacion();
        
        if (!estrategia.esCompatible(jugador, partido)) {
            String msg = "El jugador '" + jugador.getNombre() + 
                         "' (Nivel: " + jugador.getNivel().getNombre() + ") " +
                         "no cumple con la regla del partido: '" + estrategia.getNombreRegla() + "'";
            
            if (partido.getNivelRequerido() != null) {
                msg += " (Nivel requerido: " + partido.getNivelRequerido().getNombre() + ")";
            }
            throw new IllegalStateException(msg);
        }

        
        partido.getJugadores().add(jugador);
        jugador.agregarPartidoInscrito(partido);
        System.out.println("✓ " + jugador.getNombre() + " se unió al partido");
        
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
