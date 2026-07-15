package com.seguridad.patrones.estructurales;

import com.seguridad.modelo.CamaraVigilancia;
import com.seguridad.modelo.UsuarioOperador;
import com.seguridad.patrones.creacionales.CentralSeguridadSingleton;

// Proxy de control de acceso para el stream de cámaras
public class ProxyCamaraSeguridad {
    private final CamaraVigilancia camera;

    public ProxyCamaraSeguridad(CamaraVigilancia camera) {
        this.camera = camera;
    }

    public String getLiveVideoStream(UsuarioOperador user) throws SecurityException {
        if (user.getRole() == UsuarioOperador.Role.ADMIN || user.getRole() == UsuarioOperador.Role.OPERATOR) {
            return camera.getLiveVideoStream();
        } else {
            CentralSeguridadSingleton.getInstance().logActivity("[ACCESO RECHAZADO] Acceso bloqueado a stream de " + camera.getName() + " por " + user.getUsuarioOperadorname());
            throw new SecurityException("Acceso denegado: El rol " + user.getRole() + " carece de permisos de videovigilancia.");
        }
    }
}
