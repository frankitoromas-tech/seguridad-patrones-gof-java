package com.seguridad.principal;

import com.seguridad.controlador.ControladorSeguridad;
import com.seguridad.vista.*;
import com.seguridad.patrones.creacionales.CentralSeguridadSingleton;
import javax.swing.SwingUtilities;

// Inicio de la aplicación
public class SistemaVigilanciaMain {
    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("  Surveillance System - Verificación de Patrones ");
        System.out.println("=================================================");

        // Validar Singleton
        CentralSeguridadSingleton instance1 = CentralSeguridadSingleton.getInstance();
        CentralSeguridadSingleton instance2 = CentralSeguridadSingleton.getInstance();
        System.out.println("[SINGLETON] Instancia 1: " + instance1.hashCode());
        System.out.println("[SINGLETON] Instancia 2: " + instance2.hashCode());
        System.out.println("[SINGLETON] ¿Misma instancia?: " + (instance1 == instance2 ? "SÍ" : "NO"));
        System.out.println("-------------------------------------------------");

        // Cargar interfaz gráfica
        SwingUtilities.invokeLater(() -> {
            try {
                // Estilo visual moderno
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception ex) {
                // fall back to default
            }
            
            ControladorSeguridad controller = new ControladorSeguridad();
            DialogoAcceso login = new DialogoAcceso(null, controller);
            login.setVisible(true);
            if (login.isAuthenticated()) {
                PanelControlGUI frame = new PanelControlGUI(controller);
                frame.setVisible(true);
            } else {
                System.exit(0);
            }
        });
    }
}
