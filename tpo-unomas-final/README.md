# TPO - Proceso de Desarrollo de Software
## Sistema "Uno Mas" - Gestión de Encuentros Deportivos

### 📋 Descripción
Sistema para la gestión de encuentros deportivos donde los usuarios pueden encontrar jugadores para completar equipos de diferentes deportes. Implementa arquitectura MVC y patrones de diseño.

---

## 🏗️ Arquitectura y Patrones

### Patrón Arquitectónico: **MVC (Model-View-Controller)**
- **Model**: Clases en `com.tpopdsunomas.model`
- **View**: Interfaz de consola en `Main.java`
- **Controller**: Services en `com.tpopdsunomas.service`

### Patrones de Diseño Implementados:

#### 1. **State Pattern** (Estados del Partido)
- **Ubicación**: `patterns/state/`
- **Clases**:
  - `IEstadoPartido` (interfaz)
  - `NecesitaJugadores`, `Armado`, `Confirmado`, `EnJuego`, `Finalizado`, `Cancelado`
- **Justificación**: El partido tiene diferentes comportamientos según su estado. El patrón State evita condicionales complejos y facilita agregar nuevos estados sin modificar código existente.

#### 2. **Strategy Pattern** (Emparejamiento y Niveles)
- **Ubicación**: `patterns/strategy/`
- **Estrategias de Emparejamiento**:
  - `IStrategyEmparejamiento` (interfaz)
  - `EmparejamientoPorNivel`
  - `EmparejamientoPorCercania`
  - `EmparejamientoPorHistorial`
- **Estrategias de Nivel** (evita usar enum):
  - `INivelJugador` (interfaz)
  - `Principiante`, `Intermedio`, `Avanzado`
- **Justificación**: Permite cambiar algoritmos de búsqueda y niveles en tiempo de ejecución sin modificar el código cliente. Facilita la extensibilidad y el principio Open/Closed de SOLID.

#### 3. **Observer Pattern** (Notificaciones)
- **Ubicación**: `patterns/observer/`
- **Clases**:
  - `IObserverNotificacion` (interfaz)
  - `EmailNotificacion`
  - `PushNotificacion` (simulado)
- **Justificación**: Múltiples observadores deben ser notificados de cambios en el partido. Desacopla el partido de los mecanismos de notificación.

#### 4. **Adapter Pattern** (Integración de servicios externos)
- **Ubicación**: `patterns/adapter/`
- **Adapters**:
  - `IMail` (interfaz target)
  - `JavaMailSender` (adapta `EmailService`)
  - `OpenStreetMapAdapter` (adapta `Geolocation`)
- **Justificación**: Integra bibliotecas externas (Jakarta Mail, OpenStreetMap API) sin modificar sus interfaces, cumpliendo el principio de Inversión de Dependencias.

#### 5. **Repository Pattern** (Persistencia)
- **Ubicación**: `patterns/repo/`
- **Interfaces**: `ICuentaRepository`, `IPartidoRepository`, `IDeporteRepository`
- **Implementaciones**: `CuentaRepoLocal`, `PartidoRepoLocal`, `DeporteRepoLocal`
- **Justificación**: Abstrae la capa de persistencia, facilitando testing y permitiendo cambiar el almacenamiento (memoria → base de datos) sin modificar la lógica de negocio.

---

## 📦 Estructura del Proyecto

```
tpo-unomas-final/
├── src/main/java/com/tpopdsunomas/
│   ├── Main.java (Vista/Controlador - MVC)
│   ├── model/ (Modelo - MVC)
│   │   ├── Deporte.java (abstracta)
│   │   ├── Futbol.java, Basquet.java, Tenis.java, etc.
│   │   ├── Cuenta.java
│   │   ├── Partido.java
│   │   ├── Ubicacion.java
│   │   ├── Comentario.java
│   │   └── Estadistica.java
│   ├── patterns/
│   │   ├── state/ (Patrón State)
│   │   ├── strategy/ (Patrón Strategy)
│   │   ├── observer/ (Patrón Observer)
│   │   ├── adapter/ (Patrón Adapter)
│   │   └── repo/ (Patrón Repository)
│   ├── service/ (Controlador - MVC)
│   │   ├── CuentaService.java
│   │   ├── PartidoService.java
│   │   ├── DeporteService.java
│   │   ├── EmailService.java
│   │   └── Geolocation.java
│   └── util/
│       └── ConfigLoader.java
├── src/main/resources/
│   └── config.properties (configuración de email)
└── pom.xml (Maven)
```

---

## 🚀 Compilación y Ejecución

### Requisitos:
- Java 17 o superior
- Maven 3.6+

### Compilar:
```bash
mvn clean compile
```

### Ejecutar:
```bash
mvn exec:java -Dexec.mainClass="com.tpopdsunomas.Main"
```

O después de compilar:
```bash
java -cp target/classes com.tpopdsunomas.Main
```

### Crear JAR ejecutable:
```bash
mvn package
java -jar target/tpounomas-1.0-SNAPSHOT.jar
```

---

## 📧 Configuración de Email

Para que las notificaciones por email funcionen, configure `src/main/resources/config.properties`:

```properties
email.username=tu_email@gmail.com
email.password=tu_app_password
```

**Nota**: Para Gmail, debe generar una "App Password":
1. Ir a https://myaccount.google.com/security
2. Habilitar verificación en dos pasos
3. Generar contraseña de aplicación

---

## ✅ Requisitos Funcionales Implementados

1. ✅ **Registro de usuarios** con nivel de juego
2. ✅ **Búsqueda de partidos** en zona (usando OpenStreetMap)
3. ✅ **Creación de partido** con todos los atributos requeridos
4. ✅ **Estados del partido** (6 estados con transiciones correctas)
5. ✅ **Estrategias de emparejamiento** (3 estrategias diferentes)
6. ✅ **Notificaciones** (Email real + Push simulado)

## ✅ Requisitos No Funcionales Implementados

1. ✅ **Patrón MVC** claramente identificado
2. ✅ **Mínimo 4 patrones** (State, Strategy x2, Observer, Adapter x2, Repository)
3. ✅ **NO usa enums** (Deporte usa herencia, Niveles usan Strategy)
4. ✅ **NO hay clases vacías** (todas tienen implementación completa)
5. ✅ **Diagrama de clases UML** proporcionado
6. ✅ **Código implementado** y funcional

---

## 🎯 Principios SOLID Aplicados

- **S**ingle Responsibility: Cada clase tiene una única responsabilidad
- **O**pen/Closed: Extensible sin modificar código existente (Strategy, State)
- **L**iskov Substitution: Subtipos de Deporte y Estados son intercambiables
- **I**nterface Segregation: Interfaces pequeñas y específicas
- **D**ependency Inversion: Dependemos de abstracciones (interfaces)

## 📐 Principios GRASP Aplicados

- **Controller**: Services actúan como controladores
- **Creator**: Factories implícitas en Services
- **Low Coupling**: Bajo acoplamiento gracias a interfaces
- **High Cohesion**: Clases con responsabilidades relacionadas
- **Information Expert**: Cada clase maneja su propia información

---

## 🧪 Casos de Uso de Prueba

### 1. Flujo Completo de un Partido:
```
1. Registrar 3 usuarios con diferentes niveles
2. Crear partido de fútbol (requiere 10 jugadores)
3. Unir usuarios al partido
4. Al completarse → Estado "Armado" + Notificaciones
5. Confirmar partido → Estado "Confirmado" + Notificaciones
6. Iniciar juego → Estado "En Juego" + Notificaciones
7. Finalizar → Estado "Finalizado" + Notificaciones
```

### 2. Estrategias de Búsqueda:
```
1. Búsqueda por nivel: Encuentra partidos compatibles
2. Búsqueda por cercanía: Usa OpenStreetMap (requiere internet)
3. Búsqueda por historial: Encuentra partidos con conocidos
```

### 3. Notificaciones:
```
1. Email real: Configure config.properties y pruebe
2. Push simulado: Se imprime en consola
```

---

## 👥 Integrantes del Equipo

[Agregar nombres, apellidos y LU de los integrantes]

---

## 📚 Documentación Adicional

- **Diagrama de Clases UML**: Ver archivo `DiagramaClases.jpeg`
- **Consigna del TPO**: Ver archivo `Proceso_de_Desarrollo_de_Software-_TPO-_MRI-_2do_Cuatrimestre.pdf`
- **Criterios de Evaluación**: Ver archivo `adoo_evaluacion_del_parcial.pdf`

---

## 🔍 Notas Importantes

1. **Sin Enums**: Se usó herencia para Deporte y Strategy para Niveles
2. **Clases Completas**: Todas las clases tienen implementación funcional
3. **Email Real**: Funciona con configuración correcta de Gmail
4. **OpenStreetMap**: Requiere conexión a internet para geolocalización
5. **Push Simulado**: Firebase requeriría configuración compleja, se simuló

---

## 📝 Licencia

Proyecto académico para TPO de Proceso de Desarrollo de Software - UADE

---

**¡Gracias por revisar nuestro proyecto!** 🎉
