package com.seguridad.patrones.estructurales;

import com.seguridad.modelo.DispositivoSeguridadBase;
import java.util.ArrayList;
import java.util.List;

// Decorador concreto que audita el consumo y respaldo eléctrico
public class DecoradorAuditorEnergia extends DecoradorDispositivo {
    private final List<Double> consumptionHistory;

    public DecoradorAuditorEnergia(DispositivoSeguridadBase decoratedDevice) {
        super(decoratedDevice);
        this.consumptionHistory = new ArrayList<>();
    }

    @Override
    public void setOn(boolean on) {
        super.setOn(on);
        trackConsumption();
    }

    public void trackConsumption() {
        double currentCons = getEnergyConsumption();
        consumptionHistory.add(currentCons);
        if (consumptionHistory.size() > 5) {
            consumptionHistory.remove(0); // Limitar historial a últimas muestras
        }
    }

    public List<Double> getConsumptionHistory() {
        return consumptionHistory;
    }

    public double getAverageConsumption() {
        if (consumptionHistory.isEmpty()) return getEnergyConsumption();
        double sum = 0;
        for (double val : consumptionHistory) {
            sum += val;
        }
        return sum / consumptionHistory.size();
    }

    @Override
    public String getStatusSummary() {
        return super.getStatusSummary() + " [Respaldo & Consumo: " 
            + String.format("%.3f", getAverageConsumption()) + " kW/h]";
    }
}
