package com.seguridad.patrones.comportamiento;

import com.seguridad.modelo.DispositivoSeguridadBase;
import com.seguridad.modelo.ReflectorSeguridad;
import com.seguridad.modelo.DetectorHumoIncendio;
import com.seguridad.modelo.CerraduraElectronica;

// Comando concreto para cambiar el estado de un dispositivo
public class ComandoAccionSeguridad implements ComandoDispositivo {
    private final DispositivoSeguridadBase device;
    private final boolean targetState; // estado objetivo
    private final Integer targetIntensity; // intensidad (opcional)
    private final Double targetThreshold; // umbral de temperatura (opcional)

    public ComandoAccionSeguridad(DispositivoSeguridadBase device, boolean targetState) {
        this.device = device;
        this.targetState = targetState;
        this.targetIntensity = null;
        this.targetThreshold = null;
    }

    public ComandoAccionSeguridad(DispositivoSeguridadBase device, boolean targetState, int intensity) {
        this.device = device;
        this.targetState = targetState;
        this.targetIntensity = intensity;
        this.targetThreshold = null;
    }

    public ComandoAccionSeguridad(DispositivoSeguridadBase device, boolean targetState, double threshold) {
        this.device = device;
        this.targetState = targetState;
        this.targetIntensity = null;
        this.targetThreshold = threshold;
    }

    @Override
    public void execute() {
        if (device instanceof CerraduraElectronica) {
            CerraduraElectronica lock = (CerraduraElectronica) device;
            if (targetState) {
                lock.lock();
            } else {
                lock.unlock();
            }
        } else {
            device.setOn(targetState);
        }

        if (targetIntensity != null && device instanceof ReflectorSeguridad) {
            ((ReflectorSeguridad) device).setIntensity(targetIntensity);
        }
        if (targetThreshold != null && device instanceof DetectorHumoIncendio) {
            ((DetectorHumoIncendio) device).setThresholdTemperature(targetThreshold);
        }
    }

    @Override
    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(device.getName());
        if (device instanceof CerraduraElectronica) {
            sb.append(targetState ? " -> Asegurar" : " -> Desbloquear");
        } else {
            sb.append(targetState ? " -> Activar" : " -> Desactivar");
        }
        if (targetIntensity != null) {
            sb.append(" (Intensidad: ").append(targetIntensity).append("%)");
        }
        if (targetThreshold != null) {
            sb.append(" (Alarma: ").append(targetThreshold).append("°C)");
        }
        return sb.toString();
    }
}
