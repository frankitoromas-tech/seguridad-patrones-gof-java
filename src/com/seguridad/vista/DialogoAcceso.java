package com.seguridad.vista;

import com.seguridad.controlador.ControladorSeguridad;
import com.seguridad.modelo.UsuarioOperador;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

// Pantalla de acceso para el centro de seguridad
public class DialogoAcceso extends JDialog {
    private final ControladorSeguridad controller;
    private boolean authenticated = false;

    private final Color colorBg = new Color(20, 20, 25);
    private final Color colorCard = new Color(32, 32, 42);
    private final Color colorAccent = new Color(79, 110, 242);
    private final Color colorTextPrimary = Color.WHITE;
    private final Color colorTextSecondary = new Color(170, 170, 185);

    public DialogoAcceso(Frame parent, ControladorSeguridad controller) {
        super(parent, "Ingreso a la Central de Seguridad", true);
        this.controller = controller;

        setSize(380, 280);
        setLocationRelativeTo(parent);
        setResizable(false);
        getContentPane().setBackground(colorBg);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(colorBg);
        mainPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel title = new JLabel("ACCESO A LA CENTRAL");
        title.setFont(new Font("Outfit", Font.BOLD, 18));
        title.setForeground(colorTextPrimary);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(title);
        mainPanel.add(Box.createVerticalStrut(15));

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 8, 12));
        formPanel.setBackground(colorBg);

        JLabel lblUsuarioOperador = new JLabel("Operador:");
        lblUsuarioOperador.setFont(new Font("Outfit", Font.BOLD, 13));
        lblUsuarioOperador.setForeground(colorTextSecondary);
        JTextField txtUsuarioOperador = new JTextField("Oficial_Vargas");
        txtUsuarioOperador.setBackground(colorCard);
        txtUsuarioOperador.setForeground(colorTextPrimary);
        txtUsuarioOperador.setCaretColor(Color.WHITE);
        txtUsuarioOperador.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 70)),
            BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));

        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setFont(new Font("Outfit", Font.BOLD, 13));
        lblPass.setForeground(colorTextSecondary);
        JPasswordField txtPass = new JPasswordField("admin");
        txtPass.setBackground(colorCard);
        txtPass.setForeground(colorTextPrimary);
        txtPass.setCaretColor(Color.WHITE);
        txtPass.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 70)),
            BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));

        formPanel.add(lblUsuarioOperador);
        formPanel.add(txtUsuarioOperador);
        formPanel.add(lblPass);
        formPanel.add(txtPass);
        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(20));

        JButton btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setBackground(colorAccent);
        btnLogin.setForeground(colorTextPrimary);
        btnLogin.setFont(new Font("Outfit", Font.BOLD, 13));
        btnLogin.setBorderPainted(false);
        btnLogin.setFocusPainted(false);
        btnLogin.setPreferredSize(new Dimension(140, 32));
        btnLogin.setMaximumSize(new Dimension(140, 32));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnLogin.addActionListener(e -> {
            String user = txtUsuarioOperador.getText().trim();
            String pass = new String(txtPass.getPassword());

            if ("Oficial_Vargas".equalsIgnoreCase(user) && "admin".equals(pass)) {
                controller.switchUsuarioOperador("Oficial_Vargas", UsuarioOperador.Role.ADMIN);
                authenticated = true;
                dispose();
            } else if ("Guardia_Auxiliar".equalsIgnoreCase(user) && "guest".equals(pass)) {
                controller.switchUsuarioOperador("Guardia_Auxiliar", UsuarioOperador.Role.GUEST);
                authenticated = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Credenciales incorrectas.\nUse Oficial_Vargas/admin (ADMIN) o Guardia_Auxiliar/guest (INVITADO)", 
                    "Error de Acceso", JOptionPane.ERROR_MESSAGE);
            }
        });

        mainPanel.add(btnLogin);
        add(mainPanel, BorderLayout.CENTER);
    }

    public boolean isAuthenticated() {
        return authenticated;
    }
}
