package com.seguridad.patrones.creacionales;

import com.seguridad.modelo.DispositivoSeguridadBase;

// Creador abstracto de dispositivos
public abstract class FabricaDispositivos {
    public abstract DispositivoSeguridadBase createDevice(String id, String name, String room);
}
