package com.seguridad.patrones.estructurales;

import com.seguridad.modelo.DispositivoSeguridadBase;
import com.seguridad.patrones.comportamiento.ObservadorSeguridad;

// Decorador base para extender comportamiento
public abstract class DecoradorDispositivo extends DispositivoSeguridadBase {
    protected final DispositivoSeguridadBase decoratedDevice;

    public DecoradorDispositivo(DispositivoSeguridadBase decoratedDevice) {
        super(decoratedDevice.getId(), decoratedDevice.getName(), decoratedDevice.getRoom());
        this.decoratedDevice = decoratedDevice;
    }

    @Override
    public boolean isOn() {
        return decoratedDevice.isOn();
    }

    @Override
    public void setOn(boolean on) {
        decoratedDevice.setOn(on);
    }

    @Override
    public String getType() {
        return decoratedDevice.getType();
    }

    @Override
    public String getStatusSummary() {
        return decoratedDevice.getStatusSummary();
    }

    @Override
    public double getEnergyConsumption() {
        return decoratedDevice.getEnergyConsumption();
    }

    @Override
    public void addObserver(ObservadorSeguridad observer) {
        decoratedDevice.addObserver(observer);
    }

    @Override
    public void removeObserver(ObservadorSeguridad observer) {
        decoratedDevice.removeObserver(observer);
    }

    @Override
    public void notifyObservers(String details) {
        decoratedDevice.notifyObservers(details);
    }
}
