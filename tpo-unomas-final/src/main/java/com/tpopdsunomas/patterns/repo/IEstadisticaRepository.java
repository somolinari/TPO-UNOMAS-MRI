package com.tpopdsunomas.patterns.repo;
import com.tpopdsunomas.model.Estadistica;
import java.util.List;
import java.util.Optional;

/**
 * Patrón Repository - Interfaz para repositorio de Estadisticas
 */
public interface IEstadisticaRepository {
    void guardar(Estadistica estadistica);
    Optional<Estadistica> buscarPorId(int id);
    List<Estadistica> buscarTodas();
    List<Estadistica> buscarPorPartidoId(int partidoId);
    void actualizar(Estadistica estadistica);
    void eliminar(int id);
    int obtenerProximoId();
}
    
