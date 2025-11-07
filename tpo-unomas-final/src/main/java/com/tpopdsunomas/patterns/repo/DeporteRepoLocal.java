package com.tpopdsunomas.patterns.repo;

import com.tpopdsunomas.model.Deporte;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Patrón Repository - Implementación local (en memoria) del repositorio de Deportes
 */
public class DeporteRepoLocal implements IDeporteRepository {
    private Map<Integer, Deporte> deportes;

    public DeporteRepoLocal() {
        this.deportes = new HashMap<>();
    }

    @Override
    public void guardar(Deporte deporte) {
        deportes.put(deporte.getId(), deporte);
    }

    @Override
    public Optional<Deporte> buscarPorId(int id) {
        return Optional.ofNullable(deportes.get(id));
    }

    @Override
    public Optional<Deporte> buscarPorNombre(String nombre) {
        return deportes.values().stream()
                .filter(d -> d.getNombre().equalsIgnoreCase(nombre))
                .findFirst();
    }

    @Override
    public List<Deporte> buscarTodos() {
        return new ArrayList<>(deportes.values());
    }

    @Override
    public void actualizar(Deporte deporte) {
        deportes.put(deporte.getId(), deporte);
    }

    @Override
    public void eliminar(int id) {
        deportes.remove(id);
    }
}
