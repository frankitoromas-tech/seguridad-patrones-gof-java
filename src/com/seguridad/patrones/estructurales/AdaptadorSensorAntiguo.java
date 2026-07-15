package com.seguridad.patrones.estructurales;

import com.seguridad.modelo.DispositivoSeguridadBase;

// Adaptador para el sensor analógico heredado
public class AdaptadorSensorAntiguo extends DispositivoSeguridadBase {
    private final SensorAntiguoAnalogico legacyDevice;

    public AdaptadorSensorAntiguo(String id, String name, String room, SensorAntiguoAnalogico legacyDevice) {
        super(id, name, room);
        this.legacyDevice = legacyDevice;
    }

    @Override
    public boolean isOn() {
        return legacyDevice.readPinVoltage() >= 5;
    }

    @Override
    public void setOn(boolean active) {
        legacyDevice.simulateSignalChange(active ? 5 : 0);
        notifyObservers(active ? "Sensor disparado (5V)" : "Sensor restablecido (0V)");
    }

    @Override
    public String getType() {
        return "LegacySensor";
    }

    @Override
    public String getStatusSummary() {
        return isOn() ? "Sensor Activo (Alarma)" : "Sensor Listo/Ok";
    }

    @Override
    public double getEnergyConsumption() {
        return isOn() ? 0.001 : 0.0001;
    }
}
