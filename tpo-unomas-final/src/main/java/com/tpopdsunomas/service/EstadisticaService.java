package com.tpopdsunomas.service;

import com.tpopdsunomas.model.Cuenta;
import com.tpopdsunomas.model.Estadistica;
import com.tpopdsunomas.model.Partido;
import com.tpopdsunomas.patterns.repo.ICuentaRepository;
import com.tpopdsunomas.patterns.repo.IEstadisticaRepository;
import com.tpopdsunomas.patterns.repo.IPartidoRepository;
import java.util.List;
import java.util.Optional;

/**
 * Servicio de Estadisticas - Capa de lógica de negocio
 */
public class EstadisticaService {
    
    private IEstadisticaRepository estadisticaRepo;
    private ICuentaRepository cuentaRepo;
    private IPartidoRepository partidoRepo;

    public EstadisticaService(IEstadisticaRepository estadisticaRepo, ICuentaRepository cuentaRepo, IPartidoRepository partidoRepo) {
        this.estadisticaRepo = estadisticaRepo;
        this.cuentaRepo = cuentaRepo;
        this.partidoRepo = partidoRepo;
    }

    /**
     * Crea y guarda una nueva estadística para un jugador en un partido
     */
    public Estadistica crearEstadistica(int idJugador, int idPartido, int puntos, int asistencias, int faltas, String mvp, String descripcion) {
        
        Cuenta jugador = cuentaRepo.buscarPorId(idJugador)
            .orElseThrow(() -> new IllegalArgumentException("Jugador no encontrado con ID: " + idJugador));
        
        Partido partido = partidoRepo.buscarPorId(idPartido)
            .orElseThrow(() -> new IllegalArgumentException("Partido no encontrado con ID: " + idPartido));

        // Validación para evitar duplicados (un jugador solo puede tener una estadística por partido)
        Optional<Estadistica> existente = estadisticaRepo.buscarPorPartidoId(idPartido).stream()
            .filter(e -> e.getCuenta().getId() == idJugador)
            .findFirst();

        if (existente.isPresent()) {
            throw new IllegalArgumentException("Ya existen estadísticas para " + jugador.getNombre() + " en este partido.");
        }

        // Usamos el constructor que nos diste, pasando 0 para que el repo asigne el ID
        Estadistica nuevaEstadistica = new Estadistica(0, jugador, partido, puntos, asistencias, faltas);
        nuevaEstadistica.setMvp(mvp);
        nuevaEstadistica.setDescripcion(descripcion);

        estadisticaRepo.guardar(nuevaEstadistica);
        return nuevaEstadistica;
    }

    /**
     * Obtiene todas las estadísticas de un partido específico
     */
    public List<Estadistica> obtenerEstadisticasPorPartido(int partidoId) {
        return estadisticaRepo.buscarPorPartidoId(partidoId);
    }

    // (Puedes añadir más métodos como buscarPorId, actualizar, eliminar, etc. si los necesitas)
}

