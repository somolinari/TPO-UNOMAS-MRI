package com.tpopdsunomas.patterns.repo;

import com.tpopdsunomas.model.Deporte;
import java.util.List;
import java.util.Optional;

/**
 * Patrón Repository - Interfaz para repositorio de Deportes
 * Abstrae la persistencia de datos
 */
public interface IDeporteRepository {
    void guardar(Deporte deporte);
    Optional<Deporte> buscarPorId(int id);
    Optional<Deporte> buscarPorNombre(String nombre);
    List<Deporte> buscarTodos();
    void actualizar(Deporte deporte);
    void eliminar(int id);
}
