package com.tpopdsunomas;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import com.tpopdsunomas.model.*;
import com.tpopdsunomas.patterns.repo.*;
import com.tpopdsunomas.patterns.state.*;
import com.tpopdsunomas.patterns.strategy.*;
import com.tpopdsunomas.patterns.strategy.validacionIngreso.IStrategyValidacionIngreso;
import com.tpopdsunomas.patterns.strategy.validacionIngreso.ValidacionEstrictaPorNivel;
import com.tpopdsunomas.patterns.strategy.validacionIngreso.ValidacionPorCercania;
import com.tpopdsunomas.service.*;

/**
 * Clase Main - Vista/Controlador en el patrón MVC
 * Maneja la interacción con el usuario a través de consola
 */
public class Main {
    // Repositorios (Patrón Repository)
    private static ICuentaRepository cuentaRepo = new CuentaRepoLocal();
    private static IPartidoRepository partidoRepo = new PartidoRepoLocal();
    private static IDeporteRepository deporteRepo = new DeporteRepoLocal();

    // Servicios (Capa de lógica de negocio - Controlador en MVC)
    private static CuentaService cuentaService = new CuentaService(cuentaRepo);
    private static PartidoService partidoService = new PartidoService(partidoRepo, cuentaRepo);
    private static DeporteService deporteService = new DeporteService(deporteRepo);

    // Scanner para entrada del usuario
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        mostrarBanner();
        cargarDatosEjemplo();

        boolean continuar = true;
        while (continuar) {
            mostrarMenuPrincipal();
            int opcion = leerOpcion();

            switch (opcion) {
                case 1:
                    registrarUsuario();
                    break;
                case 2:
                    crearPartido();
                    break;
                case 3:
                    unirseAPartido();
                    break;
                case 4:
                    gestionarEstadoPartido();
                    break;
                case 5:
                    buscarPartidosConEstrategia();
                    break;
                case 6:
                    forzarNotificacion();
                    break;
                case 7:
                    mostrarPartidos();
                    break;
                case 8:
                    mostrarUsuarios();
                    break;
                case 9:
                    //probarEnvioEmail();
                    break;
                case 10:
                    mostrarDeportes();
                    break;
                case 0:
                    continuar = false;
                    System.out.println("\n¡Gracias por usar el Sistema Uno Mas!");
                    break;
                default:
                    System.out.println("⚠ Opción inválida");
            }
        }
        scanner.close();
    }

    private static void mostrarBanner() {
        System.out.println("╔═══════════════════════════════════════════════════╗");
        System.out.println("║     SISTEMA UNO MAS - GESTIÓN DE PARTIDOS        ║");
        System.out.println("║         Arquitectura MVC + Patrones de Diseño    ║");
        System.out.println("╚═══════════════════════════════════════════════════╝");
        System.out.println("\n✓ Patrones implementados:");
        System.out.println("  • State (Estados del Partido)");
        System.out.println("  • Strategy (Emparejamiento y Niveles)");
        System.out.println("  • Observer (Notificaciones)");
        System.out.println("  • Adapter (JavaMailSender y OpenStreetMap)");
        System.out.println("  • Repository (Persistencia de datos)\n");
    }

    private static void mostrarMenuPrincipal() {
        System.out.println("\n════════════ MENÚ PRINCIPAL ════════════");
        System.out.println("1.  Registrar nuevo usuario");
        System.out.println("2.  Crear nuevo partido");
        System.out.println("3.  Unirse a un partido");
        System.out.println("4.  Gestionar estado de partido");
        System.out.println("5.  Buscar partidos (Estrategias)");
        System.out.println("6.  Forzar notificación (Patrón Observer)");
        System.out.println("7.  Ver todos los partidos");
        System.out.println("8.  Ver todos los usuarios");
        System.out.println("9.  🔥 Probar envío de email real");
        System.out.println("10. Ver deportes disponibles");
        System.out.println("11. Ver estadisticas");
        System.out.println("0.  Salir");
        System.out.print("Seleccione una opción: ");
    }

    private static int leerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void registrarUsuario() {
        System.out.println("\n═══ REGISTRAR NUEVO USUARIO ═══");
        System.out.print("Nombre completo: ");
        String nombre = scanner.nextLine();
        
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Contraseña: ");
        String clave = scanner.nextLine();
        
        System.out.println("\nNivel de Juego:");
        System.out.println("  0-10  puntos = Principiante");
        System.out.println("  11-20 puntos = Intermedio");
        System.out.println("  21+   puntos = Avanzado");
        System.out.print("Puntos de nivel (0-50): ");
        int puntosNivel = leerOpcion();
        
        try {
            Cuenta nuevaCuenta = cuentaService.registrarCuenta(nombre, email, clave, puntosNivel);
            System.out.println("\n✓ Usuario registrado exitosamente!");
            System.out.println("ID: " + nuevaCuenta.getId());
            System.out.println("Nombre: " + nuevaCuenta.getNombre());
            System.out.println("Nivel: " + nuevaCuenta.getNivel().getNombre() + 
                             " (" + nuevaCuenta.getNivel().getNivel() + " pts)");
        } catch (Exception e) {
            System.out.println("⚠ Error al registrar: " + e.getMessage());
        }
    }

    private static void crearPartido() {
        System.out.println("\n═══ CREAR NUEVO PARTIDO ═══");

        // Seleccionar organizador
        List<Cuenta> usuarios = cuentaService.obtenerTodasLasCuentas();
        if (usuarios.isEmpty()) {
            System.out.println("⚠ No hay usuarios registrados. Registre un usuario primero.");
            return;
        }

        System.out.println("\n--- Seleccionar Organizador ---");
        usuarios.forEach(u -> System.out.println(u.getId() + ". " + u.getNombre() + 
                                                " (" + u.getNivel().getNombre() + ")"));
        System.out.print("ID del organizador: ");
        int idOrganizador = leerOpcion();

        // Seleccionar deporte
        System.out.println("\n--- Seleccionar Deporte ---");
        List<Deporte> deportes = deporteService.obtenerTodos();
        deportes.forEach(d -> System.out.println(d.getId() + ". " + d.getNombre()));
        System.out.print("ID del deporte: ");
        int idDeporte = leerOpcion();
        
        Deporte deporteSeleccionado = deporteService.buscarPorId(idDeporte).orElse(null);
        if (deporteSeleccionado == null) {
            System.out.println("⚠ Deporte no encontrado");
            return;
        }

        // Cantidad de jugadores
        System.out.print("\nCantidad de jugadores requeridos (por defecto " + 
                        deporteSeleccionado.getCantidadJugadoresPorDefecto() + "): ");
        int cantJugadores = leerOpcion();
        if (cantJugadores <= 0) {
            cantJugadores = deporteSeleccionado.getCantidadJugadoresPorDefecto();
        }

        System.out.println("\n--- Ubicación del Partido ---");
    System.out.print("Ciudad: ");
    String ciudadPartido = scanner.nextLine();
    System.out.print("Dirección: ");
    String dirPartido = scanner.nextLine();
    // (Asegúrate que tu clase Ubicacion tenga este constructor)
    Ubicacion ubicacionPartido = new Ubicacion(ciudadPartido, dirPartido);

        // Fecha y hora
        System.out.print("\nFecha del partido (dd/MM/yyyy HH:mm): ");
        String fechaStr = scanner.nextLine();
        LocalDateTime fechaHora;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            fechaHora = LocalDateTime.parse(fechaStr, formatter);
        } catch (Exception e) {
            System.out.println("⚠ Formato de fecha inválido. Usando fecha por defecto (mañana).");
            fechaHora = LocalDateTime.now().plusDays(1);
        }

        // --- 4. PREGUNTAR POR LA REGLA DE VALIDACIÓN (¡ESTA ES LA PARTE NUEVA!) ---
    System.out.println("\n--- Regla de Validación de Ingreso (RF5.c) ---");
    System.out.println("1. Abierto a todos (Default)");
    System.out.println("2. Estricto por Nivel");
    System.out.println("3. Estricto por Cercanía (Ubicación)");
    System.out.print("Seleccione la regla del partido: ");
    int opRegla = leerOpcion();

    INivelJugador nivelRequerido = null;
    IStrategyValidacionIngreso estrategiaDeValidacion; // La variable clave

    switch (opRegla) {
        case 2:
            // --- Lógica para Nivel ---
            System.out.println("\n--- Nivel Requerido para el Partido ---");
            System.out.println("1. Principiante");
            System.out.println("2. Intermedio");
            System.out.println("3. Avanzado");
            System.out.print("Seleccione nivel: ");
            int opNivel = leerOpcion();
            
            int puntos = (opNivel == 1) ? 5 : (opNivel == 2) ? 15 : 25;
            Cuenta dummy = new Cuenta(0, "dummy", "dummy", "dummy");
            nivelRequerido = dummy.getNivel();
            
            estrategiaDeValidacion = new ValidacionEstrictaPorNivel();
            break;

        case 3:
            // --- Lógica para Cercanía ---
            System.out.print("\nDistancia máxima en KM para unirse (ej: 10): ");
            double radio = Double.parseDouble(scanner.nextLine());
            
            estrategiaDeValidacion = new ValidacionPorCercania(radio);
            break;
            
        case 1:
        default:
            // --- Lógica Permisiva (Default) ---
            estrategiaDeValidacion = new ValidacionEstrictaPorNivel();
            break;
    }
    System.out.println("Regla del partido: " + estrategiaDeValidacion.getNombreRegla());


    // --- 5. LLAMAR AL SERVICIO (¡CORREGIDO!) ---
    // Ahora le pasamos los 9 argumentos que espera
    
    try {
        Partido partido = partidoService.crearPartido(
                idOrganizador,
                deporteSeleccionado,
                cantJugadores,
                fechaHora,
                nivelRequerido, // (será null si no se eligió opNivel)
                estrategiaDeValidacion // <-- ¡EL ARGUMENTO QUE FALTABA!
        );
        partido.setUbicacion(ubicacionPartido);

        System.out.println("\n✓ Partido creado exitosamente: " + partido.getId());
        System.out.println("Estado inicial: " + partido.getEstado().getNombre());
        System.out.println("Regla de ingreso: " + partido.getEstrategiaValidacion().getNombreRegla());

    } catch (Exception e) {
        System.out.println("⚠ Error al crear partido: " + e.getMessage());
        e.printStackTrace();
    }
    }

    private static void unirseAPartido() {
        System.out.println("\n═══ UNIRSE A UN PARTIDO ═══");

        // Mostrar partidos disponibles
        List<Partido> partidos = partidoService.obtenerTodosLosPartidos();
        List<Partido> partidosDisponibles = partidos.stream()
            .filter(p -> p.getEstado() instanceof NecesitaJugadores)
            .toList();

        if (partidosDisponibles.isEmpty()) {
            System.out.println("⚠ No hay partidos disponibles");
            return;
        }

        System.out.println("\n--- Partidos Disponibles ---");
        partidosDisponibles.forEach(p -> 
            System.out.println(p.getId() + ". " + p.getTipoDeporte().getNombre() + 
                             " - " + p.getJugadores().size() + "/" + p.getCantidadJugadores() +
                             " - " + p.getUbicacion())
        );

        System.out.print("\nID del partido: ");
        int idPartido = leerOpcion();

        // Mostrar usuarios
        List<Cuenta> usuarios = cuentaService.obtenerTodasLasCuentas();
        System.out.println("\n--- Seleccionar Usuario ---");
        usuarios.forEach(u -> System.out.println(u.getId() + ". " + u.getNombre()));
        System.out.print("ID del usuario: ");
        int idUsuario = leerOpcion();

        try {
            partidoService.unirseAPartido(idPartido, idUsuario);
            System.out.println("\n✓ Te has unido al partido exitosamente!");
        } catch (Exception e) {
            System.out.println("⚠ Error: " + e.getMessage());
        }
    }

    private static void gestionarEstadoPartido() {
        System.out.println("\n═══ GESTIONAR ESTADO DE PARTIDO ═══");

        List<Partido> partidos = partidoService.obtenerTodosLosPartidos();
        if (partidos.isEmpty()) {
            System.out.println("⚠ No hay partidos registrados");
            return;
        }

        System.out.println("\n--- Partidos ---");
        partidos.forEach(p -> 
            System.out.println(p.getId() + ". " + p.getTipoDeporte().getNombre() + 
                             " - Estado: " + p.getEstado().getNombre())
        );

        System.out.print("\nID del partido: ");
        int idPartido = leerOpcion();

        System.out.println("\n--- Acciones ---");
        System.out.println("1. Confirmar partido");
        System.out.println("2. Iniciar juego");
        System.out.println("3. Finalizar partido");
        System.out.println("4. Cancelar partido");
        System.out.print("Seleccione acción: ");
        int accion = leerOpcion();

        try {
            switch (accion) {
                case 1:
                    partidoService.confirmarPartido(idPartido);
                    break;
                case 2:
                    partidoService.iniciarPartido(idPartido);
                    break;
                case 3:
                    partidoService.finalizarPartido(idPartido);
                    break;
                case 4:
                    partidoService.cancelarPartido(idPartido);
                    break;
                default:
                    System.out.println("⚠ Acción inválida");
            }
        } catch (Exception e) {
            System.out.println("⚠ Error: " + e.getMessage());
        }
    }

    private static void buscarPartidosConEstrategia() {
        System.out.println("\n═══ BUSCAR PARTIDOS (ESTRATEGIAS) ═══");
        
        // Seleccionar usuario que busca
        List<Cuenta> usuarios = cuentaService.obtenerTodasLasCuentas();
        if (usuarios.isEmpty()) {
            System.out.println("⚠ No hay usuarios registrados");
            return;
        }

        System.out.println("\n--- Seleccionar Usuario que Busca ---");
        usuarios.forEach(u -> System.out.println(u.getId() + ". " + u.getNombre()));
        System.out.print("ID del usuario: ");
        int idUsuario = leerOpcion();

        Cuenta buscador = cuentaService.buscarPorId(idUsuario).orElse(null);
        if (buscador == null) {
            System.out.println("⚠ Usuario no encontrado");
            return;
        }

        // Seleccionar estrategia
        System.out.println("\n--- Estrategias de Búsqueda ---");
        System.out.println("1. Por Nivel de Habilidad");
        System.out.println("2. Por Cercanía Geográfica");
        System.out.println("3. Por Historial de Partidos");
        System.out.print("Seleccione estrategia: ");
        int opEstrategia = leerOpcion();

        IStrategyEmparejamiento estrategia;
        switch (opEstrategia) {
            case 1:
                estrategia = new EmparejamientoPorNivel();
                break;
            case 2:
                System.out.print("Radio máximo en kilómetros: ");
                double radio = leerOpcion();
                estrategia = new EmparejamientoPorCercania(radio);
                break;
            case 3:
                estrategia = new EmparejamientoPorHistorial();
                break;
            default:
                System.out.println("⚠ Estrategia inválida");
                return;
        }

        // Buscar partidos
        List<Partido> partidosEncontrados = partidoService.buscarPartidosParaUsuario(
            idUsuario, estrategia
        );

        System.out.println("\n✓ Partidos encontrados: " + partidosEncontrados.size());
        if (partidosEncontrados.isEmpty()) {
            System.out.println("No se encontraron partidos con los criterios seleccionados");
        } else {
            partidosEncontrados.forEach(p -> System.out.println("  " + p));
        }
    }

    private static void forzarNotificacion() {
        System.out.println("\n═══ FORZAR NOTIFICACIÓN (Observer) ═══");
        
        List<Partido> partidos = partidoService.obtenerTodosLosPartidos();
        if (partidos.isEmpty()) {
            System.out.println("⚠ No hay partidos");
            return;
        }

        System.out.println("\n--- Partidos ---");
        partidos.forEach(p -> 
            System.out.println(p.getId() + ". " + p.getTipoDeporte().getNombre() + 
                             " (" + p.getJugadores().size() + " jugadores)")
        );
        
        System.out.print("\nID del partido a notificar: ");
        int idPartido = leerOpcion();

        System.out.println("\n¡Forzando notificación a todos los participantes!");
        try {
            partidoService.notificarObservadores(idPartido);
            System.out.println("\n✓ Notificación enviada");
        } catch (Exception e) {
            System.out.println("⚠ Error: " + e.getMessage());
        }
    }

    /*private static void probarEnvioEmail() {
        System.out.println("\n═══ 🔥 PRUEBA DE ENVÍO DE EMAIL REAL ═══");
        System.out.print("Email destinatario de prueba: ");
        String emailDest = scanner.nextLine();

        try {
            // Crear usuario de prueba
            Cuenta cuentaPrueba = cuentaService.registrarCuenta(
                "Usuario de Prueba", emailDest, "123", 5
            );
            
            // Crear partido de prueba
            Deporte deportePrueba = deporteService.obtenerTodos().get(0);
            Ubicacion ubicacionPrueba = new Ubicacion("Buenos Aires", "Av. Test 123");
            
            Partido partidoPrueba = partidoService.crearPartido(
                cuentaPrueba.getId(), 
                deportePrueba, 
                10,
                ubicacionPrueba,
                "90 minutos",
                false,
                LocalDateTime.now().plusDays(1),
                new Principiante(5, cuentaPrueba)
            );

            // Unirse al partido
            partidoService.unirseAPartido(partidoPrueba.getId(), cuentaPrueba.getId());
            
            // Forzar notificación
            System.out.println("\n📧 Enviando email de prueba...");
            partidoService.notificarObservadores(partidoPrueba.getId());
            
            System.out.println("\n✓ Si no hubo errores, revisa la bandeja de: " + emailDest);
        } catch (Exception e) {
            System.out.println("⚠ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }*/

    private static void mostrarPartidos() {
        System.out.println("\n═══ LISTA DE PARTIDOS ═══");
        List<Partido> partidos = partidoService.obtenerTodosLosPartidos();
        
        if (partidos.isEmpty()) {
            System.out.println("No hay partidos registrados");
            return;
        }
        
        for (Partido p : partidos) {
            System.out.println("\n───────────────────────────────");
            System.out.println(p);
            System.out.println("Jugadores inscritos:");
            p.getJugadores().forEach(j -> 
                System.out.println("  - " + j.getNombre() + " (" + j.getNivel().getNombre() + ")")
            );
        }
        System.out.println("───────────────────────────────");
    }

    private static void mostrarUsuarios() {
        System.out.println("\n═══ LISTA DE USUARIOS ═══");
        List<Cuenta> cuentas = cuentaService.obtenerTodasLasCuentas();
        
        if (cuentas.isEmpty()) {
            System.out.println("No hay usuarios registrados");
            return;
        }
        
        for (Cuenta c : cuentas) {
            System.out.println("\n───────────────────────────────");
            System.out.println("ID: " + c.getId());
            System.out.println("Nombre: " + c.getNombre());
            System.out.println("Email: " + c.getEmail());
            System.out.println("Nivel: " + c.getNivel().getNombre() + 
                             " (" + c.getNivel().getNivel() + " pts)");
            System.out.println("Partidos creados: " + c.getPartidosCreados().size());
            System.out.println("Partidos inscritos: " + c.getPartidosInscritos().size());
        }
        System.out.println("───────────────────────────────");
    }

    private static void mostrarDeportes() {
        System.out.println("\n═══ DEPORTES DISPONIBLES ═══");
        List<Deporte> deportes = deporteService.obtenerTodos();
        
        for (Deporte d : deportes) {
            System.out.println("\n" + d.getId() + ". " + d.getNombre());
            System.out.println("   " + d.getDescripcion());
            System.out.println("   Jugadores por defecto: " + d.getCantidadJugadoresPorDefecto());
        }
    }

    private static void cargarDatosEjemplo() {
        System.out.println("\n⏳ Cargando datos de ejemplo...\n");

        try {
            // Cargar deportes (usando herencia, NO enum)
            deporteService.guardar(new Futbol(1));
            deporteService.guardar(new Basquet(2));
            deporteService.guardar(new Tenis(3));
            deporteService.guardar(new Voley(4));
            deporteService.guardar(new Padel(5));
            deporteService.guardar(new PingPong(6));
            deporteService.guardar(new Pool(7));

            // Cargar usuarios de ejemplo
            cuentaService.registrarCuenta("Juan Pérez", "juan@test.com", "pass123", 15);
            cuentaService.registrarCuenta("María García", "maria@test.com", "pass456", 25);
            cuentaService.registrarCuenta("Luis Rodríguez", "luis@test.com", "pass789", 5);

            System.out.println("✓ " + deporteService.obtenerTodos().size() + " deportes cargados");
            System.out.println("✓ " + cuentaService.obtenerTodasLasCuentas().size() + " usuarios cargados");
            System.out.println("✓ Configuración de email cargada");
            System.out.println("\n¡Sistema listo para operar!\n");

        } catch (Exception e) {
            System.out.println("⚠ Error cargando datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}