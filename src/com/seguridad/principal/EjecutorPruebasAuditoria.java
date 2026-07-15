package com.seguridad.principal;

import com.seguridad.modelo.*;
import com.seguridad.patrones.creacionales.*;
import com.seguridad.patrones.estructurales.*;
import com.seguridad.patrones.comportamiento.*;
import com.seguridad.persistencia.RepositorioDispositivosCSV;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

// Ejecutor automatizado de pruebas unitarias y de integración
public class EjecutorPruebasAuditoria {
    private static int testsPassed = 0;
    private static int totalTests = 0;

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("   INICIANDO AUDITORÍA Y TESTING AUTOMATIZADO     ");
        System.out.println("==================================================");

        try {
            testSingleton();
            testFactoryMethod();
            testBuilderAndCommand();
            testAdapter();
            testProxy();
            testDecorator();
            testObserver();
            testState();
            testCrudAndPersistence();

            System.out.println("\n==================================================");
            System.out.println("   RESULTADO FINAL: " + testsPassed + "/" + totalTests + " PRUEBAS EXITOSAS");
            System.out.println("==================================================");
        } catch (Exception e) {
            System.err.println("\n[FALLO FATAL EN TESTING] Excepción: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void verify(String testName, boolean condition) {
        totalTests++;
        if (condition) {
            testsPassed++;
            System.out.println("  [OK] " + testName);
        } else {
            System.err.println("  [ERROR] " + testName);
        }
    }

    private static void testSingleton() {
        System.out.println("\n[TEST] Patrón Singleton:");
        CentralSeguridadSingleton hub1 = CentralSeguridadSingleton.getInstance();
        CentralSeguridadSingleton hub2 = CentralSeguridadSingleton.getInstance();
        verify("Misma referencia de CentralSeguridadSingleton", hub1 == hub2);
        verify("Instancia no nula", hub1 != null);
    }

    private static void testFactoryMethod() {
        System.out.println("\n[TEST] Patrón Factory Method:");
        FabricaDispositivos floodlightFactory = new FabricaReflectores();
        FabricaDispositivos fireDetectorFactory = new FabricaDetectoresHumo();
        FabricaDispositivos surveillanceFactory = new FabricaVigilancia("Lock");

        DispositivoSeguridadBase light = floodlightFactory.createDevice("RF-Test", "Reflector Test", "Exterior");
        DispositivoSeguridadBase thermo = fireDetectorFactory.createDevice("SD-Test", "Humo Test", "Bóveda");
        DispositivoSeguridadBase lock = surveillanceFactory.createDevice("LK-Test", "Cerradura Test", "Entrada");

        verify("Factory crea ReflectorSeguridad correctamente", light instanceof ReflectorSeguridad);
        verify("Factory crea DetectorHumoIncendio correctamente", thermo instanceof DetectorHumoIncendio);
        verify("Factory crea CerraduraElectronica correctamente", lock instanceof CerraduraElectronica);
        verify("Atributo nombre coincide", "Reflector Test".equals(light.getName()));
    }

    private static void testBuilderAndCommand() {
        System.out.println("\n[TEST] Patrones Builder y Command:");
        ReflectorSeguridad floodlight = new ReflectorSeguridad("RF-Builder", "Reflector Builder", "Exterior");
        DetectorHumoIncendio detector = new DetectorHumoIncendio("SD-Builder", "Incendio Builder", "Almacén");

        PerfilSeguridad profile = new ConstructorPerfilSeguridad("Perfil Test")
                .adjustFloodlight(floodlight, true, 95)
                .adjustFireThreshold(detector, true, 60.0)
                .build();

        verify("Builder crea el perfil correctamente", profile != null);
        verify("El perfil contiene 2 comandos", profile.getCommands().size() == 2);

        // Ejecutar comandos
        profile.execute();
        verify("Comando Floodlight ejecutó ON", floodlight.isOn());
        verify("Comando Floodlight aplicó intensidad 95%", floodlight.getIntensity() == 95);
        verify("Comando Detector ejecutó ON", detector.isOn());
        verify("Comando Detector aplicó límite de alarma 60.0°C", Math.abs(detector.getThresholdTemperature() - 60.0) < 0.01);
    }

    private static void testAdapter() {
        System.out.println("\n[TEST] Patrón Adapter:");
        SensorAntiguoAnalogico legacySensor = new SensorAntiguoAnalogico();
        AdaptadorSensorAntiguo adapter = new AdaptadorSensorAntiguo("S-Legacy", "Sensor Infrarrojo", "Cocina", legacySensor);

        verify("Adapter inicialmente inactivo (0V)", !adapter.isOn());
        
        // Simular cambio de voltaje
        legacySensor.simulateSignalChange(5);
        verify("Adapter detecta cambio de señal a 5V como ON", adapter.isOn());

        adapter.setOn(false);
        verify("Adapter setOn(false) reduce voltaje en legacy device a 0V", legacySensor.readPinVoltage() == 0);
    }

    private static void testProxy() {
        System.out.println("\n[TEST] Patrón Proxy:");
        CamaraVigilancia realCamera = new CamaraVigilancia("C-Real", "Cámara Real", "Entrada");
        ProxyCamaraSeguridad proxy = new ProxyCamaraSeguridad(realCamera);

        UsuarioOperador admin = new UsuarioOperador("oficial_admin", UsuarioOperador.Role.ADMIN);
        UsuarioOperador guest = new UsuarioOperador("guardia_guest", UsuarioOperador.Role.GUEST);

        // Admin debe tener acceso
        try {
            String feed = proxy.getLiveVideoStream(admin);
            verify("Proxy permite acceso a ADMIN", feed.contains("STREAM"));
        } catch (SecurityException e) {
            verify("Proxy permite acceso a ADMIN", false);
        }

        // Invitado debe ser bloqueado
        try {
            proxy.getLiveVideoStream(guest);
            verify("Proxy bloquea acceso a GUEST", false);
        } catch (SecurityException e) {
            verify("Proxy bloquea acceso a GUEST", true);
        }
    }

    private static void testDecorator() {
        System.out.println("\n[TEST] Patrón Decorator:");
        ReflectorSeguridad light = new ReflectorSeguridad("RF-Decorated", "Reflector Decorado", "Sala");
        DecoradorAuditorEnergia decorator = new DecoradorAuditorEnergia(light);

        verify("Decorator delega isOn() correctamente", !decorator.isOn());
        
        // Activar y verificar consumo
        decorator.setOn(true);
        verify("Decorator delega setOn()", light.isOn());
        verify("Decorator añade historial de consumo", decorator.getConsumptionHistory().size() > 0);
        verify("Decorator altera getStatusSummary()", decorator.getStatusSummary().contains("Respaldo & Consumo"));
    }

    private static void testObserver() {
        System.out.println("\n[TEST] Patrón Observer:");
        ReflectorSeguridad light = new ReflectorSeguridad("RF-Obs", "Reflector Obs", "Sala");
        final boolean[] notified = {false};

        ObservadorSeguridad mockObserver = (device, details) -> notified[0] = true;

        light.addObserver(mockObserver);
        light.setIntensity(45); // Debe notificar

        verify("Observer fue notificado del cambio de intensidad", notified[0]);
    }

    private static void testState() {
        System.out.println("\n[TEST] Patrón State (Cerrojo Electrónico):");
        CerraduraElectronica lock = new CerraduraElectronica("LK-Lock", "Cerradura State", "Entrada");

        verify("Estado inicial es Bloqueado", "Bloqueado".equals(lock.getStatusSummary()));

        // Desbloquear
        lock.unlock();
        verify("Estado cambia a Desbloqueado", "Desbloqueado".equals(lock.getStatusSummary()));

        // Disparar alarma
        lock.triggerAlarm();
        verify("Estado cambia a ¡ALARMA DISPARADA!", "¡ALARMA DISPARADA!".equals(lock.getStatusSummary()));

        // Acciones normales bloqueadas bajo alarma
        lock.unlock(); // Debe ser ignorada
        verify("Acción desbloquear bloqueada durante alarma", "¡ALARMA DISPARADA!".equals(lock.getStatusSummary()));

        // Restablecer alarma
        lock.resetAlarm();
        verify("Restablecer alarma vuelve al estado Bloqueado", "Bloqueado".equals(lock.getStatusSummary()));
    }

    private static void testCrudAndPersistence() {
        System.out.println("\n[TEST] CRUD & Persistencia (RepositorioDispositivosCSV):");
        String testFile = "test_security_devices.csv";
        RepositorioDispositivosCSV testRepo = new RepositorioDispositivosCSV(testFile);

        List<DispositivoSeguridadBase> originalList = new ArrayList<>();
        originalList.add(new ReflectorSeguridad("CRUD-RF", "Reflector CRUD", "Baño"));
        originalList.add(new DetectorHumoIncendio("CRUD-SD", "Sensor Humo CRUD", "Comedor"));

        // Guardar
        testRepo.saveDevices(originalList);
        verify("Archivo de prueba guardado con éxito", new File(testFile).exists());

        // Cargar
        List<DispositivoSeguridadBase> loadedList = testRepo.loadDevices();
        verify("Lista cargada contiene la cantidad correcta de elementos", loadedList.size() == 2);
        verify("Elemento 1 ID coincide", "CRUD-RF".equals(loadedList.get(0).getId()));
        verify("Elemento 1 Tipo coincide", "Floodlight".equals(loadedList.get(0).getType()));

        // Limpiar archivo temporal
        new File(testFile).delete();
    }
}
