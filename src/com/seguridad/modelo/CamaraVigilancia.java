package com.seguridad.modelo;

// Cámara de videovigilancia
public class CamaraVigilancia extends DispositivoSeguridadBase {
    private boolean recording;

    public CamaraVigilancia(String id, String name, String room) {
        super(id, name, room);
        this.recording = false;
    }

    public boolean isRecording() {
        return recording;
    }

    public void setRecording(boolean recording) {
        if (this.recording != recording) {
            this.recording = recording;
            notifyObservers(recording ? "Grabación de seguridad iniciada" : "Grabación pausada");
        }
    }

    public String getLiveVideoStream() {
        return "STREAM SEGURIDAD: [Cámara=" + getName() + ", Zona=" + getRoom() + ", Resolución=1080p, Encriptado=SÍ]";
    }

    @Override
    public String getType() {
        return "Camera";
    }

    @Override
    public String getStatusSummary() {
        if (!isOn()) return "Fuera de línea";
        return recording ? "Grabando H.265" : "En espera";
    }

    @Override
    public double getEnergyConsumption() {
        if (!isOn()) return 0.0;
        return recording ? 0.018 : 0.010;
    }
}
