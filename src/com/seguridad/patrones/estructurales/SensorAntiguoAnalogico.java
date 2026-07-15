package com.seguridad.patrones.estructurales;

// Sensor de contacto analógico heredado (PIR)
public class SensorAntiguoAnalogico {
    private int voltageState; // 0 = normal, 5 = alarma activa

    public SensorAntiguoAnalogico() {
        this.voltageState = 0;
    }

    public void simulateSignalChange(int voltage) {
        this.voltageState = voltage;
    }

    public int readPinVoltage() {
        return voltageState;
    }
}
