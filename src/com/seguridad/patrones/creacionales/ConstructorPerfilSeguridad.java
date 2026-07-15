package com.seguridad.patrones.creacionales;

import com.seguridad.modelo.DispositivoSeguridadBase;
import com.seguridad.patrones.comportamiento.ComandoAccionSeguridad;

// Constructor paso a paso del perfil de seguridad
public class ConstructorPerfilSeguridad {
    private PerfilSeguridad profile;

    public ConstructorPerfilSeguridad(String profileName) {
        this.profile = new PerfilSeguridad(profileName);
    }

    public ConstructorPerfilSeguridad toggleDevice(DispositivoSeguridadBase device, boolean state) {
        profile.addCommand(new ComandoAccionSeguridad(device, state));
        return this;
    }

    public ConstructorPerfilSeguridad adjustFloodlight(DispositivoSeguridadBase floodlight, boolean state, int intensity) {
        profile.addCommand(new ComandoAccionSeguridad(floodlight, state, intensity));
        return this;
    }

    public ConstructorPerfilSeguridad adjustFireThreshold(DispositivoSeguridadBase fireDetector, boolean state, double threshold) {
        profile.addCommand(new ComandoAccionSeguridad(fireDetector, state, threshold));
        return this;
    }

    public PerfilSeguridad build() {
        PerfilSeguridad built = this.profile;
        this.profile = null;
        return built;
    }
}
