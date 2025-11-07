package com.tpopdsunomas.service;

import com.tpopdsunomas.model.Deporte;
import com.tpopdsunomas.patterns.repo.IDeporteRepository;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de Deportes - Capa de lógica de negocio (Controlador en MVC)
 */
public class DeporteService {
    private IDeporteRepository deporteRepo;

    public DeporteService(IDeporteRepository deporteRepo) {
        this.deporteRepo = deporteRepo;
    }

    public void guardar(Deporte deporte) {
        deporteRepo.guardar(deporte);
    }

    public Optional<Deporte> buscarPorId(int id) {
        return deporteRepo.buscarPorId(id);
    }

    public Optional<Deporte> buscarPorNombre(String nombre) {
        return deporteRepo.buscarPorNombre(nombre);
    }

    public List<Deporte> obtenerTodos() {
        return deporteRepo.buscarTodos();
    }

    public void actualizar(Deporte deporte) {
        deporteRepo.actualizar(deporte);
    }

    public void eliminar(int id) {
        deporteRepo.eliminar(id);
    }
}
