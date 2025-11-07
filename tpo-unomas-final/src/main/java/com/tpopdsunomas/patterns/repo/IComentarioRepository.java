package com.tpopdsunomas.patterns.repo;
import com.tpopdsunomas.model.Comentario;
import java.util.List;
import java.util.Optional;


/**
 * Patrón Repository - Interfaz para repositorio de Comentarios
 */
public interface IComentarioRepository {
    void guardar(Comentario comentario);
    Optional<Comentario> buscarPorId(int id);
    List<Comentario> buscarTodas();
    List<Comentario> buscarPorPartidoId(int partidoId);
    void actualizar(Comentario comentario);
    void eliminar(int id);
    int obtenerProximoId();
}

