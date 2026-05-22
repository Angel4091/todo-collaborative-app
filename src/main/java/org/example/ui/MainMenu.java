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
import org.example.thread.TaskWorker;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Menu principal de la aplicacion en modo consola.
 *
 * <p>Maneja el login flexible, la eleccion de tipo de usuario
 * (Classic / Premium), y todas las operaciones sobre Tasks y
 * Reminders. Es donde se dispara la concurrencia real cuando se
 * cambia el estado de una Task compartida.</p>
 *
 * <p>El storage de usuarios y de items se mantiene aca mismo
 * usando {@link ConcurrentHashMap} (thread-safe), sin pasar por
 * una capa de servicios extra.</p>
 */
public class MainMenu {

    /** Scanner unico para leer de la consola. */
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Mapa thread-safe de usuarios registrados (clave: email).
     * Soporta acceso concurrente desde varios hilos sin trabarse.
     */
    private final ConcurrentHashMap<String, User> usersByEmail = new ConcurrentHashMap<>();

    /**
     * Mapa thread-safe de items del sistema (clave: id del item).
     * Contiene Tasks y Reminders.
     */
    private final ConcurrentHashMap<Integer, Item> items = new ConcurrentHashMap<>();

    /** Generador atomico de ids para items (thread-safe). */
    private final AtomicInteger idCounter = new AtomicInteger(1);

    /** Generador atomico de ids para usuarios creados en runtime. */
    private final AtomicInteger nextUserId = new AtomicInteger(100);

    /** Usuario actualmente logueado. */
    private User currentUser;

    /**
     * Construye el MainMenu y carga usuarios de prueba para que el
     * evaluador pueda probar rapido.
     */
    public MainMenu() {
        seedUsers();
    }

    /**
     * Registra 3 usuarios pre-cargados: uno Premium (Angel) y dos
     * Classic (Maria, Pedro).
     */
    private void seedUsers() {
        register(new PremiumUser(1, "Angel", "angel@mail.com", "1234"));
        register(new ClassicUser(2, "Maria", "maria@mail.com", "abcd", 5, 3));
        register(new ClassicUser(3, "Pedro", "pedro@mail.com", "pass", 5, 3));
    }

    /**
     * Guarda (o reemplaza) un usuario en el mapa por su email.
     *
     * @param user usuario a registrar
     */
    private void register(User user) {
        usersByEmail.put(user.getEmail(), user);
    }

    /**
     * Punto de entrada del menu. Muestra la bienvenida, pide login
     * y, si es exitoso, entra al bucle principal.
     */
    public void start() {
        System.out.println("==========================================");
        System.out.println("    TODO COLLABORATIVE APP - Consola");
        System.out.println("==========================================");
        System.out.println("Podes ingresar cualquier email y contrasena,");
        System.out.println("o usar uno de los usuarios pre-cargados:");
        System.out.println("  - angel@mail.com  (Angel)");
        System.out.println("  - maria@mail.com  (Maria)");
        System.out.println("  - pedro@mail.com  (Pedro)");
        System.out.println();

        if (!login()) {
            System.out.println("Saliendo del sistema.");
            return;
        }
        mainLoop();
    }

    /**
     * Pide email + contrasena, despues pregunta el tipo de usuario
     * (Classic o Premium) y crea/registra una instancia nueva.
     *
     * @return {@code true} si el login fue exitoso
     */
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

        register(user);
        this.currentUser = user;

        System.out.println("\nBienvenido " + user.getName()
                + " (" + user.getClass().getSimpleName() + ")\n");
        return true;
    }

    /**
     * Pregunta al usuario si quiere ser Classic o Premium y devuelve
     * la instancia correspondiente. Si el email ya existe, reusa su
     * id; si no, genera uno nuevo.
     *
     * @param email    email del usuario
     * @param password password del usuario
     * @return el {@link User} construido, o {@code null} si la opcion
     *         es invalida
     */
    private User createUserOfChosenType(String email, String password) {
        System.out.println("\nQue tipo de usuario quieres usar para esta sesion?");
        System.out.println("  1. ClassicUser  (limite de 5 items y 3 colaboradores)");
        System.out.println("  2. PremiumUser  (sin limites)");
        System.out.print("Opcion: ");
        String option = scanner.nextLine().trim();

        String name = email.contains("@")
                ? email.substring(0, email.indexOf("@"))
                : email;
        if (!name.isEmpty()) {
            name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
        }

        User existing = usersByEmail.get(email);
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

    /**
     * Bucle principal del menu: muestra opciones y despacha a los
     * handlers. Solo termina con la opcion 0.
     */
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
                    // Cambiar de usuario. Si el login falla o se cancela,
                    // nos quedamos con el usuario actual y volvemos al menu.
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

    /**
     * Handler de la opcion 1. Crea una Task nueva, chequeando primero
     * que el usuario tenga cupo (ClassicUser tiene limite), y deja
     * elegir el estado inicial (PENDING por defecto).
     */
    private void createTask() {
        if (!currentUser.canAddItem()) {
            System.out.println("[Limite alcanzado] No puedes crear mas items. "
                    + "Tu cuenta (" + currentUser.getClass().getSimpleName()
                    + ") ya llego al tope.");
            return;
        }

        System.out.print("Titulo: ");
        String title = scanner.nextLine();
        System.out.print("Descripcion: ");
        String desc = scanner.nextLine();
        Priority priority = readPriority();
        Status initialStatus = readInitialStatus();

        Task task = new Task(idCounter.getAndIncrement(), title, desc, priority, currentUser, initialStatus);

        if (currentUser.addItem(task)) {
            items.put(task.getId(), task);
            System.out.println("Task creada con ID " + task.getId()
                    + " (estado inicial: " + task.getStatus() + ")");
        }
    }

    /**
     * Handler de la opcion 2. Crea un Reminder pidiendo titulo,
     * descripcion, prioridad, fecha/hora y la estrategia de
     * notificacion (esto ultimo es el patron Strategy en accion).
     */
    private void createReminder() {
        if (!currentUser.canAddItem()) {
            System.out.println("[Limite alcanzado] No puedes crear mas items. "
                    + "Tu cuenta (" + currentUser.getClass().getSimpleName()
                    + ") ya llego al tope.");
            return;
        }

        System.out.print("Titulo: ");
        String title = scanner.nextLine();
        System.out.print("Descripcion: ");
        String desc = scanner.nextLine();
        Priority priority = readPriority();
        System.out.print("Fecha/Hora (ej 2026-05-15 10:00): ");
        String dateTime = scanner.nextLine();

        Reminder reminder = new Reminder(idCounter.getAndIncrement(), title, desc, priority, currentUser, dateTime);

        NotificationStrategy strategy = readNotificationStrategy();
        reminder.setNotificationStrategy(strategy);

        if (currentUser.addItem(reminder)) {
            items.put(reminder.getId(), reminder);
            System.out.println("Reminder creado con ID " + reminder.getId());
        }
    }

    /**
     * Handler de la opcion 3. Comparte un item con otro usuario.
     * Solo el owner puede compartir, y no se puede compartir con
     * uno mismo.
     */
    private void shareItem() {
        System.out.print("ID del item: ");
        Item item = items.get(readInt());
        if (item == null) { System.out.println("Item no encontrado."); return; }
        if (!item.getOwner().equals(currentUser)) {
            System.out.println("Solo el owner puede compartir.");
            return;
        }
        System.out.print("Email del colaborador: ");
        User colab = usersByEmail.get(scanner.nextLine().trim());
        if (colab == null) { System.out.println("Usuario no encontrado."); return; }
        if (colab.equals(currentUser)) { System.out.println("No te podes compartir contigo mismo."); return; }
        currentUser.shareItem(item, colab);
    }

    /**
     * Handler de la opcion 4. Cambia el estado de una Task.
     *
     * <p>Si la task NO esta compartida, hace el cambio en el hilo
     * actual. Si ESTA compartida, lanza un hilo por cada colaborador
     * (mas uno para el usuario actual), cada uno proponiendo un
     * estado. El {@code synchronized} en {@link Task#modifyStatus}
     * serializa los accesos: solo un hilo modifica a la vez, los
     * demas quedan en estado BLOCKED hasta que se libere el
     * monitor.</p>
     */
    private void changeTaskStatus() {
        System.out.print("ID de la Task: ");
        Item item = items.get(readInt());
        if (!(item instanceof Task task)) { System.out.println("No es una Task valida."); return; }
        System.out.println("Estado actual: " + task.getStatus());
        Status s = readStatus();

        // Caso simple: la task no esta compartida. Solo el currentUser modifica.
        if (!task.isShared()) {
            task.modifyStatus(s);
            return;
        }

        // Caso interesante: la task ESTA compartida. Disparamos los hilos.
        System.out.println("\n[Concurrencia] La task esta compartida con "
                + task.getCollaborators().size() + " colaborador(es).");
        System.out.println("[Concurrencia] Cada colaborador intentara cambiar el");
        System.out.println("               estado en paralelo. synchronized los serializa.\n");

        List<Thread> hilos = new ArrayList<>();

        // 1. Hilo del usuario actual con el estado que eligio.
        Thread tMine = new Thread(new TaskWorker(task, s, currentUser),
                "Hilo-" + currentUser.getName());
        hilos.add(tMine);

        // 2. Un hilo por cada colaborador con un estado distinto rotando.
        Status[] otros = { Status.IN_PROGRESS, Status.COMPLETED, Status.PENDING, Status.CANCELED };
        int idx = 0;
        for (User colab : task.getCollaborators()) {
            Status alt = otros[idx % otros.length];
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

    /**
     * Handler de la opcion 5. Dispara la alerta de un Reminder
     * usando la {@link NotificationStrategy} configurada.
     */
    private void triggerReminder() {
        System.out.print("ID del Reminder: ");
        Item item = items.get(readInt());
        if (!(item instanceof Reminder r)) { System.out.println("No es un Reminder."); return; }
        r.notifyAlert();
    }

    /**
     * Handler de la opcion 8. Imprime todos los items del sistema
     * con sus detalles y colaboradores.
     */
    private void listAllItems() {
        List<Item> all = new ArrayList<>(items.values());
        if (all.isEmpty()) { System.out.println("No hay items."); return; }
        for (Item i : all) {
            i.showDetails();
            i.showCollaborators();
            System.out.println();
        }
    }

    /**
     * Helper que pide la prioridad por consola y valida el input.
     * Si el usuario escribe algo invalido, vuelve a preguntar en
     * bucle hasta que ingrese 1, 2 o 3.
     *
     * @return la {@link Priority} elegida
     */
    private Priority readPriority() {
        while (true) {
            System.out.println("Prioridad: 1=HIGH 2=MEDIUM 3=LOW");
            System.out.print("Opcion: ");
            switch (scanner.nextLine().trim()) {
                case "1": return Priority.HIGH;
                case "2": return Priority.MEDIUM;
                case "3": return Priority.LOW;
                default: System.out.println("Opcion invalida. Debes elegir 1, 2 o 3.\n");
            }
        }
    }

    /**
     * Helper que pide un {@link Status} valido (1/2/3/4) y vuelve
     * a preguntar si el input es invalido.
     *
     * @return el {@link Status} elegido
     */
    private Status readStatus() {
        while (true) {
            System.out.println("Estado: 1=PENDING 2=IN_PROGRESS 3=COMPLETED 4=CANCELED");
            System.out.print("Opcion: ");
            switch (scanner.nextLine().trim()) {
                case "1": return Status.PENDING;
                case "2": return Status.IN_PROGRESS;
                case "3": return Status.COMPLETED;
                case "4": return Status.CANCELED;
                default: System.out.println("Opcion invalida. Debes elegir 1, 2, 3 o 4.\n");
            }
        }
    }

    /**
     * Helper que pide el estado inicial al crear una Task. Si el
     * usuario aprieta Enter sin escribir, asume {@link Status#PENDING}.
     *
     * @return el {@link Status} elegido (o PENDING por defecto)
     */
    private Status readInitialStatus() {
        while (true) {
            System.out.println("Estado inicial: 1=PENDING 2=IN_PROGRESS 3=COMPLETED 4=CANCELED");
            System.out.print("Opcion (Enter = PENDING): ");
            String opt = scanner.nextLine().trim();
            if (opt.isEmpty()) return Status.PENDING;
            switch (opt) {
                case "1": return Status.PENDING;
                case "2": return Status.IN_PROGRESS;
                case "3": return Status.COMPLETED;
                case "4": return Status.CANCELED;
                default: System.out.println("Opcion invalida. Debes elegir 1, 2, 3 o 4 (o Enter).\n");
            }
        }
    }

    /**
     * Helper que pide la estrategia de notificacion para un Reminder.
     * Solo acepta 1 (Email) o 2 (Mensaje de texto); con cualquier
     * otra cosa vuelve a preguntar.
     *
     * @return la {@link NotificationStrategy} elegida
     */
    private NotificationStrategy readNotificationStrategy() {
        while (true) {
            System.out.println("Estrategia de notificacion: 1=Email 2=Mensaje de texto");
            System.out.print("Opcion: ");
            switch (scanner.nextLine().trim()) {
                case "1": return new EmailNotification();
                case "2": return new MensajeTextoNotification();
                default: System.out.println("Opcion invalida. Debes elegir 1 o 2.\n");
            }
        }
    }

    /**
     * Helper que lee un entero de la consola. Si lo que el usuario
     * tipea no es un numero, devuelve -1.
     *
     * @return el numero leido, o -1 si el input es invalido
     */
    private int readInt() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
