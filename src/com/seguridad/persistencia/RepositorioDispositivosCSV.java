package com.seguridad.persistencia;

import com.seguridad.modelo.*;
import com.seguridad.patrones.creacionales.*;
import com.seguridad.patrones.estructurales.SensorAntiguoAnalogico;
import com.seguridad.patrones.estructurales.AdaptadorSensorAntiguo;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Repositorio de persistencia en archivo CSV
public class RepositorioDispositivosCSV {
    private final String filePath;

    public RepositorioDispositivosCSV(String filePath) {
        this.filePath = filePath;
    }

    public void saveDevices(List<DispositivoSeguridadBase> devices) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (DispositivoSeguridadBase device : devices) {
                StringBuilder line = new StringBuilder();
                line.append(device.getId()).append(",")
                    .append(device.getName()).append(",")
                    .append(device.getRoom()).append(",")
                    .append(device.getType()).append(",")
                    .append(device.isOn());

                if (device instanceof ReflectorSeguridad) {
                    line.append(",").append(((ReflectorSeguridad) device).getIntensity());
                } else if (device instanceof DetectorHumoIncendio) {
                    line.append(",").append(((DetectorHumoIncendio) device).getThresholdTemperature());
                } else if (device instanceof CamaraVigilancia) {
                    line.append(",").append(((CamaraVigilancia) device).isRecording());
                } else if (device instanceof CerraduraElectronica) {
                    line.append(",").append(device.getStatusSummary());
                } else if (device instanceof AdaptadorSensorAntiguo) {
                    line.append(",0");
                } else {
                    line.append(",");
                }
                writer.println(line.toString());
            }
        } catch (IOException e) {
            System.err.println("[REPOSITORY ERROR] Fallo al guardar: " + e.getMessage());
        }
    }

    public List<DispositivoSeguridadBase> loadDevices() {
        List<DispositivoSeguridadBase> list = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return createDefaultDevices();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] tokens = line.split(",");
                if (tokens.length < 5) continue;

                String id = tokens[0];
                String name = tokens[1];
                String room = tokens[2];
                String type = tokens[3];
                boolean on = Boolean.parseBoolean(tokens[4]);

                DispositivoSeguridadBase device = null;

                if ("Floodlight".equalsIgnoreCase(type)) {
                    device = new FabricaReflectores().createDevice(id, name, room);
                    if (tokens.length >= 6) {
                        ((ReflectorSeguridad) device).setIntensity(Integer.parseInt(tokens[5]));
                    }
                } else if ("FireDetector".equalsIgnoreCase(type)) {
                    device = new FabricaDetectoresHumo().createDevice(id, name, room);
                    if (tokens.length >= 6) {
                        ((DetectorHumoIncendio) device).setThresholdTemperature(Double.parseDouble(tokens[5]));
                    }
                } else if ("Camera".equalsIgnoreCase(type)) {
                    device = new FabricaVigilancia("Camera").createDevice(id, name, room);
                    if (tokens.length >= 6) {
                        ((CamaraVigilancia) device).setRecording(Boolean.parseBoolean(tokens[5]));
                    }
                } else if ("Lock".equalsIgnoreCase(type)) {
                    device = new FabricaVigilancia("Lock").createDevice(id, name, room);
                    if (tokens.length >= 6 && "Desbloqueado".equalsIgnoreCase(tokens[5])) {
                        ((CerraduraElectronica) device).unlock();
                    }
                } else if ("LegacySensor".equalsIgnoreCase(type)) {
                    device = new AdaptadorSensorAntiguo(id, name, room, new SensorAntiguoAnalogico());
                }

                if (device != null) {
                    device.setOn(on);
                    list.add(device);
                }
            }
        } catch (Exception e) {
            System.err.println("[REPOSITORY ERROR] Error al cargar: " + e.getMessage());
            return createDefaultDevices();
        }
        return list;
    }

    private List<DispositivoSeguridadBase> createDefaultDevices() {
        List<DispositivoSeguridadBase> defaults = new ArrayList<>();
        defaults.add(new FabricaReflectores().createDevice("RF1", "Reflector Entrada", "Exterior"));
        defaults.add(new FabricaDetectoresHumo().createDevice("SD1", "Sensor de Humo", "Pasillo"));
        defaults.add(new FabricaVigilancia("Camera").createDevice("CAM1", "Cámara Perímetro", "Exterior"));
        defaults.add(new FabricaVigilancia("Lock").createDevice("LK1", "Esclusa de Entrada", "Bóveda"));
        
        SensorAntiguoAnalogico legacySensor = new SensorAntiguoAnalogico();
        defaults.add(new AdaptadorSensorAntiguo("PIR1", "Detector Infrarrojo Antiguo", "Almacén", legacySensor));
        return defaults;
    }
}
