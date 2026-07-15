package com.seguridad.modelo;

// Datos del operador y rol asignado
public class UsuarioOperador {
    public enum Role {
        ADMIN,
        OPERATOR,
        GUEST
    }

    private final String username;
    private final Role role;

    public UsuarioOperador(String username, Role role) {
        this.username = username;
        this.role = role;
    }

    public String getUsuarioOperadorname() {
        return username;
    }

    public Role getRole() {
        return role;
    }
}
