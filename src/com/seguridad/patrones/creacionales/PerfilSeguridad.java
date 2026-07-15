package com.seguridad.patrones.creacionales;

import com.seguridad.patrones.comportamiento.ComandoDispositivo;
import java.util.ArrayList;
import java.util.List;

// Perfil de seguridad (Macro automatizado)
public class PerfilSeguridad {
    private final String name;
    private final List<ComandoDispositivo> commands;

    public PerfilSeguridad(String name) {
        this.name = name;
        this.commands = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<ComandoDispositivo> getCommands() {
        return commands;
    }

    public void addCommand(ComandoDispositivo command) {
        commands.add(command);
    }

    public void execute() {
        System.out.println("[PROFILE EXECUTION] Aplicando perfil: " + name);
        for (ComandoDispositivo cmd : commands) {
            cmd.execute();
        }
    }
}
