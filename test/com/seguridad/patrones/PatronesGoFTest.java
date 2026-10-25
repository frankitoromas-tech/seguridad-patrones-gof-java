package com.seguridad.patrones;

import com.seguridad.modelo.CamaraVigilancia;
import com.seguridad.modelo.CerraduraElectronica;
import com.seguridad.modelo.DetectorHumoIncendio;
import com.seguridad.modelo.DispositivoSeguridadBase;
import com.seguridad.modelo.ReflectorSeguridad;
import com.seguridad.modelo.UsuarioOperador;
import com.seguridad.patrones.comportamiento.ComandoAccionSeguridad;
import com.seguridad.patrones.comportamiento.ComandoDispositivo;
import com.seguridad.patrones.comportamiento.EstadoAlarmaActivada;
import com.seguridad.patrones.comportamiento.EstadoBloqueado;
import com.seguridad.patrones.comportamiento.EstadoDesbloqueado;
import com.seguridad.patrones.comportamiento.ObservadorSeguridad;
import com.seguridad.patrones.creacionales.CentralSeguridadSingleton;
import com.seguridad.patrones.creacionales.ConstructorPerfilSeguridad;
import com.seguridad.patrones.creacionales.FabricaDetectoresHumo;
import com.seguridad.patrones.creacionales.FabricaDispositivos;
import com.seguridad.patrones.creacionales.FabricaReflectores;
import com.seguridad.patrones.creacionales.FabricaVigilancia;
import com.seguridad.patrones.creacionales.PerfilSeguridad;
import com.seguridad.patrones.estructurales.AdaptadorSensorAntiguo;
import com.seguridad.patrones.estructurales.DecoradorAuditorEnergia;
import com.seguridad.patrones.estructurales.ProxyCamaraSeguridad;
import com.seguridad.patrones.estructurales.SensorAntiguoAnalogico;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Suite Integral de Pruebas Automatizadas de 8 Patrones GoF (Java 17 / JUnit 5 / AssertJ)")
class PatronesGoFTest {

    // ==========================================
    // 1. PATRÓN SINGLETON
    // ==========================================
    @Test
    @DisplayName("1. Singleton: Garantiza instancia única y registro central de dispositivos")
    void testCentralSeguridadSingleton() {
        CentralSeguridadSingleton instancia1 = CentralSeguridadSingleton.getInstance();
        CentralSeguridadSingleton instancia2 = CentralSeguridadSingleton.getInstance();

        assertThat(instancia1)
                .as("La instancia 1 de CentralSeguridadSingleton no debe ser nula")
                .isNotNull();
        assertThat(instancia2)
                .as("La instancia 2 de CentralSeguridadSingleton no debe ser nula")
                .isNotNull();
        assertThat(instancia1)
                .as("Ambas referencias deben apuntar exactamente al mismo objeto en memoria")
                .isSameAs(instancia2);

        // Registro de dispositivo en la central
        CamaraVigilancia cam = new CamaraVigilancia("CAM-SGT", "Cámara Singleton", "Garita");
        instancia1.addDevice(cam);
        DispositivoSeguridadBase hallado = instancia2.findDevice("CAM-SGT");

        assertThat(hallado).isNotNull();
        assertThat(hallado.getName()).isEqualTo("Cámara Singleton");

        // Retiro seguro del dispositivo
        boolean retirado = instancia1.removeDevice("CAM-SGT");
        assertThat(retirado).isTrue();
        assertThat(instancia2.findDevice("CAM-SGT")).isNull();
    }

    // ==========================================
    // 2. PATRÓN FACTORY METHOD
    // ==========================================
    @Test
    @DisplayName("2. Factory Method: FabricaDispositivos desacopla la instanciación de hardware")
    void testFactoryMethodDispositivos() {
        // Fábrica de Reflectores
        FabricaDispositivos fabricaReflector = new FabricaReflectores();
        DispositivoSeguridadBase reflector = fabricaReflector.createDevice("RF-01", "Reflector Perimetral", "Jardín Norte");
        assertThat(reflector)
                .isInstanceOf(ReflectorSeguridad.class);
        assertThat(reflector.getType()).isEqualTo("Floodlight");

        // Fábrica de Detectores de Humo
        FabricaDispositivos fabricaHumo = new FabricaDetectoresHumo();
        DispositivoSeguridadBase detectorHumo = fabricaHumo.createDevice("SMK-01", "Detector Iónico", "Centro de Cómputo");
        assertThat(detectorHumo)
                .isInstanceOf(DetectorHumoIncendio.class);
        assertThat(detectorHumo.getType()).isEqualTo("FireDetector");

        // Fábrica de Vigilancia - Cámara
        FabricaDispositivos fabricaCamara = new FabricaVigilancia("Camera");
        DispositivoSeguridadBase camara = fabricaCamara.createDevice("CAM-01", "Cámara Domo PTZ", "Recepción");
        assertThat(camara)
                .isInstanceOf(CamaraVigilancia.class);
        assertThat(camara.getType()).isEqualTo("Camera");

        // Fábrica de Vigilancia - Cerradura
        FabricaDispositivos fabricaCerrojo = new FabricaVigilancia("Lock");
        DispositivoSeguridadBase cerrojo = fabricaCerrojo.createDevice("LK-01", "Cerradura Biométrica", "Bóveda");
        assertThat(cerrojo)
                .isInstanceOf(CerraduraElectronica.class);
        assertThat(cerrojo.getType()).isEqualTo("Lock");
    }

    // ==========================================
    // 3. PATRÓN BUILDER
    // ==========================================
    @Test
    @DisplayName("3. Builder: Construcción paso a paso y fluida de perfiles de automatización")
    void testBuilderPerfilSeguridad() {
        ReflectorSeguridad reflector = new ReflectorSeguridad("RF-BLD", "Reflector Bld", "Zona A");
        DetectorHumoIncendio detector = new DetectorHumoIncendio("SD-BLD", "Sensor Bld", "Zona B");
        CerraduraElectronica cerradura = new CerraduraElectronica("LK-BLD", "Cerradura Bld", "Acceso 1");

        ConstructorPerfilSeguridad builder = new ConstructorPerfilSeguridad("Perfil Noche Cerrada");
        PerfilSeguridad perfil = builder
                .adjustFloodlight(reflector, true, 85)
                .adjustFireThreshold(detector, true, 65.0)
                .toggleDevice(cerradura, true)
                .build();

        assertThat(perfil).isNotNull();
        assertThat(perfil.getName()).isEqualTo("Perfil Noche Cerrada");
        assertThat(perfil.getCommands()).hasSize(3);

        // Ejecución en lote de la macro construida
        perfil.execute();

        assertThat(reflector.isOn()).isTrue();
        assertThat(reflector.getIntensity()).isEqualTo(85);
        assertThat(detector.isOn()).isTrue();
        assertThat(detector.getThresholdTemperature()).isEqualTo(65.0);
        assertThat(cerradura.getStatusSummary()).isEqualTo("Bloqueado");
    }

    // ==========================================
    // 4. PATRÓN ADAPTER
    // ==========================================
    @Test
    @DisplayName("4. Adapter: AdaptadorSensorAntiguo integra hardware analógico legado")
    void testAdapterSensorAntiguo() {
        SensorAntiguoAnalogico hardwareLegado = new SensorAntiguoAnalogico();
        AdaptadorSensorAntiguo adaptador = new AdaptadorSensorAntiguo("PIR-LEGACY", "Sensor Infrarrojo PIR", "Almacén", hardwareLegado);

        assertThat(adaptador.getType()).isEqualTo("LegacySensor");
        assertThat(hardwareLegado.readPinVoltage()).isZero();
        assertThat(adaptador.isOn()).isFalse();
        assertThat(adaptador.getStatusSummary()).isEqualTo("Sensor Listo/Ok");

        // Simular activación física de voltaje analógico a 5V en hardware heredado
        hardwareLegado.simulateSignalChange(5);
        assertThat(adaptador.isOn()).isTrue();
        assertThat(adaptador.getStatusSummary()).isEqualTo("Sensor Activo (Alarma)");
        assertThat(adaptador.getEnergyConsumption()).isEqualTo(0.001);

        // Apagado desde interfaz estándar del sistema
        adaptador.setOn(false);
        assertThat(hardwareLegado.readPinVoltage()).isZero();
        assertThat(adaptador.isOn()).isFalse();
    }

    // ==========================================
    // 5. PATRÓN DECORATOR
    // ==========================================
    @Test
    @DisplayName("5. Decorator: DecoradorAuditorEnergia extiende métricas sin mutar la clase base")
    void testDecoratorAuditorEnergia() {
        ReflectorSeguridad reflector = new ReflectorSeguridad("RF-DEC", "Reflector Patio", "Exterior");
        reflector.setIntensity(100);
        DecoradorAuditorEnergia decorador = new DecoradorAuditorEnergia(reflector);

        assertThat(decorador.getId()).isEqualTo("RF-DEC");
        assertThat(decorador.getName()).isEqualTo("Reflector Patio");
        assertThat(decorador.isOn()).isFalse();

        // Al activar mediante decorador, delega y registra telemetría
        decorador.setOn(true);
        assertThat(reflector.isOn()).isTrue();
        assertThat(decorador.isOn()).isTrue();
        assertThat(decorador.getConsumptionHistory()).isNotEmpty();

        // Muestrear consumos adicionales para verificar promedio e historial
        decorador.trackConsumption();
        decorador.trackConsumption();

        assertThat(decorador.getAverageConsumption()).isGreaterThan(0.0);
        assertThat(decorador.getStatusSummary()).contains("Respaldo & Consumo:");
    }

    // ==========================================
    // 6. PATRÓN PROXY
    // ==========================================
    @Test
    @DisplayName("6. Proxy: ProxyCamaraSeguridad restringe acceso a streams con control RBAC")
    void testProxyCamaraSeguridad() {
        CamaraVigilancia camara = new CamaraVigilancia("CAM-PRX", "Cámara Alta Seguridad", "Bóveda Central");
        ProxyCamaraSeguridad proxy = new ProxyCamaraSeguridad(camara);

        UsuarioOperador admin = new UsuarioOperador("operador_admin", UsuarioOperador.Role.ADMIN);
        UsuarioOperador operator = new UsuarioOperador("operador_turno", UsuarioOperador.Role.OPERATOR);
        UsuarioOperador guest = new UsuarioOperador("visitante_anon", UsuarioOperador.Role.GUEST);

        // Acceso permitido para ADMIN y OPERATOR
        String streamAdmin = proxy.getLiveVideoStream(admin);
        assertThat(streamAdmin)
                .isNotNull()
                .contains("STREAM SEGURIDAD")
                .contains("Bóveda Central");

        String streamOperator = proxy.getLiveVideoStream(operator);
        assertThat(streamOperator)
                .isNotNull()
                .contains("STREAM SEGURIDAD");

        // Acceso denegado para GUEST con SecurityException
        assertThatThrownBy(() -> proxy.getLiveVideoStream(guest))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("Acceso denegado: El rol GUEST carece de permisos de videovigilancia.");
    }

    // ==========================================
    // 7. PATRÓN COMMAND
    // ==========================================
    @Test
    @DisplayName("7. Command: ComandoAccionSeguridad encapsula peticiones de forma polimórfica")
    void testCommandAccionSeguridad() {
        ReflectorSeguridad reflector = new ReflectorSeguridad("RF-CMD", "Reflector Calle", "Perímetro");
        CerraduraElectronica cerradura = new CerraduraElectronica("LK-CMD", "Cerradura Calle", "Perímetro");

        ComandoDispositivo comandoLuz = new ComandoAccionSeguridad(reflector, true, 75);
        ComandoDispositivo comandoDesbloqueo = new ComandoAccionSeguridad(cerradura, false);

        assertThat(comandoLuz.getDescription()).contains("Activar (Intensidad: 75%)");
        assertThat(comandoDesbloqueo.getDescription()).contains("Desbloquear");

        // Ejecución desacoplada
        comandoLuz.execute();
        comandoDesbloqueo.execute();

        assertThat(reflector.isOn()).isTrue();
        assertThat(reflector.getIntensity()).isEqualTo(75);
        assertThat(cerradura.getStatusSummary()).isEqualTo("Desbloqueado");
    }

    // ==========================================
    // 8. PATRÓN STATE
    // ==========================================
    @Test
    @DisplayName("8. State: EstadoCerradura gestiona transiciones entre Bloqueado, Desbloqueado y Alarma")
    void testStateCerraduraElectronica() {
        CerraduraElectronica cerradura = new CerraduraElectronica("LK-ST", "Cerradura Inteligente", "Puerta Principal");

        // Estado inicial por defecto: EstadoBloqueado
        assertThat(cerradura.getState()).isInstanceOf(EstadoBloqueado.class);
        assertThat(cerradura.getStatusSummary()).isEqualTo("Bloqueado");

        // Transición: Desbloquear
        cerradura.unlock();
        assertThat(cerradura.getState()).isInstanceOf(EstadoDesbloqueado.class);
        assertThat(cerradura.getStatusSummary()).isEqualTo("Desbloqueado");

        // Transición: Disparar Alarma de Intrusión
        cerradura.triggerAlarm();
        assertThat(cerradura.getState()).isInstanceOf(EstadoAlarmaActivada.class);
        assertThat(cerradura.getStatusSummary()).isEqualTo("¡ALARMA DISPARADA!");
        assertThat(cerradura.getEnergyConsumption()).isEqualTo(0.15);

        // Durante el estado de alarma, los comandos de desbloqueo normal quedan inhabilitados
        cerradura.unlock();
        assertThat(cerradura.getState()).isInstanceOf(EstadoAlarmaActivada.class);
        assertThat(cerradura.getStatusSummary()).isEqualTo("¡ALARMA DISPARADA!");

        // Restablecimiento de alarma devuelve la cerradura al estado bloqueado seguro
        cerradura.resetAlarm();
        assertThat(cerradura.getState()).isInstanceOf(EstadoBloqueado.class);
        assertThat(cerradura.getStatusSummary()).isEqualTo("Bloqueado");
    }

    // ==========================================
    // 9. PATRÓN OBSERVER
    // ==========================================
    @Test
    @DisplayName("9. Observer: Notificación reactiva desacoplada 1:N entre Sujeto y Observador")
    void testObserverNotificaciones() {
        DetectorHumoIncendio detector = new DetectorHumoIncendio("SMK-OBS", "Sensor Óptico Humo", "Sala de Servidores");

        AtomicBoolean notificado = new AtomicBoolean(false);
        AtomicReference<String> detalleRecibido = new AtomicReference<>("");

        ObservadorSeguridad observadorCustom = (device, details) -> {
            notificado.set(true);
            detalleRecibido.set(details);
        };

        detector.addObserver(observadorCustom);

        // Modificar umbral dispara notificación
        detector.setThresholdTemperature(68.5);
        assertThat(notificado.get()).isTrue();
        assertThat(detalleRecibido.get()).contains("Umbral de temperatura establecido en 68.5°C");

        // Desuscribir observador
        detector.removeObserver(observadorCustom);
        notificado.set(false);
        detector.setOn(true);
        assertThat(notificado.get())
                .as("El observador desuscrito no debe recibir nuevas notificaciones")
                .isFalse();
    }
}
