package com.tpopdsunomas.patterns.observer;

import com.tpopdsunomas.model.Partido;

/**
 * Patrón Observer - Interfaz para observadores de notificaciones
 * Los observadores son notificados cuando el partido cambia de estado
 */
public interface IObserverNotificacion {
    void actualizar(Partido partido);
}
