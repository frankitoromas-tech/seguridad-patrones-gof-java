package com.seguridad.patrones.comportamiento;

// Interfaz para el sujeto observable
public interface SujetoObservable {
    void addObserver(ObservadorSeguridad observer);
    void removeObserver(ObservadorSeguridad observer);
    void notifyObservers(String details);
}
