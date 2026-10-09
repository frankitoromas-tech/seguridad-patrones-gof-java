# 🛡️ Sistema Integral de Gestión de Seguridad & Vigilancia

[![Java 17 CI](https://github.com/frankitoromas-tech/seguridad-patrones-gof-java/actions/workflows/ci-build.yml/badge.svg)](https://github.com/frankitoromas-tech/seguridad-patrones-gof-java/actions/workflows/ci-build.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

[![Java 17](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![GoF Design Patterns](https://img.shields.io/badge/Patrones_GoF-8%20Implementados-blueviolet?style=for-the-badge)](https://en.wikipedia.org/wiki/Design_Patterns)
[![Architecture](https://img.shields.io/badge/Arquitectura-MVC%20%2B%20Clean%20Code-007ACC?style=for-the-badge)](#arquitectura-y-diseño)
[![UI](https://img.shields.io/badge/GUI-Java%20Swing-FF6F00?style=for-the-badge)](#interfaz-gráfica)
[![UTP](https://img.shields.io/badge/UTP-Ingenier%C3%ADa%20de%20Sistemas-red?style=for-the-badge)](https://www.utp.edu.pe/)

Sistema robusto de nivel empresarial para la monitorización, orquestación y auditoría de dispositivos de seguridad física (cámaras IP con streaming, sensores térmicos/humo, reflectores perimétricos y cerraduras electrónicas biométricas). 

Desarrollado en **Java 17**, el sistema ejemplifica la aplicación rigurosa de **8 patrones de diseño clásicos de la Gang of Four (GoF)**, desacoplamiento modular bajo principios **SOLID**, persistencia desacoplada en CSV y una interfaz visual interactiva basada en **Java Swing**.

---

## 🏛️ Matriz de Patrones de Diseño (GoF)

| Clasificación | Patrón GoF | Clase(s) Clave | Responsabilidad y Problema Resuelto |
| :--- | :--- | :--- | :--- |
| **Creacional** | **Singleton** | `CentralSeguridadSingleton` | Garantiza un único punto de control y coordinación centralizada para todo el sistema de vigilancia con acceso concurrente seguro. |
| **Creacional** | **Factory Method** | `FabricaDispositivos`, `FabricaVigilancia`, `FabricaDetectoresHumo`, `FabricaReflectores` | Desacopla la instanciación de dispositivos concretos (cámaras, sensores, reflectores) permitiendo extender nuevo hardware sin modificar la lógica consumidora. |
| **Creacional** | **Builder** | `ConstructorPerfilSeguridad`, `PerfilSeguridad` | Permite construir perfiles de seguridad complejos (horarios, niveles de autorización, zonas perimetrales) paso a paso con una sintaxis fluida y legible. |
| **Estructural** | **Adapter** | `AdaptadorSensorAntiguo`, `SensorAntiguoAnalogico` | Integra sensores analógicos heredados al ecosistema digital moderno adaptando su interfaz sin mutar el código legado. |
| **Estructural** | **Decorator** | `DecoradorDispositivo`, `DecoradorAuditorEnergia` | Incorpora dinámicamente capacidades de auditoría de telemetría y consumo energético a cualquier dispositivo en tiempo de ejecución sin alterar su jerarquía de clases. |
| **Estructural** | **Proxy** | `ProxyCamaraSeguridad` | Controla el acceso a las cámaras de alta resolución realizando validación de credenciales, control de roles y carga perezosa (*lazy loading*) de feeds de video. |
| **Comportamiento** | **Command** | `ComandoDispositivo`, `ComandoAccionSeguridad` | Encapsula las solicitudes de activación/desactivación como objetos independientes, facilitando el encolado de órdenes y el registro de auditoría. |
| **Comportamiento** | **State** | `EstadoCerradura`, `EstadoBloqueado`, `EstadoDesbloqueado`, `EstadoAlarmaActivada` | Modela el comportamiento de las cerraduras inteligentes según su estado actual, erradicando estructuras complejas de condicionales `if-else`. |
| **Comportamiento** | **Observer** | `SujetoObservable`, `ObservadorSeguridad` | Notifica en tiempo real a los operadores y consolas de monitoreo ante eventos críticos (detección de intrusos, alarmas de incendio o fallas de hardware). |

---

## 📐 Estructura del Proyecto

```
desarrollo-de-DP/
├── DiagramaClases.puml          # Especificación formal en PlantUML (arquitectura completa)
├── out.png / out.svg           # Renderizado visual en alta definición del diagrama
├── security_devices.csv        # Persistencia de dispositivos registrados y su estado
├── src/
│   └── com/seguridad/
│       ├── controlador/        # Coordinador MVC y orquestación de operaciones
│       │   └── ControladorSeguridad.java
│       ├── modelo/             # Entidades del dominio (Cámaras, Sensores, Cerraduras)
│       │   ├── DispositivoSeguridadBase.java
│       │   ├── CamaraVigilancia.java
│       │   ├── CerraduraElectronica.java
│       │   ├── DetectorHumoIncendio.java
│       │   ├── ReflectorSeguridad.java
│       │   └── UsuarioOperador.java
│       ├── patrones/           # Módulos organizados según la taxonomía GoF
│       │   ├── creacionales/   # Singleton, Factory Method, Builder
│       │   ├── estructurales/  # Adapter, Decorator, Proxy
│       │   └── comportamiento/ # Command, State, Observer
│       ├── persistencia/       # Acceso a datos y repositorio CSV
│       │   └── RepositorioDispositivosCSV.java
│       ├── vista/              # Componentes de interfaz gráfica Swing
│       │   ├── DialogoAcceso.java
│       │   └── PanelControlGUI.java
│       └── principal/          # Puntos de entrada ejecutables
│           ├── SistemaVigilanciaMain.java
│           └── EjecutorPruebasAuditoria.java
```

---

## 🚀 Compilación y Ejecución

### Requisitos Previos
- **JDK 17** o superior instalado.
- Terminal bash o PowerShell.

### 1. Compilar el Código Fuente
```bash
javac -d bin -sourcepath src src/com/seguridad/principal/SistemaVigilanciaMain.java
```

### 2. Ejecutar la Aplicación Principal (GUI Swing)
```bash
java -cp bin com.seguridad.principal.SistemaVigilanciaMain
```

### 3. Ejecutar la Suite de Auditoría de Patrones
```bash
java -cp bin com.seguridad.principal.EjecutorPruebasAuditoria
```

---

## 📊 Formato de Datos (`security_devices.csv`)
Los dispositivos se guardan y serializan con el siguiente esquema:
```csv
id,nombre,tipo,ubicacion,estado,consumo_watts
CAM-001,Camara Perimetral Norte,CAMARA,Zona Exterior A,ACTIVO,15.5
SEN-102,Detector de Humo Central,DETECTOR_HUMO,Pasillo 2,ACTIVO,3.2
CER-050,Cerradura Boveda Principal,CERRADURA,Sala de Servidores,BLOQUEADO,12.0
```

---

## 👨‍💻 Autor & Contacto
- **Desarrollador:** **Φραγκοσύνη / francus 🐦‍🔥** (Frank Emiliano Vargas Huamán)
- **Especialidad:** Backend Java Enterprise & Patrones de Arquitectura
- **GitHub:** [@frankitoromas-tech](https://github.com/frankitoromas-tech)
- **LinkedIn:** [Frank Emiliano Vargas](https://www.linkedin.com/in/frank-emiliano-vargas-huam%C3%A1n-6a010a378/)
