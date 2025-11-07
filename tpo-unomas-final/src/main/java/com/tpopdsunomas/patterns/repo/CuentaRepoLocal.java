package com.tpopdsunomas.patterns.repo;

import com.tpopdsunomas.model.Cuenta;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Patrón Repository - Implementación local (en memoria) del repositorio de Cuentas
 */
public class CuentaRepoLocal implements ICuentaRepository {
    private Map<Integer, Cuenta> cuentas;
    private int contadorId;

    public CuentaRepoLocal() {
        this.cuentas = new HashMap<>();
        this.contadorId = 1;
    }

    @Override
    public void guardar(Cuenta cuenta) {
        if (cuenta.getId() == 0) {
            cuenta.setId(contadorId++);
        }
        cuentas.put(cuenta.getId(), cuenta);
    }

    @Override
    public Optional<Cuenta> buscarPorId(int id) {
        return Optional.ofNullable(cuentas.get(id));
    }

    @Override
    public Optional<Cuenta> buscarPorEmail(String email) {
        return cuentas.values().stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public List<Cuenta> buscarTodas() {
        return new ArrayList<>(cuentas.values());
    }

    @Override
    public void actualizar(Cuenta cuenta) {
        cuentas.put(cuenta.getId(), cuenta);
    }

    @Override
    public void eliminar(int id) {
        cuentas.remove(id);
    }

    @Override
    public int obtenerProximoId() {
        return contadorId;
    }
}
