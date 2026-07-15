package com.seguridad.patrones.creacionales;

import com.seguridad.modelo.DispositivoSeguridadBase;
import com.seguridad.modelo.CamaraVigilancia;
import com.seguridad.modelo.CerraduraElectronica;

// Fábrica de cámaras y cerrojos
public class FabricaVigilancia extends FabricaDispositivos {
    private final String type;

    public FabricaVigilancia(String type) {
        this.type = type;
    }

    @Override
    public DispositivoSeguridadBase createDevice(String id, String name, String room) {
        if ("Lock".equalsIgnoreCase(type)) {
            return new CerraduraElectronica(id, name, room);
        } else {
            return new CamaraVigilancia(id, name, room);
        }
    }
}
