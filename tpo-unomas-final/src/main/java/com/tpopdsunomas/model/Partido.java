package com.tpopdsunomas.model;

import com.tpopdsunomas.patterns.observer.IObserverNotificacion;
import com.tpopdsunomas.patterns.state.IEstadoPartido;
import com.tpopdsunomas.patterns.state.NecesitaJugadores;
import com.tpopdsunomas.patterns.strategy.INivelJugador;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase Partido
 * Implementa el patrón State para estados y Observer para notificaciones
 */
public class Partido {
    private int id;
    private int cantidadJugadores;
    private Deporte tipoDeporte;
    private Ubicacion ubicacion;
    private String codigoPostal;
    private int duracion; // Por ejemplo: "90 minutos"
    private boolean cuentaConCancha;
    private Cuenta dueno;
    private IEstadoPartido estado;
    private List<IObserverNotificacion> observers;
    private List<Estadistica> estadisticas;
    private List<Comentario> comentarios;
    private LocalDateTime fechaHora;
    private INivelJugador nivelRequerido;
    private List<Cuenta> jugadores;
    

    public Partido() {
        this.estado = new NecesitaJugadores();
        this.jugadores = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.estadisticas = new ArrayList<>();
        this.comentarios = new ArrayList<>();
        this.codigoPostal = "";
    }

    public Partido(int id, Deporte tipoDeporte, int cantidadJugadores, Ubicacion ubicacion,
                   int duracion, boolean cuentaConCancha, Cuenta dueno, LocalDateTime fechaHora,
                   INivelJugador nivelRequerido) {
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
        
        // 1. Establecer el estado ANTES de añadir jugadores
        this.estado = new NecesitaJugadores();
        
        // 2. Registrar al dueño como creador
        dueno.agregarPartidoCreado(this);

        // 3. // CORRECCIÓN //
        // Usar el método de estado para añadir al dueño como primer jugador.
        // Esto ejecutará la lógica de NecesitaJugadores.java
        // y comprobará si el partido se llena (ej: 1/1).
        this.estado.agregarJugador(this, dueno); 
    this.codigoPostal = (ubicacion != null && ubicacion.getCodigoPostal() != null)
        ? ubicacion.getCodigoPostal() : "";
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
    public Boolean agregarJugador(Cuenta jugador) {

        LocalDateTime fechaInicial = this.fechaHora;
        LocalDateTime fechaFinal = this.fechaHora.plusMinutes(this.getDuracion());
        Boolean disponible = validarDisponibilidad(fechaInicial,fechaFinal,jugador);

        if(disponible){
            this.estado.agregarJugador(this, jugador);            
        }        
        
        return disponible;
    }

    private boolean validarDisponibilidad(LocalDateTime jugadorFechaInicial, LocalDateTime jugadorFechaFinal, Cuenta jugador){
        
        List<Partido> partidosInscritos = jugador.getPartidosInscritos().stream()
            .filter(partido -> 
                !partido.getEstado().getNombre().equals("Cancelado") &&
                !partido.getEstado().getNombre().equals("Finalizado"))
            .collect(Collectors.toList());             
      
        List<Partido> partidosCreados = jugador.getPartidosCreados().stream()
             .filter(partido -> 
                !partido.getEstado().getNombre().equals("Cancelado") &&
                !partido.getEstado().getNombre().equals("Finalizado"))
            .collect(Collectors.toList());

        List<Partido> partidosTodos = new ArrayList<>();
        partidosTodos.addAll(partidosInscritos);
        partidosTodos.addAll(partidosCreados);

        for(Partido partido : partidosTodos){
            int partidoDuracion = partido.getDuracion();
            LocalDateTime partidoFechaInicial = partido.getFechaHora();
            LocalDateTime partidoFechaFinal =   partido.getFechaHora().plusMinutes(partidoDuracion);

            

            boolean existeConflicto = jugadorFechaInicial.isBefore(partidoFechaFinal) 
                && partidoFechaInicial.isBefore(jugadorFechaFinal);
            if(existeConflicto){
                return false;
            }
        }

       return true;
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
        this.codigoPostal = (ubicacion != null && ubicacion.getCodigoPostal() != null)
                ? ubicacion.getCodigoPostal() : "";
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
        if (this.ubicacion != null) {
            this.ubicacion.setCodigoPostal(codigoPostal);
        }
    }
    
    public int getDuracion() {
        return duracion;
    }
    
    public void setDuracion(int duracion) {
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
               (codigoPostal != null && !codigoPostal.isEmpty() ? ("\nC.P.: " + codigoPostal) : "") +
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