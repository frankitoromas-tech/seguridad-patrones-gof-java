package com.seguridad.controlador;

import com.seguridad.modelo.*;
import com.seguridad.patrones.creacionales.*;
import com.seguridad.persistencia.RepositorioDispositivosCSV;
import java.util.List;

// Controlador del sistema de seguridad
public class ControladorSeguridad {
    private final CentralSeguridadSingleton hub;
    private final RepositorioDispositivosCSV repo;

    public ControladorSeguridad() {
        this.hub = CentralSeguridadSingleton.getInstance();
        this.repo = new RepositorioDispositivosCSV("security_devices.csv");
        // Cargar dispositivos persistidos al iniciar
        List<DispositivoSeguridadBase> loaded = repo.loadDevices();
        for (DispositivoSeguridadBase d : loaded) {
            hub.addDevice(d);
        }
    }

    public List<DispositivoSeguridadBase> getDevices() {
        return hub.getDevices();
    }

    public UsuarioOperador getCurrentUsuarioOperador() {
        return hub.getCurrentUsuarioOperador();
    }

    public void switchUsuarioOperador(String username, UsuarioOperador.Role role) {
        hub.setCurrentUsuarioOperador(new UsuarioOperador(username, role));
    }

    public void addDevice(String id, String name, String room, String type) {
        if (id == null || id.trim().isEmpty() || name == null || name.trim().isEmpty() || room == null || room.trim().isEmpty()) {
            throw new IllegalArgumentException("Todos los campos (ID, Nombre, Ubicación) son obligatorios.");
        }
        if (hub.findDevice(id.trim()) != null) {
            throw new IllegalArgumentException("El dispositivo con ID '" + id.trim() + "' ya existe.");
        }

        DispositivoSeguridadBase device;
        switch (type.toLowerCase()) {
            case "floodlight":
                device = new FabricaReflectores().createDevice(id.trim(), name.trim(), room.trim());
                break;
            case "firedetector":
                device = new FabricaDetectoresHumo().createDevice(id.trim(), name.trim(), room.trim());
                break;
            case "camera":
                device = new FabricaVigilancia("Camera").createDevice(id.trim(), name.trim(), room.trim());
                break;
            case "lock":
                device = new FabricaVigilancia("Lock").createDevice(id.trim(), name.trim(), room.trim());
                break;
            default:
                throw new IllegalArgumentException("Tipo de dispositivo no válido: " + type);
        }

        hub.addDevice(device);
        saveState();
    }

    public void removeDevice(String id) {
        if (hub.removeDevice(id)) {
            saveState();
        }
    }

    public void toggleDevice(String id) {
        DispositivoSeguridadBase device = hub.findDevice(id);
        if (device != null) {
            device.setOn(!device.isOn());
            saveState();
        }
    }

    public void setFloodlightIntensity(String id, int intensity) {
        DispositivoSeguridadBase device = hub.findDevice(id);
        if (device instanceof ReflectorSeguridad) {
            ((ReflectorSeguridad) device).setIntensity(intensity);
            saveState();
        }
    }

    public void setFireDetectorThreshold(String id, double temp) {
        DispositivoSeguridadBase device = hub.findDevice(id);
        if (device instanceof DetectorHumoIncendio) {
            ((DetectorHumoIncendio) device).setThresholdTemperature(temp);
            saveState();
        }
    }

    public void toggleLock(String id) {
        DispositivoSeguridadBase device = hub.findDevice(id);
        if (device instanceof CerraduraElectronica) {
            CerraduraElectronica lock = (CerraduraElectronica) device;
            if ("Bloqueado".equals(lock.getStatusSummary())) {
                lock.unlock();
            } else if ("Desbloqueado".equals(lock.getStatusSummary())) {
                lock.lock();
            }
            saveState();
        }
    }

    public void resetLockAlarm(String id) {
        DispositivoSeguridadBase device = hub.findDevice(id);
        if (device instanceof CerraduraElectronica) {
            ((CerraduraElectronica) device).resetAlarm();
            saveState();
        }
    }

    public void triggerLockAlarm(String id) {
        DispositivoSeguridadBase device = hub.findDevice(id);
        if (device instanceof CerraduraElectronica) {
            ((CerraduraElectronica) device).triggerAlarm();
            saveState();
        }
    }

    public void toggleCameraRecording(String id) {
        DispositivoSeguridadBase device = hub.findDevice(id);
        if (device instanceof CamaraVigilancia) {
            CamaraVigilancia cam = (CamaraVigilancia) device;
            cam.setRecording(!cam.isRecording());
            saveState();
        }
    }

    public void runRoutine(String profileName) {
        PerfilSeguridad profile = null;
        if ("Guardia Nocturna".equalsIgnoreCase(profileName)) {
            ConstructorPerfilSeguridad builder = new ConstructorPerfilSeguridad("Guardia Nocturna");
            for (DispositivoSeguridadBase d : hub.getDevices()) {
                if (d instanceof ReflectorSeguridad) {
                    builder.adjustFloodlight(d, true, 100);
                } else if (d instanceof CerraduraElectronica) {
                    builder.toggleDevice(d, true);
                } else if (d instanceof CamaraVigilancia) {
                    builder.toggleDevice(d, true);
                }
            }
            profile = builder.build();
        } else if ("Cierre Completo".equalsIgnoreCase(profileName) || "Modo Vacaciones".equalsIgnoreCase(profileName)) {
            ConstructorPerfilSeguridad builder = new ConstructorPerfilSeguridad("Cierre Completo");
            for (DispositivoSeguridadBase d : hub.getDevices()) {
                if (d instanceof CerraduraElectronica) {
                    builder.toggleDevice(d, true);
                } else if (d instanceof CamaraVigilancia) {
                    builder.toggleDevice(d, true);
                }
            }
            profile = builder.build();
        } else if ("Desarmar".equalsIgnoreCase(profileName)) {
            ConstructorPerfilSeguridad builder = new ConstructorPerfilSeguridad("Desarmar");
            for (DispositivoSeguridadBase d : hub.getDevices()) {
                if (d instanceof ReflectorSeguridad) {
                    builder.adjustFloodlight(d, false, 0);
                } else if (d instanceof CerraduraElectronica) {
                    builder.toggleDevice(d, false);
                }
            }
            profile = builder.build();
        }

        if (profile != null) {
            profile.execute();
            hub.logActivity("Perfil '" + profileName + "' aplicado con éxito.");
            saveState();
        } else {
            hub.logActivity("[ERROR] Perfil no reconocido.");
        }
    }

    public String exportLogsReport() {
        String reportFileName = "reporte_seguridad_" + System.currentTimeMillis() + ".txt";
        try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(reportFileName))) {
            writer.println("==================================================");
            writer.println("   REPORTE DE ACTIVIDAD Y EVENTOS DE SEGURIDAD    ");
            writer.println("   Generado: " + new java.util.Date());
            writer.println("   Operador activo: " + hub.getCurrentUsuarioOperador().getUsuarioOperadorname() + " (" + hub.getCurrentUsuarioOperador().getRole() + ")");
            writer.println("==================================================");
            writer.println();
            for (String log : hub.getLogs()) {
                writer.println(log);
            }
            writer.println();
            writer.println("==================================================");
            writer.println("   Fin del Reporte - Central de Vigilancia        ");
            writer.println("==================================================");
            return reportFileName;
        } catch (java.io.IOException e) {
            System.err.println("[EXPORT ERROR] Error al exportar: " + e.getMessage());
            return null;
        }
    }

    public void saveState() {
        repo.saveDevices(hub.getDevices());
    }

    public List<String> getSystemLogs() {
        return hub.getLogs();
    }
}
