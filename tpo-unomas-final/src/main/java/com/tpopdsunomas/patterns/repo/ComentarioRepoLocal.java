package com.tpopdsunomas.patterns.repo;

import com.tpopdsunomas.model.Comentario;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Patrón Repository - Implementación local (en memoria) del repositorio de Comentarios
 */
public class ComentarioRepoLocal implements IComentarioRepository {
    private Map<Integer, Comentario> comentarios;
    private int contadorId;

    public ComentarioRepoLocal() {
        this.comentarios = new HashMap<>();
        this.contadorId = 1;
    }

    @Override
    public void guardar(Comentario comentario) {
        if (comentario.getId() == 0) {
            comentario.setId(contadorId++);
        }
        comentarios.put(comentario.getId(), comentario);
    }

    @Override
    public Optional<Comentario> buscarPorId(int id) {
        return Optional.ofNullable(comentarios.get(id));
    }

    @Override
    public List<Comentario> buscarTodas() {
        return new ArrayList<>(comentarios.values());
    }

    @Override
    public List<Comentario> buscarPorPartidoId(int partidoId) {
        return comentarios.values().stream()
                .filter(c -> c.getPartido().getId() == partidoId)
                .collect(Collectors.toList());
    }

    @Override
    public void actualizar(Comentario comentario) {
        comentarios.put(comentario.getId(), comentario);
    }

    @Override
    public void eliminar(int id) {
        comentarios.remove(id);
    }

    @Override
    public int obtenerProximoId() {
        return contadorId;
    }
}
    