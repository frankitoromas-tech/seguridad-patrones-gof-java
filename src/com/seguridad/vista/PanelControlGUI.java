package com.seguridad.vista;

import com.seguridad.controlador.ControladorSeguridad;
import com.seguridad.modelo.*;
import com.seguridad.patrones.estructurales.DecoradorAuditorEnergia;
import com.seguridad.patrones.estructurales.ProxyCamaraSeguridad;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

// Panel de control Swing para la central de seguridad
public class PanelControlGUI extends JFrame {
    private final ControladorSeguridad controller;
    
    // Componentes gráficos
    private JPanel gridPanel;
    private JTextArea txtLogs;
    private JLabel lblActiveDevicesVal;
    private JLabel lblEnergyVal;
    private JLabel lblAlarmsVal;
    private JLabel lblUsuarioOperadorVal;
    private JTextField txtSearch;
    private String searchQuery = "";

    // Paleta de colores oscuros
    private final Color colorBg = new Color(20, 20, 25);
    private final Color colorCard = new Color(32, 32, 42);
    private final Color colorSidebar = new Color(26, 26, 34);
    private final Color colorAccent = new Color(79, 110, 242);
    private final Color colorAccentHover = new Color(99, 127, 245);
    private final Color colorDanger = new Color(235, 87, 87);
    private final Color colorSuccess = new Color(39, 174, 96);
    private final Color colorTextPrimary = Color.WHITE;
    private final Color colorTextSecondary = new Color(170, 170, 185);

    public PanelControlGUI(ControladorSeguridad controller) {
        this.controller = controller;
        
        setTitle("Central de Seguridad Ciudadana y Vigilancia");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(colorBg);
        setLayout(new BorderLayout());

        // Panel de cabecera
        add(createHeaderPanel(), BorderLayout.NORTH);

        // Panel lateral
        add(createSidebarPanel(), BorderLayout.WEST);

        // Contenedor principal y rejilla de dispositivos
        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setBackground(colorBg);
        centerContainer.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel topControls = new JPanel(new BorderLayout());
        topControls.setBackground(colorBg);
        topControls.add(createAddDeviceForm(), BorderLayout.NORTH);
        topControls.add(createSearchPanel(), BorderLayout.SOUTH);
        centerContainer.add(topControls, BorderLayout.NORTH);

        gridPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        gridPanel.setBackground(colorBg);
        
        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(colorBg);
        centerContainer.add(scrollPane, BorderLayout.CENTER);

        add(centerContainer, BorderLayout.CENTER);

        // Panel de registro de eventos
        add(createLogsPanel(), BorderLayout.SOUTH);

        refreshUI();
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(colorSidebar);
        panel.setPreferredSize(new Dimension(1200, 65));
        panel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel title = new JLabel("CENTRAL DE SEGURIDAD");
        title.setFont(new Font("Outfit", Font.BOLD, 22));
        title.setForeground(colorTextPrimary);
        
        JLabel subtitle = new JLabel(" • Monitoreo Urbano & Videovigilancia");
        subtitle.setFont(new Font("Outfit", Font.PLAIN, 14));
        subtitle.setForeground(colorTextSecondary);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 8));
        titlePanel.setBackground(colorSidebar);
        titlePanel.add(title);
        titlePanel.add(subtitle);

        panel.add(titlePanel, BorderLayout.WEST);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        userPanel.setBackground(colorSidebar);

        lblUsuarioOperadorVal = new JLabel("Operador: Vargas (ADMIN)");
        lblUsuarioOperadorVal.setFont(new Font("Outfit", Font.BOLD, 14));
        lblUsuarioOperadorVal.setForeground(colorTextPrimary);
        userPanel.add(lblUsuarioOperadorVal);

        JButton btnLogout = createStyledButton("Cerrar Sesión", colorDanger);
        btnLogout.addActionListener(e -> {
            dispose();
            DialogoAcceso login = new DialogoAcceso(null, controller);
            login.setVisible(true);
            if (login.isAuthenticated()) {
                new PanelControlGUI(controller).setVisible(true);
            } else {
                System.exit(0);
            }
        });
        userPanel.add(btnLogout);

        panel.add(userPanel, BorderLayout.EAST);
        return panel;
    }

    private JPanel createSidebarPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(colorSidebar);
        panel.setPreferredSize(new Dimension(280, 800));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleMetrics = new JLabel("MÉTRICAS DE SURVEILLANCE");
        titleMetrics.setFont(new Font("Outfit", Font.BOLD, 14));
        titleMetrics.setForeground(colorTextSecondary);
        titleMetrics.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titleMetrics);
        panel.add(Box.createVerticalStrut(15));

        JPanel metricsContainer = new JPanel(new GridLayout(3, 1, 10, 10));
        metricsContainer.setBackground(colorSidebar);
        metricsContainer.setMaximumSize(new Dimension(240, 200));

        lblActiveDevicesVal = createMetricLabel("Sensores Operativos: 0");
        lblEnergyVal = createMetricLabel("Carga Central: 0.000 kW");
        lblAlarmsVal = createMetricLabel("Estados de Alerta: Ninguno");

        metricsContainer.add(lblActiveDevicesVal);
        metricsContainer.add(lblEnergyVal);
        metricsContainer.add(lblAlarmsVal);
        metricsContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(metricsContainer);

        panel.add(Box.createVerticalStrut(30));

        JLabel titleRoutines = new JLabel("AUTOMATIZACIÓN [Builder + Command]");
        titleRoutines.setFont(new Font("Outfit", Font.BOLD, 14));
        titleRoutines.setForeground(colorTextSecondary);
        titleRoutines.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titleRoutines);
        panel.add(Box.createVerticalStrut(15));

        JPanel routinePanel = new JPanel(new GridLayout(3, 1, 10, 10));
        routinePanel.setBackground(colorSidebar);
        routinePanel.setMaximumSize(new Dimension(240, 150));

        JButton btnNight = createStyledButton("🛡️ Guardia Nocturna", colorCard);
        btnNight.setToolTipText("Patrón Builder & Command: Construye un perfil y ejecuta múltiples acciones sobre los dispositivos.");
        btnNight.addActionListener(e -> {
            controller.runRoutine("Guardia Nocturna");
            refreshUI();
        });
        
        JButton btnVacation = createStyledButton("🔒 Cierre Completo", colorCard);
        btnVacation.setToolTipText("Patrón Command: Ejecuta de forma encapsulada el bloqueo de todas las cerraduras y activación de sensores.");
        btnVacation.addActionListener(e -> {
            controller.runRoutine("Cierre Completo");
            refreshUI();
        });

        JButton btnWake = createStyledButton("🔓 Desarmar Sistema", colorCard);
        btnWake.setToolTipText("Patrón Command: Restablece el sistema a su estado normal.");
        btnWake.addActionListener(e -> {
            controller.runRoutine("Desarmar");
            refreshUI();
        });

        routinePanel.add(btnNight);
        routinePanel.add(btnVacation);
        routinePanel.add(btnWake);
        routinePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(routinePanel);

        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JPanel createAddDeviceForm() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panel.setBackground(colorCard);
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(50, 50, 60)));

        JLabel lblTitle = new JLabel("Registrar Dispositivo [Factory Method]:");
        lblTitle.setFont(new Font("Outfit", Font.BOLD, 13));
        lblTitle.setForeground(colorTextPrimary);
        panel.add(lblTitle);

        JTextField txtId = new JTextField(4);
        txtId.setBackground(colorBg);
        txtId.setForeground(colorTextPrimary);
        txtId.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 70)));
        txtId.setCaretColor(Color.WHITE);
        panel.add(new JLabel("ID:") {{ setForeground(colorTextSecondary); }});
        panel.add(txtId);

        JTextField txtName = new JTextField(10);
        txtName.setBackground(colorBg);
        txtName.setForeground(colorTextPrimary);
        txtName.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 70)));
        txtName.setCaretColor(Color.WHITE);
        panel.add(new JLabel("Nombre:") {{ setForeground(colorTextSecondary); }});
        panel.add(txtName);

        JTextField txtRoom = new JTextField(8);
        txtRoom.setBackground(colorBg);
        txtRoom.setForeground(colorTextPrimary);
        txtRoom.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 70)));
        txtRoom.setCaretColor(Color.WHITE);
        panel.add(new JLabel("Zona:") {{ setForeground(colorTextSecondary); }});
        panel.add(txtRoom);

        String[] types = {"Reflector", "Sensor Incendio", "Cámara", "Cerradura"};
        JComboBox<String> cmbType = new JComboBox<>(types);
        cmbType.setBackground(colorBg);
        cmbType.setForeground(colorTextPrimary);
        panel.add(new JLabel("Tipo:") {{ setForeground(colorTextSecondary); }});
        panel.add(cmbType);

        JButton btnAdd = createStyledButton("Registrar", colorAccent);
        btnAdd.setToolTipText("Patrón Factory Method: Delega la creación (new Object()) a una fábrica según el tipo que elijas.");
        btnAdd.addActionListener(e -> {
            String id = txtId.getText().trim();
            String name = txtName.getText().trim();
            String room = txtRoom.getText().trim();
            String selectedType = (String) cmbType.getSelectedItem();

            String type = "";
            if ("Reflector".equals(selectedType)) type = "floodlight";
            else if ("Sensor Incendio".equals(selectedType)) type = "firedetector";
            else if ("Cámara".equals(selectedType)) type = "camera";
            else if ("Cerradura".equals(selectedType)) type = "lock";

            try {
                controller.addDevice(id, name, room, type);
                txtId.setText("");
                txtName.setText("");
                txtRoom.setText("");
                refreshUI();
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Validación", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(btnAdd);

        return panel;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        panel.setBackground(colorBg);
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));

        JLabel lblSearch = new JLabel("🔍 Filtrar dispositivos (Nombre/Zona):");
        lblSearch.setFont(new Font("Outfit", Font.BOLD, 12));
        lblSearch.setForeground(colorTextSecondary);
        panel.add(lblSearch);

        txtSearch = new JTextField(20);
        txtSearch.setBackground(colorCard);
        txtSearch.setForeground(colorTextPrimary);
        txtSearch.setCaretColor(Color.WHITE);
        txtSearch.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 70)));
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateSearch(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateSearch(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateSearch(); }
            private void updateSearch() {
                searchQuery = txtSearch.getText().trim().toLowerCase();
                refreshUI();
            }
        });
        panel.add(txtSearch);

        return panel;
    }

    private JPanel createLogsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(colorSidebar);
        panel.setPreferredSize(new Dimension(1200, 160));
        panel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(colorSidebar);
        header.setBorder(new EmptyBorder(0, 0, 5, 0));

        JLabel title = new JLabel("CONSOLA DE EVENTOS [Patrón Observer]");
        title.setFont(new Font("Outfit", Font.BOLD, 13));
        title.setForeground(colorTextSecondary);
        header.add(title, BorderLayout.WEST);

        JButton btnExport = createStyledButton("📥 Exportar Reporte", colorAccent);
        btnExport.setPreferredSize(new Dimension(150, 25));
        btnExport.addActionListener(e -> {
            String path = controller.exportLogsReport();
            if (path != null) {
                JOptionPane.showMessageDialog(this, "Reporte exportado con éxito en:\n" + path, "Exportación Exitosa", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Ocurrió un error al exportar el reporte.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        header.add(btnExport, BorderLayout.EAST);

        panel.add(header, BorderLayout.NORTH);

        txtLogs = new JTextArea();
        txtLogs.setBackground(new Color(15, 15, 20));
        txtLogs.setForeground(new Color(120, 220, 120));
        txtLogs.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtLogs.setEditable(false);

        JScrollPane scroll = new JScrollPane(txtLogs);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(40, 40, 50)));
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private void refreshUI() {
        UsuarioOperador user = controller.getCurrentUsuarioOperador();
        lblUsuarioOperadorVal.setText("Operador: " + user.getUsuarioOperadorname() + " (" + user.getRole() + ")");

        gridPanel.removeAll();
        List<DispositivoSeguridadBase> devices = controller.getDevices();

        int activeCount = 0;
        double totalEnergy = 0.0;
        boolean alarmTriggered = false;

        for (DispositivoSeguridadBase device : devices) {
            if (searchQuery != null && !searchQuery.isEmpty()) {
                boolean matchesName = device.getName().toLowerCase().contains(searchQuery);
                boolean matchesRoom = device.getRoom().toLowerCase().contains(searchQuery);
                if (!matchesName && !matchesRoom) {
                    continue;
                }
            }

            if (device.isOn()) {
                activeCount++;
            }
            totalEnergy += device.getEnergyConsumption();

            if (device instanceof CerraduraElectronica) {
                if ("¡ALARMA DISPARADA!".equals(device.getStatusSummary())) {
                    alarmTriggered = true;
                }
            }

            gridPanel.add(createDeviceCard(device));
        }

        lblActiveDevicesVal.setText("Dispositivos Activos: " + activeCount);
        lblEnergyVal.setText("Consumo Central: " + String.format("%.3f", totalEnergy) + " kW/h");
        
        if (alarmTriggered) {
            lblAlarmsVal.setText("Estados de Alerta: ¡INTRUSIÓN DETECTADA!");
            lblAlarmsVal.setForeground(colorDanger);
        } else {
            lblAlarmsVal.setText("Estados de Alerta: Ninguno");
            lblAlarmsVal.setForeground(colorTextSecondary);
        }

        txtLogs.setText("");
        for (String log : controller.getSystemLogs()) {
            txtLogs.append(log + "\n");
        }
        txtLogs.setCaretPosition(txtLogs.getDocument().getLength());

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private JPanel createDeviceCard(DispositivoSeguridadBase device) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(colorCard);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 70), 1),
                new EmptyBorder(12, 12, 12, 12)
        ));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(colorCard);

        JLabel title = new JLabel(device.getName());
        title.setFont(new Font("Outfit", Font.BOLD, 15));
        title.setForeground(colorTextPrimary);
        header.add(title, BorderLayout.WEST);
        
        // Etiqueta didáctica para mostrar el patrón en la interfaz
        String patternTag = "";
        if (device instanceof CerraduraElectronica) patternTag = " [State]";
        else if (device instanceof CamaraVigilancia) patternTag = " [Proxy]";
        else if (device.getType().equals("LegacySensor")) patternTag = " [Adapter]";
        else if (device instanceof DecoradorAuditorEnergia) patternTag = " [Decorator]";

        JLabel room = new JLabel(device.getRoom() + " • " + device.getType() + patternTag);
        room.setFont(new Font("Outfit", Font.PLAIN, 12));
        room.setForeground(colorTextSecondary);
        header.add(room, BorderLayout.SOUTH);

        card.add(header, BorderLayout.NORTH);

        JPanel controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
        controls.setBackground(colorCard);
        controls.setBorder(new EmptyBorder(10, 0, 10, 0));

        JLabel status = new JLabel(device.getStatusSummary());
        status.setFont(new Font("Outfit", Font.BOLD, 13));
        status.setForeground(device.isOn() ? colorSuccess : colorTextSecondary);
        status.setAlignmentX(Component.LEFT_ALIGNMENT);
        controls.add(status);
        controls.add(Box.createVerticalStrut(10));

        if (device instanceof ReflectorSeguridad) {
            ReflectorSeguridad light = (ReflectorSeguridad) device;
            
            JButton btnToggle = createStyledButton(light.isOn() ? "Apagar" : "Encender", light.isOn() ? colorDanger : colorSuccess);
            btnToggle.setMaximumSize(new Dimension(100, 25));
            btnToggle.addActionListener(e -> {
                controller.toggleDevice(light.getId());
                refreshUI();
            });
            btnToggle.setAlignmentX(Component.LEFT_ALIGNMENT);
            controls.add(btnToggle);
            controls.add(Box.createVerticalStrut(8));

            JSlider slider = new JSlider(0, 100, light.getIntensity());
            slider.setBackground(colorCard);
            slider.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    controller.setFloodlightIntensity(light.getId(), slider.getValue());
                    refreshUI();
                }
            });
            slider.setAlignmentX(Component.LEFT_ALIGNMENT);
            controls.add(new JLabel("Intensidad:") {{ setForeground(colorTextSecondary); setAlignmentX(LEFT_ALIGNMENT); }});
            controls.add(slider);

        } else if (device instanceof DetectorHumoIncendio) {
            DetectorHumoIncendio term = (DetectorHumoIncendio) device;

            JButton btnToggle = createStyledButton(term.isOn() ? "Desactivar" : "Activar", term.isOn() ? colorDanger : colorSuccess);
            btnToggle.setMaximumSize(new Dimension(100, 25));
            btnToggle.addActionListener(e -> {
                controller.toggleDevice(term.getId());
                refreshUI();
            });
            btnToggle.setAlignmentX(Component.LEFT_ALIGNMENT);
            controls.add(btnToggle);
            controls.add(Box.createVerticalStrut(8));

            JPanel tempGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            tempGroup.setBackground(colorCard);
            
            JButton btnDec = createStyledButton("-", colorSidebar);
            btnDec.setPreferredSize(new Dimension(30, 25));
            btnDec.addActionListener(e -> {
                controller.setFireDetectorThreshold(term.getId(), term.getThresholdTemperature() - 1.0);
                refreshUI();
            });

            JButton btnInc = createStyledButton("+", colorSidebar);
            btnInc.setPreferredSize(new Dimension(30, 25));
            btnInc.addActionListener(e -> {
                controller.setFireDetectorThreshold(term.getId(), term.getThresholdTemperature() + 1.0);
                refreshUI();
            });

            tempGroup.add(btnDec);
            tempGroup.add(btnInc);
            tempGroup.setAlignmentX(Component.LEFT_ALIGNMENT);
            controls.add(tempGroup);

        } else if (device instanceof CamaraVigilancia) {
            CamaraVigilancia cam = (CamaraVigilancia) device;

            JButton btnToggle = createStyledButton(cam.isOn() ? "Apagar" : "Conectar", cam.isOn() ? colorDanger : colorSuccess);
            btnToggle.setMaximumSize(new Dimension(100, 25));
            btnToggle.addActionListener(e -> {
                controller.toggleDevice(cam.getId());
                refreshUI();
            });
            btnToggle.setAlignmentX(Component.LEFT_ALIGNMENT);
            controls.add(btnToggle);
            controls.add(Box.createVerticalStrut(8));

            if (cam.isOn()) {
                JButton btnRecord = createStyledButton(cam.isRecording() ? "Detener Grabación" : "Grabar CCTV", colorCard);
                btnRecord.setMaximumSize(new Dimension(150, 25));
                btnRecord.addActionListener(e -> {
                    controller.toggleCameraRecording(cam.getId());
                    refreshUI();
                });
                btnRecord.setAlignmentX(Component.LEFT_ALIGNMENT);
                controls.add(btnRecord);
                controls.add(Box.createVerticalStrut(8));
            }

            JButton btnStream = createStyledButton("🎬 Ver Streaming (Proxy)", colorAccent);
            btnStream.setMaximumSize(new Dimension(180, 25));
            btnStream.setToolTipText("Patrón Proxy: Intercepta tu petición para verificar tu nivel de acceso antes de cargar la cámara real.");
            btnStream.addActionListener(e -> {
                ProxyCamaraSeguridad proxy = new ProxyCamaraSeguridad(cam);
                try {
                    String streamFeed = proxy.getLiveVideoStream(controller.getCurrentUsuarioOperador());
                    JOptionPane.showMessageDialog(this, streamFeed, "Streaming: " + cam.getName(), JOptionPane.INFORMATION_MESSAGE);
                } catch (SecurityException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Acceso Denegado (Proxy)", JOptionPane.ERROR_MESSAGE);
                }
            });
            btnStream.setAlignmentX(Component.LEFT_ALIGNMENT);
            controls.add(btnStream);

        } else if (device instanceof CerraduraElectronica) {
            CerraduraElectronica lock = (CerraduraElectronica) device;
            String stateStr = lock.getStatusSummary();
            
            if (!"¡ALARMA DISPARADA!".equals(stateStr)) {
                JButton btnToggle = createStyledButton("Bloqueado".equals(stateStr) ? "Desbloquear" : "Bloquear", 
                        "Bloqueado".equals(stateStr) ? colorSuccess : colorDanger);
                btnToggle.setMaximumSize(new Dimension(120, 25));
                btnToggle.setToolTipText("Patrón State: Delega la lógica de bloqueo/desbloqueo al objeto de estado interno.");
                btnToggle.addActionListener(e -> {
                    controller.toggleLock(lock.getId());
                    refreshUI();
                });
                btnToggle.setAlignmentX(Component.LEFT_ALIGNMENT);
                controls.add(btnToggle);
                controls.add(Box.createVerticalStrut(8));

                JButton btnTrigger = createStyledButton("Simular Intruso (Alarma)", colorDanger);
                btnTrigger.setMaximumSize(new Dimension(180, 25));
                btnTrigger.addActionListener(e -> {
                    controller.triggerLockAlarm(lock.getId());
                    refreshUI();
                });
                btnTrigger.setAlignmentX(Component.LEFT_ALIGNMENT);
                controls.add(btnTrigger);
            } else {
                JButton btnReset = createStyledButton("Silenciar Sirena", colorSuccess);
                btnReset.setMaximumSize(new Dimension(150, 25));
                btnReset.addActionListener(e -> {
                    controller.resetLockAlarm(lock.getId());
                    refreshUI();
                });
                btnReset.setAlignmentX(Component.LEFT_ALIGNMENT);
                controls.add(btnReset);
            }
        } else if (device.getType().equals("LegacySensor")) {
            JButton btnSignal = createStyledButton(device.isOn() ? "Establecer Normal" : "Simular Movimiento", device.isOn() ? colorDanger : colorSuccess);
            btnSignal.setMaximumSize(new Dimension(150, 25));
            btnSignal.setToolTipText("Patrón Adapter: Traduce esta interacción hacia los métodos del sensor antiguo que tiene una interfaz incompatible.");
            btnSignal.addActionListener(e -> {
                device.setOn(!device.isOn());
                refreshUI();
            });
            btnSignal.setAlignmentX(Component.LEFT_ALIGNMENT);
            controls.add(btnSignal);
        }

        card.add(controls, BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(colorCard);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(50, 50, 60)));

        boolean isDecorated = (device instanceof DecoradorAuditorEnergia);
        JButton btnDecorate = createStyledButton(isDecorated ? "Decorado (Auditor)" : "Auditar Consumo", isDecorated ? colorSuccess : colorSidebar);
        btnDecorate.setPreferredSize(new Dimension(140, 25));
        btnDecorate.setToolTipText("Patrón Decorator: Envuelve el objeto actual con una nueva capa para auditar energía sin alterar el código original.");
        btnDecorate.addActionListener(e -> {
            if (!isDecorated) {
                List<DispositivoSeguridadBase> devList = controller.getDevices();
                for (int i = 0; i < devList.size(); i++) {
                    if (devList.get(i).getId().equals(device.getId())) {
                        DispositivoSeguridadBase wrapped = new DecoradorAuditorEnergia(devList.get(i));
                        devList.set(i, wrapped);
                        break;
                    }
                }
                controller.saveState();
                refreshUI();
            }
        });
        footer.add(btnDecorate, BorderLayout.WEST);

        JButton btnDelete = createStyledButton("Eliminar", colorDanger);
        btnDelete.setPreferredSize(new Dimension(80, 25));
        btnDelete.addActionListener(e -> {
            int resp = JOptionPane.showConfirmDialog(this, "¿Eliminar dispositivo " + device.getName() + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (resp == JOptionPane.YES_OPTION) {
                controller.removeDevice(device.getId());
                refreshUI();
            }
        });
        footer.add(btnDelete, BorderLayout.EAST);

        card.add(footer, BorderLayout.SOUTH);

        return card;
    }

    private JLabel createMetricLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Outfit", Font.PLAIN, 13));
        label.setForeground(colorTextSecondary);
        return label;
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(colorTextPrimary);
        btn.setFont(new Font("Outfit", Font.BOLD, 12));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (bg == colorAccent) btn.setBackground(colorAccentHover);
                else btn.setBackground(bg.brighter());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg);
            }
        });
        return btn;
    }
}
