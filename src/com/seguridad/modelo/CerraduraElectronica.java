package com.seguridad.modelo;

import com.seguridad.patrones.comportamiento.EstadoCerradura;
import com.seguridad.patrones.comportamiento.EstadoBloqueado;

// Cerrojo electrónico inteligente
public class CerraduraElectronica extends DispositivoSeguridadBase {
    private EstadoCerradura state;

    public CerraduraElectronica(String id, String name, String room) {
        super(id, name, room);
        this.state = new EstadoBloqueado(); // Estado inicial
    }

    public EstadoCerradura getState() {
        return state;
    }

    public void setState(EstadoCerradura state) {
        this.state = state;
    }

    public void lock() {
        state.lock(this);
    }

    public void unlock() {
        state.unlock(this);
    }

    public void triggerAlarm() {
        state.triggerAlarm(this);
    }

    public void resetAlarm() {
        state.resetAlarm(this);
    }

    @Override
    public String getType() {
        return "Lock";
    }

    @Override
    public String getStatusSummary() {
        return state.getStatusString();
    }

    @Override
    public double getEnergyConsumption() {
        return (state instanceof com.seguridad.patrones.comportamiento.EstadoAlarmaActivada) ? 0.15 : 0.002;
    }
}
