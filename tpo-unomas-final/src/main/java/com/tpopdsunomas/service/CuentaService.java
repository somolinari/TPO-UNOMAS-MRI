package com.tpopdsunomas.service;

import com.tpopdsunomas.model.Cuenta;
import com.tpopdsunomas.patterns.repo.ICuentaRepository;
import com.tpopdsunomas.patterns.strategy.*;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de Cuentas - Capa de lógica de negocio (Controlador en MVC)
 */
public class CuentaService {
    private ICuentaRepository cuentaRepo;

    public CuentaService(ICuentaRepository cuentaRepo) {
        this.cuentaRepo = cuentaRepo;
    }

    /**
     * Registra una nueva cuenta y asigna automáticamente el nivel según los puntos
     */
    public Cuenta registrarCuenta(String nombre, String email, String clave, int puntosNivel) {
        // Verificar que el email no esté registrado
        Optional<Cuenta> existente = cuentaRepo.buscarPorEmail(email);
        if (existente.isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // Crear la cuenta
        //int proximoId = cuentaRepo.obtenerProximoId();
        Cuenta nuevaCuenta = new Cuenta(0, nombre, email, clave);
        
        // Asignar nivel según los puntos (usando el patrón Strategy)
        asignarNivelSegunPuntos(nuevaCuenta, puntosNivel);
        
        // Guardar en el repositorio
        cuentaRepo.guardar(nuevaCuenta);
        
        return nuevaCuenta;
    }

    /**
     * Asigna el nivel correspondiente según los puntos (evita usar enum)
     */
    private void asignarNivelSegunPuntos(Cuenta cuenta, int puntos) {
        if (puntos <= 10) {
            new Principiante(puntos, cuenta);
        } else if (puntos <= 20) {
            new Intermedio(puntos, cuenta);
        } else {
            new Avanzado(puntos, cuenta);
        }
    }

    public Optional<Cuenta> buscarPorId(int id) {
        return cuentaRepo.buscarPorId(id);
    }

    public Optional<Cuenta> buscarPorEmail(String email) {
        return cuentaRepo.buscarPorEmail(email);
    }

    public List<Cuenta> obtenerTodasLasCuentas() {
        return cuentaRepo.buscarTodas();
    }

    public void actualizarCuenta(Cuenta cuenta) {
        cuentaRepo.actualizar(cuenta);
    }

    public void eliminarCuenta(int id) {
        cuentaRepo.eliminar(id);
    }
}
