package com.seguridad.patrones.creacionales;

import com.seguridad.modelo.DispositivoSeguridadBase;
import com.seguridad.modelo.ReflectorSeguridad;

// Fábrica de reflectores
public class FabricaReflectores extends FabricaDispositivos {
    @Override
    public DispositivoSeguridadBase createDevice(String id, String name, String room) {
        return new ReflectorSeguridad(id, name, room);
    }
}
