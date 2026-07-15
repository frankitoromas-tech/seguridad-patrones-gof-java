package com.seguridad.patrones.comportamiento;

import com.seguridad.modelo.CerraduraElectronica;

// Estado: Bloqueado
public class EstadoBloqueado implements EstadoCerradura {
    @Override
    public void lock(CerraduraElectronica lock) {
        // Ya está bloqueado
    }

    @Override
    public void unlock(CerraduraElectronica lock) {
        lock.setState(new EstadoDesbloqueado());
        lock.notifyObservers("Desbloqueado");
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
        return "Bloqueado";
    }
}
