package com.tpopdsunomas.model;

import com.tpopdsunomas.patterns.observer.IObserverNotificacion;
import com.tpopdsunomas.patterns.state.IEstadoPartido;
import com.tpopdsunomas.patterns.state.NecesitaJugadores;
import com.tpopdsunomas.patterns.strategy.INivelJugador;
import com.tpopdsunomas.patterns.strategy.validacionIngreso.IStrategyValidacionIngreso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase Partido
 * Implementa el patrón State para estados y Observer para notificaciones
 */
public class Partido {
    private int id;
    private int cantidadJugadores;
    private Deporte tipoDeporte;
    private Ubicacion ubicacion;
    private String duracion; // Por ejemplo: "90 minutos"
    private boolean cuentaConCancha;
    private Cuenta dueno;
    private IEstadoPartido estado;
    private List<IObserverNotificacion> observers;
    private List<Estadistica> estadisticas;
    private List<Comentario> comentarios;
    private LocalDateTime fechaHora;
    private INivelJugador nivelRequerido;
    private List<Cuenta> jugadores;
    private IStrategyValidacionIngreso estrategiaValidacion;

    public Partido() {
        this.estado = new NecesitaJugadores();
        this.jugadores = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.estadisticas = new ArrayList<>();
        this.comentarios = new ArrayList<>();
    }

    public Partido(int id, Deporte tipoDeporte, int cantidadJugadores, Ubicacion ubicacion,
                   String duracion, boolean cuentaConCancha, Cuenta dueno, LocalDateTime fechaHora,
                   INivelJugador nivelRequerido, IStrategyValidacionIngreso estrategiaValidacion) {
        this.id = id;
        this.tipoDeporte = tipoDeporte;
        this.cantidadJugadores = cantidadJugadores;
        this.ubicacion = ubicacion;
        this.duracion = duracion;
        this.cuentaConCancha = cuentaConCancha;
        this.dueno = dueno;
        this.fechaHora = fechaHora;
        this.nivelRequerido = nivelRequerido;
        this.jugadores = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.estadisticas = new ArrayList<>();
        this.comentarios = new ArrayList<>();
        this.estado = new NecesitaJugadores();
        this.estrategiaValidacion = estrategiaValidacion;
        
        // El dueño se agrega automáticamente como jugador
        this.jugadores.add(dueno);
        dueno.agregarPartidoCreado(this);
    }

    // Métodos del patrón Observer
    public void agregarObservador(IObserverNotificacion observador) {
        if (!observers.contains(observador)) {
            observers.add(observador);
            System.out.println("(Observador agregado: " + observador.getClass().getSimpleName() + ")");
        }
    }

    public void eliminarObservador(IObserverNotificacion observador) {
        observers.remove(observador);
        System.out.println("(Observador eliminado: " + observador.getClass().getSimpleName() + ")");
    }

    public void notificarObservadores() {
        System.out.println("=== Notificando a " + observers.size() + " observador(es) ===");
        for (IObserverNotificacion obs : observers) {
            obs.actualizar(this);
        }
    }

    public List<Cuenta> getParticipantes() {
        return this.jugadores;
    }

    // Métodos de gestión de estado (delegados al patrón State)
    public void agregarJugador(Cuenta jugador) {
        this.estado.agregarJugador(this, jugador);
    }

    public IStrategyValidacionIngreso getEstrategiaValidacion() {
        return estrategiaValidacion;
    }

    public void setEstrategiaValidacion(IStrategyValidacionIngreso estrategiaValidacion) {
        this.estrategiaValidacion = estrategiaValidacion;
    }
    
    public void confirmar() {
        this.estado.confirmar(this);
    }

    public void iniciarJuego() {
        this.estado.iniciarJuego(this);
    }

    public void finalizarPartido() {
        this.estado.finalizar(this);
    }
    
    public void cancelarPartido() {
        this.estado.cancelar(this);
    }
    
    // Métodos auxiliares
    public boolean estaCompleto() {
        return jugadores.size() >= cantidadJugadores;
    }

    public void agregarComentario(Comentario comentario) {
        this.comentarios.add(comentario);
    }

    public void agregarEstadistica(Estadistica estadistica) {
        this.estadisticas.add(estadistica);
    }
    
    // Getters y Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public Deporte getTipoDeporte() {
        return tipoDeporte;
    }
    
    public void setTipoDeporte(Deporte tipoDeporte) {
        this.tipoDeporte = tipoDeporte;
    }
    
    public int getCantidadJugadores() {
        return cantidadJugadores;
    }
    
    public void setCantidadJugadores(int cantidadJugadores) {
        this.cantidadJugadores = cantidadJugadores;
    }
    
    public Ubicacion getUbicacion() {
        return ubicacion;
    }
    
    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }
    
    public String getDuracion() {
        return duracion;
    }
    
    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }
    
    public boolean isCuentaConCancha() {
        return cuentaConCancha;
    }
    
    public void setCuentaConCancha(boolean cuentaConCancha) {
        this.cuentaConCancha = cuentaConCancha;
    }
    
    public Cuenta getDueno() {
        return dueno;
    }
    
    public void setDueno(Cuenta dueno) {
        this.dueno = dueno;
    }
    
    public List<Cuenta> getJugadores() {
        return jugadores;
    }
    
    public IEstadoPartido getEstado() {
        return estado;
    }
    
    public void setEstado(IEstadoPartido nuevoEstado) {
        this.estado = nuevoEstado;
        System.out.println("--- Partido #" + id + " cambió a estado: " + nuevoEstado.getNombre() + " ---");
        this.notificarObservadores(); 
    }
    
    public List<Estadistica> getEstadisticas() {
        return estadisticas;
    }

    public List<Comentario> getComentarios() {
        return comentarios;
    }
    
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }
    
    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
    
    public INivelJugador getNivelRequerido() {
        return nivelRequerido;
    }
    
    public void setNivelRequerido(INivelJugador nivelRequerido) {
        this.nivelRequerido = nivelRequerido;
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "Partido #" + id + " - " + tipoDeporte.getNombre() + 
               "\nUbicación: " + (ubicacion != null ? ubicacion : "No especificada") +
               "\nFecha: " + fechaHora.format(formatter) +
               "\nJugadores: " + jugadores.size() + "/" + cantidadJugadores +
               "\nEstado: " + estado.getNombre() +
               (nivelRequerido != null ? "\nNivel requerido: " + nivelRequerido.getNombre() : "");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Partido partido = (Partido) obj;
        return id == partido.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
