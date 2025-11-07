package com.tpopdsunomas.patterns.repo;

import com.tpopdsunomas.model.Partido;
import com.tpopdsunomas.model.Deporte;
import com.tpopdsunomas.patterns.state.NecesitaJugadores;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Patrón Repository - Implementación local (en memoria) del repositorio de Partidos
 */
public class PartidoRepoLocal implements IPartidoRepository {
    private Map<Integer, Partido> partidos;
    private int contadorId;

    public PartidoRepoLocal() {
        this.partidos = new HashMap<>();
        this.contadorId = 1;
    }

    @Override
    public void guardar(Partido partido) {
        if (partido.getId() == 0) {
            partido.setId(contadorId++);
        }
        partidos.put(partido.getId(), partido);
    }

    @Override
    public Optional<Partido> buscarPorId(int id) {
        return Optional.ofNullable(partidos.get(id));
    }

    @Override
    public List<Partido> buscarTodos() {
        return new ArrayList<>(partidos.values());
    }

    @Override
    public List<Partido> buscarPorDeporte(Deporte deporte) {
        return partidos.values().stream()
                .filter(p -> p.getTipoDeporte().equals(deporte))
                .collect(Collectors.toList());
    }

    @Override
    public List<Partido> buscarDisponibles() {
        return partidos.values().stream()
                .filter(p -> p.getEstado() instanceof NecesitaJugadores)
                .filter(p -> !p.estaCompleto())
                .collect(Collectors.toList());
    }

    @Override
    public void actualizar(Partido partido) {
        partidos.put(partido.getId(), partido);
    }

    @Override
    public void eliminar(int id) {
        partidos.remove(id);
    }

    @Override
    public int obtenerProximoId() {
        return contadorId;
    }
}
