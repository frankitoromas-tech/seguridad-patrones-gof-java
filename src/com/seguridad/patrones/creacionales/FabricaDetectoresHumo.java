package com.seguridad.patrones.creacionales;

import com.seguridad.modelo.DispositivoSeguridadBase;
import com.seguridad.modelo.DetectorHumoIncendio;

// Fábrica de detectores de incendio
public class FabricaDetectoresHumo extends FabricaDispositivos {
    @Override
    public DispositivoSeguridadBase createDevice(String id, String name, String room) {
        return new DetectorHumoIncendio(id, name, room);
    }
}
