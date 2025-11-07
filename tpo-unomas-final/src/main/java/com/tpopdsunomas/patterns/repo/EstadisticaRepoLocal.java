package com.tpopdsunomas.patterns.repo;

import com.tpopdsunomas.model.Estadistica;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Patrón Repository - Implementación local (en memoria) del repositorio de Estadisticas
 */
public class EstadisticaRepoLocal implements IEstadisticaRepository {
    private Map<Integer, Estadistica> estadisticas;
    private int contadorId;

    public EstadisticaRepoLocal() {
        this.estadisticas = new HashMap<>();
        this.contadorId = 1;
    }

    @Override
    public void guardar(Estadistica estadistica) {
        if (estadistica.getId() == 0) {
            estadistica.setId(contadorId++);
        }
        estadisticas.put(estadistica.getId(), estadistica);
    }

    @Override
    public Optional<Estadistica> buscarPorId(int id) {
        return Optional.ofNullable(estadisticas.get(id));
    }

    @Override
    public List<Estadistica> buscarTodas() {
        return new ArrayList<>(estadisticas.values());
    }

    @Override
    public List<Estadistica> buscarPorPartidoId(int partidoId) {
        return estadisticas.values().stream()
                .filter(e -> e.getPartido().getId() == partidoId)
                .collect(Collectors.toList());
    }

    @Override
    public void actualizar(Estadistica estadistica) {
        estadisticas.put(estadistica.getId(), estadistica);
    }

    @Override
    public void eliminar(int id) {
        estadisticas.remove(id);
    }

    @Override
    public int obtenerProximoId() {
        return contadorId;
    }
}
    
