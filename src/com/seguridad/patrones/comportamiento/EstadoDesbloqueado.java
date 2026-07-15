package com.seguridad.patrones.comportamiento;

import com.seguridad.modelo.CerraduraElectronica;

// Estado: Desbloqueado
public class EstadoDesbloqueado implements EstadoCerradura {
    @Override
    public void lock(CerraduraElectronica lock) {
        lock.setState(new EstadoBloqueado());
        lock.notifyObservers("Bloqueado");
    }

    @Override
    public void unlock(CerraduraElectronica lock) {
        // Ya está desbloqueado
    }

    @Override
    public void triggerAlarm(CerraduraElectronica lock) {
        lock.setState(new EstadoAlarmaActivada());
        lock.notifyObservers("¡ALERTA! Intrusión detectada");
    }

    @Override
    public void resetAlarm(CerraduraElectronica lock) {
        // Sin alarma activa
    }

    @Override
    public String getStatusString() {
        return "Desbloqueado";
    }
}
