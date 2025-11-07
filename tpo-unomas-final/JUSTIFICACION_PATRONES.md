# JUSTIFICACIÓN DE PATRONES DE DISEÑO
## TPO - Sistema Uno Mas

---

## 1. PATRÓN STATE (Estados del Partido)

### Problema que Resuelve:
Un partido tiene diferentes comportamientos según su estado actual:
- En "Necesita Jugadores" se pueden agregar jugadores
- En "Armado" ya no se pueden agregar más jugadores
- En "En Juego" no se puede cancelar
- etc.

Sin el patrón State, tendríamos múltiples `if/switch` en cada método del Partido para verificar el estado actual, lo que resulta en código difícil de mantener y extender.

### Solución Implementada:
- **Interfaz**: `IEstadoPartido`
- **Estados Concretos**: `NecesitaJugadores`, `Armado`, `Confirmado`, `EnJuego`, `Finalizado`, `Cancelado`
- **Contexto**: `Partido` delega comportamiento al estado actual

### Beneficios:
1. Elimina condicionales complejos
2. Facilita agregar nuevos estados sin modificar código existente (Open/Closed)
3. Cada estado encapsula su comportamiento específico
4. Las transiciones están claramente definidas

### Ejemplo de Código:
```java
// En Partido.java
public void agregarJugador(Cuenta jugador) {
    this.estado.agregarJugador(this, jugador);  // Delega al estado
}

// En NecesitaJugadores.java
@Override
public void agregarJugador(Partido partido, Cuenta jugador) {
    partido.getJugadores().add(jugador);
    if (partido.estaCompleto()) {
        partido.setEstado(new Armado());  // Transición automática
    }
}
```

---

## 2. PATRÓN STRATEGY (Dos Implementaciones)

### A) Strategy para Niveles de Jugador

#### Problema que Resuelve:
El TPO requiere NO usar enums, pero necesitamos representar niveles (Principiante, Intermedio, Avanzado) con comportamientos diferentes.

#### Solución Implementada:
- **Interfaz**: `INivelJugador`
- **Estrategias Concretas**: `Principiante`, `Intermedio`, `Avanzado`
- Cada nivel tiene su propia lógica de compatibilidad y progresión

#### Beneficios:
1. Evita usar enum (cumple requisito del TPO)
2. Polimorfismo: tratamos todos los niveles uniformemente
3. Cada nivel puede tener lógica específica (ej: Principiante puede jugar con todos)
4. Facilita agregar nuevos niveles sin modificar código existente

#### Ejemplo de Código:
```java
// En Principiante.java
@Override
public boolean puedeJugarCon(INivelJugador otroNivel) {
    return true;  // Un principiante puede jugar con cualquiera
}

// En Avanzado.java
@Override
public boolean puedeJugarCon(INivelJugador otroNivel) {
    return otroNivel.getNivel() >= 15;  // Solo con intermedios/avanzados
}
```

### B) Strategy para Emparejamiento de Partidos

#### Problema que Resuelve:
Los usuarios necesitan diferentes formas de buscar partidos:
- Por nivel de habilidad
- Por cercanía geográfica
- Por jugadores conocidos (historial)

Hardcodear un solo algoritmo de búsqueda no es flexible.

#### Solución Implementada:
- **Interfaz**: `IStrategyEmparejamiento`
- **Estrategias Concretas**:
  - `EmparejamientoPorNivel`
  - `EmparejamientoPorCercania`
  - `EmparejamientoPorHistorial`

#### Beneficios:
1. Permite cambiar algoritmo de búsqueda en runtime
2. Facilita agregar nuevas estrategias (ej: por deporte favorito)
3. Cada estrategia encapsula su algoritmo complejo
4. El cliente (Service) no conoce detalles de implementación

#### Ejemplo de Código:
```java
// En PartidoService.java
public List<Partido> buscarPartidos(Cuenta buscador, IStrategyEmparejamiento estrategia) {
    List<Partido> todos = partidoRepo.buscarDisponibles();
    return estrategia.buscar(buscador, todos);  // Delega a la estrategia
}

// El usuario puede elegir la estrategia en runtime
IStrategyEmparejamiento estrategia = new EmparejamientoPorNivel();
// o
estrategia = new EmparejamientoPorCercania(10.0);  // 10 km de radio
```

---

## 3. PATRÓN OBSERVER (Notificaciones)

### Problema que Resuelve:
Cuando un partido cambia de estado, múltiples entidades necesitan ser notificadas:
- Enviar email a los participantes
- Enviar notificación push
- En el futuro: actualizar dashboard, enviar SMS, etc.

Hardcodear las notificaciones en `Partido.setEstado()` crea alto acoplamiento.

### Solución Implementada:
- **Interfaz**: `IObserverNotificacion`
- **Observadores Concretos**:
  - `EmailNotificacion`
  - `PushNotificacion`
- **Sujeto**: `Partido` mantiene lista de observadores

### Beneficios:
1. Bajo acoplamiento: Partido no conoce los detalles de cada notificación
2. Facilita agregar nuevos tipos de notificación sin modificar Partido
3. Los observadores se pueden agregar/quitar dinámicamente
4. Cumple con el principio Open/Closed

### Ejemplo de Código:
```java
// En Partido.java
public void setEstado(IEstadoPartido nuevoEstado) {
    this.estado = nuevoEstado;
    this.notificarObservadores();  // Notifica automáticamente
}

public void notificarObservadores() {
    for (IObserverNotificacion obs : observers) {
        obs.actualizar(this);  // Cada observador decide qué hacer
    }
}

// En PartidoService.crearPartido()
partido.agregarObservador(new EmailNotificacion());
partido.agregarObservador(new PushNotificacion());
// Fácil agregar: partido.agregarObservador(new SMSNotificacion());
```

---

## 4. PATRÓN ADAPTER (Dos Implementaciones)

### A) Adapter para JavaMail

#### Problema que Resuelve:
La librería Jakarta Mail tiene su propia interfaz (`EmailService.enviarCorreo()`), pero nuestro sistema necesita una interfaz estándar (`IMail.enviarMail()`).

No queremos modificar nuestro código cada vez que cambiamos de librería de email.

#### Solución Implementada:
- **Interfaz Target**: `IMail`
- **Adaptee**: `EmailService` (Jakarta Mail)
- **Adapter**: `JavaMailSender`

#### Beneficios:
1. Desacopla el sistema de la librería específica
2. Facilita cambiar de librería (ej: de Jakarta Mail a SendGrid) sin modificar el Observer
3. Cumple con el principio de Inversión de Dependencias
4. El cliente (`EmailNotificacion`) programa contra la interfaz, no contra la implementación

#### Ejemplo de Código:
```java
// Interfaz que nuestro sistema espera
public interface IMail {
    void enviarMail(String destinatario, String asunto, String mensaje);
}

// Adapter que traduce nuestra interfaz a Jakarta Mail
public class JavaMailSender implements IMail {
    private EmailService emailService;  // Jakarta Mail
    
    @Override
    public void enviarMail(String destinatario, String asunto, String mensaje) {
        // Adapta el método enviarMail() a enviarCorreo()
        emailService.enviarCorreo(destinatario, asunto, mensaje);
    }
}
```

### B) Adapter para OpenStreetMap

#### Problema que Resuelve:
La API de OpenStreetMap tiene su propia estructura de peticiones y respuestas. Necesitamos adaptar esta API para calcular distancias entre ubicaciones de manera simple.

#### Solución Implementada:
- **Servicio Externo**: `Geolocation` (consume API de OpenStreetMap)
- **Adapter**: `OpenStreetMapAdapter`
- **Cliente**: `EmparejamientoPorCercania`

#### Beneficios:
1. Encapsula la complejidad de las llamadas HTTP y parsing JSON
2. Facilita testing (se puede mockear el adapter)
3. Si cambiamos a Google Maps API, solo modificamos el adapter
4. Proporciona una interfaz simple para el Strategy de cercanía

#### Ejemplo de Código:
```java
// El adapter simplifica el uso de OpenStreetMap
public class OpenStreetMapAdapter {
    public double calcularDistancia(Ubicacion origen, Ubicacion destino) {
        // Internamente usa Geolocation para geocoding y cálculo
        // Pero expone una interfaz simple
        double[] coords1 = Geolocation.geocodeAddress(origen.getDireccion());
        double[] coords2 = Geolocation.geocodeAddress(destino.getDireccion());
        return Geolocation.calcularDistancia(coords1[0], coords1[1], 
                                             coords2[0], coords2[1]);
    }
}

// Uso simple en EmparejamientoPorCercania
OpenStreetMapAdapter geoAdapter = new OpenStreetMapAdapter();
double distancia = geoAdapter.calcularDistancia(ubicacion1, ubicacion2);
```

---

## 5. PATRÓN REPOSITORY (Bonus)

### Problema que Resuelve:
La lógica de negocio (Services) no debería conocer detalles de cómo se persisten los datos (memoria, base de datos, API, etc.).

Sin Repository, tendríamos acceso directo a colecciones en memoria desde los Services, lo que dificulta:
- Testing (no se pueden mockear datos)
- Migración a base de datos real
- Mantenimiento del código

### Solución Implementada:
- **Interfaces**: `ICuentaRepository`, `IPartidoRepository`, `IDeporteRepository`
- **Implementaciones**: `CuentaRepoLocal`, `PartidoRepoLocal`, `DeporteRepoLocal`

### Beneficios:
1. Separa lógica de negocio de persistencia
2. Facilita testing con mocks
3. Permite cambiar implementación (de memoria a BD) sin modificar Services
4. Cumple con el principio de Inversión de Dependencias

### Ejemplo de Código:
```java
// Interfaz
public interface ICuentaRepository {
    void guardar(Cuenta cuenta);
    Optional<Cuenta> buscarPorId(int id);
    List<Cuenta> buscarTodas();
}

// El Service depende de la interfaz, no de la implementación
public class CuentaService {
    private ICuentaRepository repo;  // Interfaz, no implementación concreta
    
    public CuentaService(ICuentaRepository repo) {
        this.repo = repo;  // Inyección de dependencia
    }
    
    public Cuenta registrarCuenta(...) {
        Cuenta cuenta = new Cuenta(...);
        repo.guardar(cuenta);  // No sabe si es memoria, BD, o API
        return cuenta;
    }
}
```

---

## RESUMEN DE CUMPLIMIENTO DE REQUISITOS

### ✅ Requisitos No Funcionales:
1. **Patrón MVC**: Main (Vista), Services (Controlador), Model (Modelo)
2. **Mínimo 4 patrones**: State, Strategy (x2), Observer, Adapter (x2), Repository
3. **NO enums**: Deporte usa herencia abstracta, Niveles usan Strategy
4. **NO clases vacías**: Todas tienen implementación completa
5. **Código funcional**: Sistema completo y operativo

### ✅ Principios SOLID:
- **S**ingle Responsibility: Cada clase tiene una única responsabilidad
- **O**pen/Closed: Patrones State y Strategy facilitan extensión
- **L**iskov Substitution: Subtipos son intercambiables
- **I**nterface Segregation: Interfaces pequeñas y específicas
- **D**ependency Inversion: Services dependen de interfaces, no implementaciones

### ✅ Principios GRASP:
- **Controller**: Services
- **Information Expert**: Cada clase maneja su información
- **Low Coupling**: Uso de interfaces
- **High Cohesion**: Responsabilidades bien definidas
- **Polymorphism**: Strategy y State

---

**Conclusión**: El sistema implementa correctamente todos los patrones requeridos, siguiendo las mejores prácticas de diseño orientado a objetos y cumpliendo con todos los requisitos del TPO.
