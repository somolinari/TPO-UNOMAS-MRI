package com.tpopdsunomas.service;

import com.tpopdsunomas.model.Cuenta;
import com.tpopdsunomas.model.Deporte;
import com.tpopdsunomas.model.Partido;
import com.tpopdsunomas.model.Ubicacion;
import com.tpopdsunomas.patterns.observer.EmailNotificacion;
import com.tpopdsunomas.patterns.observer.PushNotificacion;
import com.tpopdsunomas.patterns.repo.ICuentaRepository;
import com.tpopdsunomas.patterns.repo.IPartidoRepository;
import com.tpopdsunomas.patterns.strategy.IStrategyEmparejamiento;
import com.tpopdsunomas.patterns.strategy.INivelJugador;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio de Partidos - Capa de lógica de negocio (Controlador en MVC)
 * Maneja la lógica de partidos, estados, y notificaciones
 */
public class PartidoService {
    private IPartidoRepository partidoRepo;
    private ICuentaRepository cuentaRepo;

    public PartidoService(IPartidoRepository partidoRepo, ICuentaRepository cuentaRepo) {
        this.partidoRepo = partidoRepo;
        this.cuentaRepo = cuentaRepo;
    }

    /**
     * Crea un nuevo partido y registra observadores para notificaciones
     */
    public Partido crearPartido(int idDueno, Deporte deporte, int cantJugadores,
                                LocalDateTime fechaHora, INivelJugador nivelRequerido) {
        return crearPartido(idDueno, deporte, cantJugadores, null, 
                          "90 minutos", false, fechaHora, nivelRequerido);
    }

    public Partido crearPartido(int idDueno, Deporte deporte, int cantJugadores,
                                Ubicacion ubicacion, String duracion, boolean cuentaConCancha,
                                LocalDateTime fechaHora, INivelJugador nivelRequerido) {
        // Buscar el dueño
        Cuenta dueno = cuentaRepo.buscarPorId(idDueno)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Crear el partido
        //int proximoId = partidoRepo.obtenerProximoId();
        Partido partido = new Partido(0, deporte, cantJugadores, ubicacion,
                                      duracion, cuentaConCancha, dueno, fechaHora, nivelRequerido);

        // Registrar observadores (Observer pattern)
        partido.agregarObservador(new EmailNotificacion());
        partido.agregarObservador(new PushNotificacion());

        // Guardar el partido
        partidoRepo.guardar(partido);

        System.out.println("✓ Partido creado con ID: " + partido.getId());
        return partido;
    }

    /**
     * Un usuario se une a un partido
     */
    public void unirseAPartido(int idPartido, int idJugador) {
        Partido partido = partidoRepo.buscarPorId(idPartido)
                .orElseThrow(() -> new IllegalArgumentException("Partido no encontrado"));

        Cuenta jugador = cuentaRepo.buscarPorId(idJugador)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Verificar compatibilidad de nivel si el partido lo requiere
        if (partido.getNivelRequerido() != null) {
            if (!esNivelCompatible(jugador.getNivel(), partido.getNivelRequerido())) {
                throw new IllegalArgumentException(
                    "Tu nivel (" + jugador.getNivel().getNombre() + 
                    ") no es compatible con el nivel requerido (" + 
                    partido.getNivelRequerido().getNombre() + ")"
                );
            }
        }

        // Agregar jugador (el estado maneja la lógica)
        partido.agregarJugador(jugador);
        
        // Actualizar el partido en el repositorio
        partidoRepo.actualizar(partido);
    }

    /**
     * Buscar partidos usando una estrategia de emparejamiento (Strategy pattern)
     */
    public List<Partido> buscarPartidos(IStrategyEmparejamiento estrategia) {
        // Para búsqueda, necesitamos un usuario buscador
        // Por ahora retornamos todos los disponibles
        // En una implementación más completa, recibiríamos el ID del buscador
        List<Partido> todosLosPartidos = partidoRepo.buscarDisponibles();
        return todosLosPartidos;
    }

    /**
     * Buscar partidos con estrategia para un usuario específico
     */
    public List<Partido> buscarPartidosParaUsuario(int idUsuario, IStrategyEmparejamiento estrategia) {
        Cuenta buscador = cuentaRepo.buscarPorId(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        List<Partido> todosLosPartidos = partidoRepo.buscarDisponibles();
        return estrategia.buscar(buscador, todosLosPartidos);
    }

    /**
     * Confirmar un partido (transición de estado)
     */
    public void confirmarPartido(int idPartido) {
        Partido partido = partidoRepo.buscarPorId(idPartido)
                .orElseThrow(() -> new IllegalArgumentException("Partido no encontrado"));
        
        partido.confirmar();
        partidoRepo.actualizar(partido);
    }

    /**
     * Iniciar un partido (transición de estado)
     */
    public void iniciarPartido(int idPartido) {
        Partido partido = partidoRepo.buscarPorId(idPartido)
                .orElseThrow(() -> new IllegalArgumentException("Partido no encontrado"));
        
        partido.iniciarJuego();
        partidoRepo.actualizar(partido);
    }

    /**
     * Finalizar un partido (transición de estado)
     */
    public void finalizarPartido(int idPartido) {
        Partido partido = partidoRepo.buscarPorId(idPartido)
                .orElseThrow(() -> new IllegalArgumentException("Partido no encontrado"));
        
        partido.finalizarPartido();
        partidoRepo.actualizar(partido);
    }

    /**
     * Cancelar un partido (transición de estado)
     */
    public void cancelarPartido(int idPartido) {
        Partido partido = partidoRepo.buscarPorId(idPartido)
                .orElseThrow(() -> new IllegalArgumentException("Partido no encontrado"));
        
        partido.cancelarPartido();
        partidoRepo.actualizar(partido);
    }

    /**
     * Forzar notificación de observadores (para testing)
     */
    public void notificarObservadores(int idPartido) {
        Partido partido = partidoRepo.buscarPorId(idPartido)
                .orElseThrow(() -> new IllegalArgumentException("Partido no encontrado"));
        
        partido.notificarObservadores();
    }

    public List<Partido> obtenerTodosLosPartidos() {
        return partidoRepo.buscarTodos();
    }

    public Optional<Partido> buscarPartidoPorId(int id) {
        return partidoRepo.buscarPorId(id);
    }

    /**
     * Verifica si dos niveles son compatibles
     */
    private boolean esNivelCompatible(INivelJugador nivelJugador, INivelJugador nivelRequerido) {
        // Permitir diferencia de ±5 puntos
        return Math.abs(nivelJugador.getNivel() - nivelRequerido.getNivel()) <= 5;
    }
}
