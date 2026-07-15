package com.seguridad.patrones.comportamiento;

import com.seguridad.modelo.CerraduraElectronica;

// Estado de la cerradura electrónica
public interface EstadoCerradura {
    void lock(CerraduraElectronica lock);
    void unlock(CerraduraElectronica lock);
    void triggerAlarm(CerraduraElectronica lock);
    void resetAlarm(CerraduraElectronica lock);
    String getStatusString();
}
