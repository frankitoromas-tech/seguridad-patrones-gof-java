package com.seguridad.patrones.creacionales;

import com.seguridad.modelo.DispositivoSeguridadBase;
import com.seguridad.modelo.UsuarioOperador;
import com.seguridad.patrones.comportamiento.ObservadorSeguridad;
import java.util.ArrayList;
import java.util.List;

// Central de monitoreo y control (Singleton)
public class CentralSeguridadSingleton implements ObservadorSeguridad {
    private static CentralSeguridadSingleton instance;
    
    private final List<DispositivoSeguridadBase> devices;
    private final List<String> logs;
    private UsuarioOperador currentUsuarioOperador;

    private CentralSeguridadSingleton() {
        devices = new ArrayList<>();
        logs = new ArrayList<>();
        currentUsuarioOperador = new UsuarioOperador("Oficial_Vargas", UsuarioOperador.Role.ADMIN);
    }

    public static synchronized CentralSeguridadSingleton getInstance() {
        if (instance == null) {
            instance = new CentralSeguridadSingleton();
        }
        return instance;
    }

    public List<DispositivoSeguridadBase> getDevices() {
        return devices;
    }

    public void addDevice(DispositivoSeguridadBase device) {
        devices.add(device);
        device.addObserver(this);
        logActivity("Dispositivo registrado: " + device.getName() + " (" + device.getType() + ")");
    }

    public boolean removeDevice(String id) {
        DispositivoSeguridadBase device = findDevice(id);
        if (device != null) {
            device.removeObserver(this);
            devices.remove(device);
            logActivity("Dispositivo retirado: " + device.getName());
            return true;
        }
        return false;
    }

    public DispositivoSeguridadBase findDevice(String id) {
        for (DispositivoSeguridadBase d : devices) {
            if (d.getId().equals(id)) {
                return d;
            }
        }
        return null;
    }

    public UsuarioOperador getCurrentUsuarioOperador() {
        return currentUsuarioOperador;
    }

    public void setCurrentUsuarioOperador(UsuarioOperador user) {
        this.currentUsuarioOperador = user;
        logActivity("Operador activo: " + user.getUsuarioOperadorname() + " (" + user.getRole() + ")");
    }

    public List<String> getLogs() {
        return new ArrayList<>(logs);
    }

    public void logActivity(String msg) {
        logs.add(msg);
        if (logs.size() > 100) {
            logs.remove(0); // Limitar historial
        }
        System.out.println("[MONITOR LOG] " + msg);
    }

    @Override
    public void onDeviceStateChanged(DispositivoSeguridadBase device, String details) {
        logActivity("[" + device.getName().toUpperCase() + "] " + details);
    }
}
