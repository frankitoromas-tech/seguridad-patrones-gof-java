package com.seguridad.modelo;

// Reflector de iluminación de alta potencia
public class ReflectorSeguridad extends DispositivoSeguridadBase {
    private int intensity; // 0 to 100

    public ReflectorSeguridad(String id, String name, String room) {
        super(id, name, room);
        this.intensity = 50;
    }

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        if (this.intensity != intensity) {
            this.intensity = Math.max(0, Math.min(100, intensity));
            notifyObservers("Intensidad de reflector cambiada a " + this.intensity + "%");
        }
    }

    @Override
    public String getType() {
        return "Floodlight";
    }

    @Override
    public String getStatusSummary() {
        return isOn() ? "Iluminando - " + intensity + "%" : "Apagado";
    }

    @Override
    public double getEnergyConsumption() {
        // Consumo proporcional a la potencia
        return isOn() ? (0.02 + (0.13 * (intensity / 100.0))) : 0.0;
    }
}
