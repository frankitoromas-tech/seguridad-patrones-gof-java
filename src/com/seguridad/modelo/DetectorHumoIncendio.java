package com.seguridad.modelo;

// Detector de incendios y humos
public class DetectorHumoIncendio extends DispositivoSeguridadBase {
    private double thresholdTemperature;

    public DetectorHumoIncendio(String id, String name, String room) {
        super(id, name, room);
        this.thresholdTemperature = 57.0;
    }

    public double getThresholdTemperature() {
        return thresholdTemperature;
    }

    public void setThresholdTemperature(double temp) {
        if (this.thresholdTemperature != temp) {
            this.thresholdTemperature = temp;
            notifyObservers("Umbral de temperatura establecido en " + temp + "°C");
        }
    }

    @Override
    public String getType() {
        return "FireDetector";
    }

    @Override
    public String getStatusSummary() {
        return isOn() ? "Sensor Activo - Vigilando Humos" : "Inactivo";
    }

    @Override
    public double getEnergyConsumption() {
        return isOn() ? 0.003 : 0.0;
    }
}
