package com.seguridad.patrones.comportamiento;

import com.seguridad.modelo.DispositivoSeguridadBase;

// Interfaz para observadores de seguridad
public interface ObservadorSeguridad {
    void onDeviceStateChanged(DispositivoSeguridadBase device, String details);
}
