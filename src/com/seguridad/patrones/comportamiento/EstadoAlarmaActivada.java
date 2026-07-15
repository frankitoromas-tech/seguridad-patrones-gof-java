package com.seguridad.patrones.comportamiento;

import com.seguridad.modelo.CerraduraElectronica;

// Estado: Alarma Activada
public class EstadoAlarmaActivada implements EstadoCerradura {
    @Override
    public void lock(CerraduraElectronica lock) {
        // Bloqueado bajo alarma
    }

    @Override
    public void unlock(CerraduraElectronica lock) {
        // Bloqueado bajo alarma
    }

    @Override
    public void triggerAlarm(CerraduraElectronica lock) {
        // Ya está disparada
    }

    @Override
    public void resetAlarm(CerraduraElectronica lock) {
        lock.setState(new EstadoBloqueado());
        lock.notifyObservers("Alarma desactivada - Acceso bloqueado");
    }

    @Override
    public String getStatusString() {
        return "¡ALARMA DISPARADA!";
    }
}
