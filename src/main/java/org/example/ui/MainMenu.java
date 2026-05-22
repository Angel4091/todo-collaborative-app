package org.example.ui;

import org.example.catalog.Priority;
import org.example.catalog.Status;
import org.example.model.ClassicUser;
import org.example.model.Item;
import org.example.model.PremiumUser;
import org.example.model.Reminder;
import org.example.model.Task;
import org.example.model.User;
import org.example.patterns.EmailNotification;
import org.example.patterns.MensajeTextoNotification;
import org.example.patterns.NotificationStrategy;
import org.example.service.AuthService;
import org.example.service.UserService;
import org.example.thread.TaskWorker;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

// Menu principal de consola. Maneja el login flexible, la eleccion
// de tipo de usuario, y todas las operaciones sobre Tasks/Reminders.
// Es donde se dispara la concurrencia real cuando se cambia el estado
// de una Task compartida.
public class MainMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final UserService userService;
    private final AuthService authService;
    // Mapa thread-safe de todos los items del sistema (clave = id del item).
    private final ConcurrentHashMap<Integer, Item> items = new ConcurrentHashMap<>();
    // Contadores atomicos: generan ids unicos aunque varios hilos compitan.
    private final AtomicInteger idCounter = new AtomicInteger(1);
    private final AtomicInteger nextUserId = new AtomicInteger(100);
    private User currentUser;

    public MainMenu() {
        this.userService = new UserService();
        this.authService = new AuthService(userService);
        seedUsers();
    }

    // Crea 3 usuarios de prueba para que el evaluador pueda probar rapido.
    private void seedUsers() {
        userService.register(new PremiumUser(1, "Angel", "angel@mail.com", "1234"));
        userService.register(new ClassicUser(2, "Maria", "maria@mail.com", "abcd", 5, 3));
        userService.register(new ClassicUser(3, "Pedro", "pedro@mail.com", "pass", 5, 3));
    }

    // Punto de entrada: muestra bienvenida, hace login y arranca el bucle.
    public void start() {
        System.out.println("==========================================");
        System.out.println("    TODO COLLABORATIVE APP - Consola");
        System.out.println("==========================================");
        System.out.println("Podes ingresar email y contrasena,");
        System.out.println();

        if (!login()) {
            System.out.println("Saliendo del sistema.");
            return;
        }
        mainLoop();
    }

    // Pide email + contrasena, despues pregunta el tipo de usuario
    // (Classic o Premium) y crea/registra una instancia nueva.
    private boolean login() {
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        if (email.isEmpty()) {
            System.out.println("El email no puede estar vacio.");
            return false;
        }

        System.out.print("Contrasena: ");
        String password = scanner.nextLine().trim();
        if (password.isEmpty()) {
            System.out.println("La contrasena no puede estar vacia.");
            return false;
        }

        User user = createUserOfChosenType(email, password);
        if (user == null) return false;

        userService.register(user);
        this.currentUser = user;

        System.out.println("\nBienvenido " + user.getName()
                + " (" + user.getClass().getSimpleName() + ")\n");
        return true;
    }

    // Pregunta si quiere ser Classic o Premium y construye el User.
    // Si el email ya existe, reusa el id; si no, genera uno nuevo.
    private User createUserOfChosenType(String email, String password) {
        System.out.println("\nQue tipo de usuario quieres usar para esta sesion?");
        System.out.println("  1. ClassicUser  (limite de 5 items y 3 colaboradores)");
        System.out.println("  2. PremiumUser  (sin limites)");
        System.out.print("Opcion: ");
        String option = scanner.nextLine().trim();

        // El nombre se deriva del email (parte antes del @, capitalizada).
        String name = email.contains("@")
                ? email.substring(0, email.indexOf("@"))
                : email;
        if (!name.isEmpty()) {
            name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
        }

        User existing = userService.findByEmail(email);
        int id = (existing != null) ? existing.getId() : nextUserId.getAndIncrement();

        return switch (option) {
            case "1" -> new ClassicUser(id, name, email, password, 5, 3);
            case "2" -> new PremiumUser(id, name, email, password);
            default -> {
                System.out.println("Opcion invalida. Debes elegir 1 (Classic) o 2 (Premium).");
                yield null;
            }
        };
    }

    // Bucle principal del menu: muestra opciones y despacha a los handlers.
    private void mainLoop() {
        while (true) {
            System.out.println("\n========== MENU PRINCIPAL ==========");
            System.out.println("1. Crear Task");
            System.out.println("2. Crear Reminder          (Strategy)");
            System.out.println("3. Compartir item          (synchronized)");
            System.out.println("4. Cambiar estado de Task  (synchronized + hilos si la task esta compartida)");
            System.out.println("5. Disparar alerta Reminder");
            System.out.println("6. Ver mis items creados");
            System.out.println("7. Ver items compartidos conmigo");
            System.out.println("8. Ver todos los items del sistema");
            System.out.println("9. Cambiar de usuario");
            System.out.println("0. Salir");
            System.out.print("Opcion: ");
            String option = scanner.nextLine().trim();

            switch (option) {
                case "1" -> createTask();
                case "2" -> createReminder();
                case "3" -> shareItem();
                case "4" -> changeTaskStatus();
                case "5" -> triggerReminder();
                case "6" -> currentUser.printCreatedItems();
                case "7" -> currentUser.printSharedItems();
                case "8" -> listAllItems();
                case "9" -> {
                    // Cambiar de usuario. Si el login falla o se cancela
                    // (email/contrasena vacios o tipo invalido), nos
                    // quedamos con el usuario actual y volvemos al menu.
                    // El programa SOLO se cierra con la opcion 0.
                    if (!login()) {
                        System.out.println("Cambio de usuario cancelado. Sigues como "
                                + currentUser.getName() + " ("
                                + currentUser.getClass().getSimpleName() + ").");
                    }
                }
                case "0" -> { System.out.println("Hasta luego!"); return; }
                default -> System.out.println("Opcion invalida.");
            }
        }
    }

    // Crea una Task nueva con id atomico, la agrega al usuario y al mapa.
    private void createTask() {
        System.out.print("Titulo: ");
        String title = scanner.nextLine();
        System.out.print("Descripcion: ");
        String desc = scanner.nextLine();
        Priority priority = readPriority();

        Task task = new Task(idCounter.getAndIncrement(), title, desc, priority, currentUser);

        // addItem puede devolver false si es ClassicUser y supera el limite.
        if (currentUser.addItem(task)) {
            items.put(task.getId(), task);
            System.out.println("Task creada con ID " + task.getId());
        }
    }

    // Crea un Reminder pidiendo tambien la estrategia de notificacion
    // (esto es el patron Strategy en accion).
    private void createReminder() {
        System.out.print("Titulo: ");
        String title = scanner.nextLine();
        System.out.print("Descripcion: ");
        String desc = scanner.nextLine();
        Priority priority = readPriority();
        System.out.print("Fecha/Hora (ej 2026-05-15 10:00): ");
        String dateTime = scanner.nextLine();

        Reminder reminder = new Reminder(idCounter.getAndIncrement(), title, desc, priority, currentUser, dateTime);

        // STRATEGY: se elige una implementacion concreta en runtime.
        System.out.println("Estrategia de notificacion: 1=Email 2=Mensaje de texto");
        System.out.print("Opcion: ");
        NotificationStrategy strategy = switch (scanner.nextLine().trim()) {
            case "2" -> new MensajeTextoNotification();
            default -> new EmailNotification();
        };
        reminder.setNotificationStrategy(strategy);

        if (currentUser.addItem(reminder)) {
            items.put(reminder.getId(), reminder);
            System.out.println("Reminder creado con ID " + reminder.getId());
        }
    }

    // Comparte un item con otro usuario (solo el owner puede compartir).
    private void shareItem() {
        System.out.print("ID del item: ");
        Item item = items.get(readInt());
        if (item == null) { System.out.println("Item no encontrado."); return; }
        if (!item.getOwner().equals(currentUser)) {
            System.out.println("Solo el owner puede compartir.");
            return;
        }
        System.out.print("Email del colaborador: ");
        User colab = userService.findByEmail(scanner.nextLine().trim());
        if (colab == null) { System.out.println("Usuario no encontrado."); return; }
        if (colab.equals(currentUser)) { System.out.println("No te podes compartir contigo mismo."); return; }
        currentUser.shareItem(item, colab);
    }

    // Cambia el estado de una Task. Si la task esta compartida, dispara
    // la concurrencia real: un hilo por colaborador + uno del usuario actual,
    // todos compitiendo por el monitor que sincroniza modifyStatus.
    private void changeTaskStatus() {
        System.out.print("ID de la Task: ");
        Item item = items.get(readInt());
        if (!(item instanceof Task task)) { System.out.println("No es una Task valida."); return; }
        System.out.println("Estado actual: " + task.getStatus());
        System.out.println("Nuevo: 1=PENDING 2=IN_PROGRESS 3=COMPLETED 4=CANCELED");
        System.out.print("Opcion: ");
        Status s = switch (scanner.nextLine().trim()) {
            case "2" -> Status.IN_PROGRESS;
            case "3" -> Status.COMPLETED;
            case "4" -> Status.CANCELED;
            default -> Status.PENDING;
        };

        // Caso simple: la task no esta compartida, solo el currentUser modifica.
        if (!task.isShared()) {
            task.modifyStatus(s);
            return;
        }

        // Caso interesante: la task ESTA compartida. Aca disparamos los hilos.
        // Cada colaborador (mas el current user) intenta cambiar el estado al
        // mismo tiempo en su propio hilo. synchronized en Task.modifyStatus
        // garantiza que solo uno entra a la seccion critica a la vez.
        System.out.println("\n[Concurrencia] La task esta compartida con "
                + task.getCollaborators().size() + " colaborador(es).");
        System.out.println("[Concurrencia] Cada colaborador intentara cambiar el");
        System.out.println("               estado en paralelo. synchronized los serializa.\n");

        List<Thread> hilos = new ArrayList<>();

        // 1. Hilo del usuario actual con el estado que eligio.
        Thread tMine = new Thread(new TaskWorker(task, s, currentUser),
                "Hilo-" + currentUser.getName());
        hilos.add(tMine);

        // 2. Un hilo por cada colaborador, con un estado distinto rotando.
        Status[] otros = { Status.IN_PROGRESS, Status.COMPLETED, Status.PENDING, Status.CANCELED };
        int idx = 0;
        for (User colab : task.getCollaborators()) {
            Status alt = otros[idx % otros.length];
            // Si el alternativo coincide con el del current user, agarra otro.
            if (alt == s) alt = otros[(idx + 1) % otros.length];
            hilos.add(new Thread(new TaskWorker(task, alt, colab),
                    "Hilo-" + colab.getName()));
            idx++;
        }

        // Arrancamos todos en paralelo.
        for (Thread t : hilos) t.start();
        // Esperamos a que TODOS terminen antes de imprimir el resultado final.
        try {
            for (Thread t : hilos) t.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n[Concurrencia] Estado final tras los " + hilos.size()
                + " hilos: " + task.getStatus());
    }

    // Dispara la alerta de un Reminder (usa la NotificationStrategy
    // configurada para entregar el mensaje).
    private void triggerReminder() {
        System.out.print("ID del Reminder: ");
        Item item = items.get(readInt());
        if (!(item instanceof Reminder r)) { System.out.println("No es un Reminder."); return; }
        r.notifyAlert();
    }

    // Imprime todos los items del sistema con sus detalles y colaboradores.
    private void listAllItems() {
        List<Item> all = new ArrayList<>(items.values());
        if (all.isEmpty()) { System.out.println("No hay items."); return; }
        for (Item i : all) {
            i.showDetails();
            i.showCollaborators();
            System.out.println();
        }
    }

    // Helper: pide la prioridad por consola.
    private Priority readPriority() {
        System.out.println("Prioridad: 1=HIGH 2=MEDIUM 3=LOW");
        System.out.print("Opcion: ");
        return switch (scanner.nextLine().trim()) {
            case "1" -> Priority.HIGH;
            case "3" -> Priority.LOW;
            default -> Priority.MEDIUM;
        };
    }

    // Helper: lee un int de la consola, devuelve -1 si no es un numero.
    private int readInt() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
