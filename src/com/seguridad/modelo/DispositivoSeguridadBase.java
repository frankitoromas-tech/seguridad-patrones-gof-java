package com.seguridad.modelo;

import com.seguridad.patrones.comportamiento.ObservadorSeguridad;
import com.seguridad.patrones.comportamiento.SujetoObservable;
import java.util.ArrayList;
import java.util.List;

// Abstracción base del dispositivo
public abstract class DispositivoSeguridadBase implements SujetoObservable {
    private final String id;
    private String name;
    private final String room;
    private boolean on;
    private final List<ObservadorSeguridad> observers = new ArrayList<>();

    public DispositivoSeguridadBase(String id, String name, String room) {
        this.id = id;
        this.name = name;
        this.room = room;
        this.on = false;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoom() {
        return room;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        if (this.on != on) {
            this.on = on;
            notifyObservers(on ? "Activado" : "Desactivado");
        }
    }

    public abstract String getType();
    public abstract String getStatusSummary();
    
    // Consumo eléctrico estimado en kWh
    public abstract double getEnergyConsumption();

    @Override
    public void addObserver(ObservadorSeguridad observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(ObservadorSeguridad observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String details) {
        for (ObservadorSeguridad observer : observers) {
            observer.onDeviceStateChanged(this, details);
        }
    }
}
