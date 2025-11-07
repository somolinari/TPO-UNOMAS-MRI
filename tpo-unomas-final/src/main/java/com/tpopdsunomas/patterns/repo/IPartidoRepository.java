package com.tpopdsunomas.patterns.repo;

import com.tpopdsunomas.model.Partido;
import com.tpopdsunomas.model.Deporte;
import java.util.List;
import java.util.Optional;

/**
 * Patrón Repository - Interfaz para repositorio de Partidos
 * Abstrae la persistencia de datos
 */
public interface IPartidoRepository {
    void guardar(Partido partido);
    Optional<Partido> buscarPorId(int id);
    List<Partido> buscarTodos();
    List<Partido> buscarPorDeporte(Deporte deporte);
    List<Partido> buscarDisponibles(); // Partidos que aún necesitan jugadores
    void actualizar(Partido partido);
    void eliminar(int id);
    int obtenerProximoId();
}
