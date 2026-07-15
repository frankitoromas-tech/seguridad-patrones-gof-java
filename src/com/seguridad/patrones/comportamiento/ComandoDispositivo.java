package com.seguridad.patrones.comportamiento;

// Interfaz para comandos de automatización
public interface ComandoDispositivo {
    void execute();
    String getDescription();
}
