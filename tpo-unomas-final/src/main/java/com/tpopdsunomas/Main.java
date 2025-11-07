package com.tpopdsunomas;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import com.tpopdsunomas.model.*;
import com.tpopdsunomas.patterns.repo.*;
import com.tpopdsunomas.patterns.state.*;
import com.tpopdsunomas.patterns.strategy.*;
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
    // 🔽 AÑADIDO 🔽
    private static IEstadisticaRepository estadisticaRepo = new EstadisticaRepoLocal();
    private static IComentarioRepository comentarioRepo = new ComentarioRepoLocal();

    // Servicios (Capa de lógica de negocio - Controlador en MVC)
    private static CuentaService cuentaService = new CuentaService(cuentaRepo);
    private static PartidoService partidoService = new PartidoService(partidoRepo, cuentaRepo);
    private static DeporteService deporteService = new DeporteService(deporteRepo);
    // 🔽 AÑADIDO 🔽 (Ajusta los constructores si es necesario)
    private static EstadisticaService estadisticaService = new EstadisticaService(estadisticaRepo, cuentaRepo, partidoRepo);
    private static ComentarioService comentarioService = new ComentarioService(comentarioRepo, cuentaRepo, partidoRepo);


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
                    probarEnvioEmail();
                    break;
                case 10:
                    mostrarDeportes();
                    break;
                // 🔽 AÑADIDO 🔽
                case 11:
                    registrarEstadisticas();
                    break;
                case 12:
                    agregarComentario();
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
        System.out.println("11. Registrar estadísticas de partido (Finalizado)");
        System.out.println("12. Añadir comentario a partido (Finalizado)");
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
            // Se aplica la corrección del ID que hicimos antes
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
        
        // 🔽 VALIDACIÓN AÑADIDA (Recomendación anterior) 🔽
        Cuenta organizador = cuentaService.buscarPorId(idOrganizador).orElse(null);
        if (organizador == null) {
            System.out.println("⚠ Usuario organizador no encontrado. Volviendo al menú.");
            return; // Salir del método
        }

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

        // Ubicación
        System.out.print("\nCiudad: ");
        String ciudad = scanner.nextLine();
        System.out.print("Dirección: ");
        String direccion = scanner.nextLine();
        Ubicacion ubicacion = new Ubicacion(ciudad, direccion);

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

        // Nivel requerido
        System.out.println("\n--- Nivel Requerido ---");
        System.out.println("1. Principiante");
        System.out.println("2. Intermedio");
        System.out.println("3. Avanzado");
        System.out.println("4. Sin restricción de nivel");
        System.out.print("Seleccione opción: ");
        int opNivel = leerOpcion();

        INivelJugador nivelRequerido = null;
        // Cuenta organizador = cuentaService.buscarPorId(idOrganizador).orElse(null); // Movido arriba
        
        if (opNivel >= 1 && opNivel <= 3 && organizador != null) {
            int puntos = (opNivel == 1) ? 5 : (opNivel == 2) ? 15 : 25;
            // Crear instancia temporal solo para el nivel requerido
            Cuenta cuentaTemporal = new Cuenta(0, "Temp", "temp@temp.com", "temp");
            if (opNivel == 1) {
                nivelRequerido = new Principiante(puntos, cuentaTemporal);
            } else if (opNivel == 2) {
                nivelRequerido = new Intermedio(puntos, cuentaTemporal);
            } else {
                nivelRequerido = new Avanzado(puntos, cuentaTemporal);
            }
        }

        try {
            Partido partido = partidoService.crearPartido(
                idOrganizador, deporteSeleccionado, cantJugadores, ubicacion,
                "90 minutos", false, fechaHora, nivelRequerido
            );

            System.out.println("\n✓ Partido creado exitosamente!");
            System.out.println(partido);
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
            .filter(p -> p.getEstado() instanceof NecesitaJugadores) // Asume que existe esta clase de estado
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
                    System.out.println("✓ Partido finalizado. Ahora puedes registrar estadísticas (Opción 11) y comentarios (Opción 12).");
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

    // 🔽 RECOMENDACIÓN: Método para leer doubles 🔽
    private static double leerDouble() {
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1.0; // O un valor por defecto que manejes
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
                // 🔽 CORRECCIÓN: Usar leerDouble() 🔽
                System.out.print("Radio máximo en kilómetros (ej: 10.5): ");
                double radio = leerDouble(); // Usar el nuevo método
                if (radio <= 0) {
                     System.out.println("⚠ Radio inválido. Usando 10km por defecto.");
                     radio = 10.0;
                }
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

    private static void probarEnvioEmail() {
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
    }

    private static void mostrarPartidos() {
        System.out.println("\n═══ LISTA DE PARTIDOS ═══");
        List<Partido> partidos = partidoService.obtenerTodosLosPartidos();
        
        if (partidos.isEmpty()) {
            System.out.println("No hay partidos registrados");
            return;
        }
        
        for (Partido p : partidos) {
            System.out.println("\n───────────────────────────────");
            System.out.println(p); // Muestra info básica del partido
            
            System.out.println("Jugadores inscritos:");
            p.getJugadores().forEach(j -> 
                System.out.println("  - " + j.getNombre() + " (" + j.getNivel().getNombre() + ")")
            );

            // 🔽 AÑADIDO: Mostrar Estadísticas y Comentarios 🔽
            System.out.println("Estadísticas:");
            // (Asumo que tu service tiene este método)
            List<Estadistica> stats = estadisticaService.obtenerEstadisticasPorPartido(p.getId());
            if (stats.isEmpty()) {
                System.out.println("  (Sin estadísticas registradas)");
            } else {
                stats.forEach(s -> System.out.println("  - " + s)); // Usa el toString() de Estadistica
            }

            System.out.println("Comentarios:");
            // (Asumo que tu service tiene este método)
            List<Comentario> comentarios = comentarioService.obtenerComentariosPorPartido(p.getId());
            if (comentarios.isEmpty()) {
                System.out.println("  (Sin comentarios)");
            } else {
                comentarios.forEach(c -> System.out.println("  - " + c)); // Usa el toString() de Comentario
            }
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

    // 🔽🔽🔽 MÉTODOS NUEVOS AÑADIDOS 🔽🔽🔽

    /**
     * Permite registrar estadísticas para un jugador en un partido finalizado.
     */
    private static void registrarEstadisticas() {
        System.out.println("\n═══ REGISTRAR ESTADÍSTICAS ═══");

        // 1. Filtrar partidos finalizados
        // (Asumo que tienes una clase de estado llamada 'Finalizado' en state.*)
        List<Partido> partidosFinalizados = partidoService.obtenerTodosLosPartidos().stream()
                .filter(p -> p.getEstado() instanceof Finalizado)
                .toList();

        if (partidosFinalizados.isEmpty()) {
            System.out.println("⚠ No hay partidos finalizados para registrar estadísticas.");
            return;
        }

        // 2. Seleccionar Partido
        System.out.println("\n--- Partidos Finalizados ---");
        partidosFinalizados.forEach(p ->
                System.out.println(p.getId() + ". " + p.getTipoDeporte().getNombre() + " en " + p.getUbicacion())
        );
        System.out.print("\nID del partido: ");
        int idPartido = leerOpcion();
        
        Partido partido = partidoService.buscarPartidoPorId(idPartido).orElse(null);
        if (partido == null || !(partido.getEstado() instanceof Finalizado)) {
            System.out.println("⚠ ID de partido inválido o no está finalizado.");
            return;
        }

        // 3. Seleccionar Jugador (del partido)
        System.out.println("\n--- Jugadores del Partido ---");
        partido.getJugadores().forEach(j -> System.out.println(j.getId() + ". " + j.getNombre()));
        System.out.print("\nID del jugador para cargar estadísticas: ");
        int idJugador = leerOpcion();

        Cuenta jugador = partido.getJugadores().stream()
                           .filter(j -> j.getId() == idJugador).findFirst().orElse(null);
        
        if (jugador == null) {
            System.out.println("⚠ Ese jugador no participó en este partido.");
            return;
        }

        // 4. Pedir datos
        System.out.print("Puntos anotados: ");
        int puntos = leerOpcion();
        System.out.print("Asistencias: ");
        int asistencias = leerOpcion();
        System.out.print("Faltas cometidas: ");
        int faltas = leerOpcion();
        System.out.print("¿Fue MVP? (1=Sí, 0=No): ");
        String mvp = leerOpcion() == 1 ? "Sí" : "No";
        System.out.print("Descripción/Notas: ");
        String descripcion = scanner.nextLine();

        // 5. Llamar al servicio
        try {
            // (Asumo que tu EstadisticaService tiene un método así)
            Estadistica nuevaEstadistica = estadisticaService.crearEstadistica(
                idJugador, idPartido, puntos, asistencias, faltas, mvp, descripcion
            );
            System.out.println("\n✓ Estadísticas registradas para " + jugador.getNombre() + "!");
            System.out.println("Puntuación general: " + nuevaEstadistica.calcularPuntuacionGeneral());
        } catch (Exception e) {
            System.out.println("⚠ Error al registrar estadísticas: " + e.getMessage());
        }
    }

    /**
     * Permite añadir un comentario a un partido finalizado.
     */
    private static void agregarComentario() {
        System.out.println("\n═══ AÑADIR COMENTARIO ═══");

        // 1. Filtrar partidos finalizados
        List<Partido> partidosFinalizados = partidoService.obtenerTodosLosPartidos().stream()
                .filter(p -> p.getEstado() instanceof Finalizado)
                .toList();

        if (partidosFinalizados.isEmpty()) {
            System.out.println("⚠ No hay partidos finalizados para comentar.");
            return;
        }

        // 2. Seleccionar Partido
        System.out.println("\n--- Partidos Finalizados ---");
        partidosFinalizados.forEach(p ->
                System.out.println(p.getId() + ". " + p.getTipoDeporte().getNombre() + " en " + p.getUbicacion())
        );
        System.out.print("\nID del partido: ");
        int idPartido = leerOpcion();
        
        Partido partido = partidoService.buscarPartidoPorId(idPartido).orElse(null);
        if (partido == null || !(partido.getEstado() instanceof Finalizado)) {
            System.out.println("⚠ ID de partido inválido o no está finalizado.");
            return;
        }

        // 3. Seleccionar Usuario que comenta (cualquier usuario)
        System.out.println("\n--- Seleccionar Usuario (Autor) ---");
        cuentaService.obtenerTodasLasCuentas().forEach(u -> 
            System.out.println(u.getId() + ". " + u.getNombre())
        );
        System.out.print("\nID del usuario que comenta: ");
        int idUsuario = leerOpcion();

        Cuenta autor = cuentaService.buscarPorId(idUsuario).orElse(null);
        if (autor == null) {
            System.out.println("⚠ Usuario no encontrado.");
            return;
        }

        // 4. Pedir texto
        System.out.print("Escribe tu comentario: ");
        String texto = scanner.nextLine();

        // 5. Llamar al servicio
        try {
            // (Asumo que tu ComentarioService tiene un método así)
            Comentario nuevoComentario = comentarioService.crearComentario(idUsuario, idPartido, texto);
            System.out.println("\n✓ Comentario añadido exitosamente!");
            System.out.println(nuevoComentario); // Usa el toString() de Comentario
        } catch (Exception e) {
            System.out.println("⚠ Error al añadir comentario: " + e.getMessage());
        }
    }
}