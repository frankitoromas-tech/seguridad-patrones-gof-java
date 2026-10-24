package com.seguridad.patrones;

import com.seguridad.modelo.CamaraVigilancia;
import com.seguridad.modelo.CerraduraElectronica;
import com.seguridad.modelo.DetectorHumoIncendio;
import com.seguridad.modelo.DispositivoSeguridadBase;
import com.seguridad.patrones.creacionales.CentralSeguridadSingleton;
import com.seguridad.patrones.creacionales.FabricaDetectoresHumo;
import com.seguridad.patrones.creacionales.FabricaDispositivos;
import com.seguridad.patrones.creacionales.FabricaVigilancia;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Pruebas Unitarias de Patrones de Diseño GoF")
class PatronesGoFTest {

    @Test
    @DisplayName("Patrón Singleton: Debe retornar exactamente la misma instancia única de la Central de Seguridad")
    void testCentralSeguridadSingleton() {
        CentralSeguridadSingleton instancia1 = CentralSeguridadSingleton.getInstance();
        CentralSeguridadSingleton instancia2 = CentralSeguridadSingleton.getInstance();

        assertThat(instancia1).isNotNull();
        assertThat(instancia2).isNotNull();
        assertThat(instancia1).isSameAs(instancia2);
    }

    @Test
    @DisplayName("Patrón Factory Method: FabricaVigilancia debe instanciar CamaraVigilancia y CerraduraElectronica")
    void testFabricaVigilancia() {
        FabricaDispositivos fabricaCamara = new FabricaVigilancia("Camera");
        DispositivoSeguridadBase camara = fabricaCamara.createDevice("CAM-101", "Cámara Principal", "Recepción");

        assertThat(camara).isNotNull();
        assertThat(camara).isInstanceOf(CamaraVigilancia.class);
        assertThat(camara.getId()).isEqualTo("CAM-101");
        assertThat(camara.getName()).isEqualTo("Cámara Principal");

        FabricaDispositivos fabricaCerrojo = new FabricaVigilancia("Lock");
        DispositivoSeguridadBase cerrojo = fabricaCerrojo.createDevice("LOCK-202", "Cerradura Bóveda", "Bóveda");

        assertThat(cerrojo).isNotNull();
        assertThat(cerrojo).isInstanceOf(CerraduraElectronica.class);
        assertThat(cerrojo.getId()).isEqualTo("LOCK-202");
    }

    @Test
    @DisplayName("Patrón Factory Method: FabricaDetectoresHumo debe instanciar DetectorHumoIncendio")
    void testFabricaDetectoresHumo() {
        FabricaDispositivos fabricaHumo = new FabricaDetectoresHumo();
        DispositivoSeguridadBase detector = fabricaHumo.createDevice("SMK-01", "Sensor Humo", "Laboratorio");

        assertThat(detector).isNotNull();
        assertThat(detector).isInstanceOf(DetectorHumoIncendio.class);
        assertThat(detector.getId()).isEqualTo("SMK-01");
    }

    @Test
    @DisplayName("Registro y Monitoreo en Central: Debe registrar dispositivo y permitir búsqueda por ID")
    void testRegistroDispositivoEnCentral() {
        CentralSeguridadSingleton central = CentralSeguridadSingleton.getInstance();
        FabricaDispositivos fabrica = new FabricaVigilancia("Camera");
        DispositivoSeguridadBase dispositivo = fabrica.createDevice("TEST-DEV-99", "Cámara Test", "Área Segura");

        central.addDevice(dispositivo);
        DispositivoSeguridadBase encontrado = central.findDevice("TEST-DEV-99");

        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getName()).isEqualTo("Cámara Test");
    }
}
