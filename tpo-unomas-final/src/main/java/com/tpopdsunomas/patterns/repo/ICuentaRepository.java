package com.tpopdsunomas.patterns.repo;

import com.tpopdsunomas.model.Cuenta;
import java.util.List;
import java.util.Optional;

/**
 * Patrón Repository - Interfaz para repositorio de Cuentas
 * Abstrae la persistencia de datos
 */
public interface ICuentaRepository {
    void guardar(Cuenta cuenta);
    Optional<Cuenta> buscarPorId(int id);
    Optional<Cuenta> buscarPorEmail(String email);
    List<Cuenta> buscarTodas();
    void actualizar(Cuenta cuenta);
    void eliminar(int id);
    int obtenerProximoId();
}
