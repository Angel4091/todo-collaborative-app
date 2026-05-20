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

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MainMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final UserService userService;
    private final AuthService authService;
    private final ConcurrentHashMap<Integer, Item> items = new ConcurrentHashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);
    private User currentUser;

    public MainMenu() {
        this.userService = new UserService();
        this.authService = new AuthService(userService);
        seedUsers();
    }

    private void seedUsers() {
        userService.register(new PremiumUser(1, "Angel", "angel@mail.com", "1234"));
        userService.register(new ClassicUser(2, "Maria", "maria@mail.com", "abcd", 5, 3));
        userService.register(new ClassicUser(3, "Pedro", "pedro@mail.com", "pass", 5, 3));
    }

    public void start() {
        System.out.println("==========================================");
        System.out.println("    TODO COLLABORATIVE APP - Consola");
        System.out.println("==========================================");
        System.out.println("Usuarios de prueba:");
        System.out.println("  - angel@mail.com / 1234     (Premium)");
        System.out.println("  - maria@mail.com / abcd     (Classic)");
        System.out.println("  - pedro@mail.com / pass     (Classic)");
        System.out.println();

        if (!login()) {
            System.out.println("Saliendo del sistema.");
            return;
        }
        mainLoop();
    }

    private boolean login() {
        for (int attempts = 0; attempts < 3; attempts++) {
            System.out.print("Email: ");
            String email = scanner.nextLine().trim();
            System.out.print("Contrasena: ");
            String password = scanner.nextLine().trim();
            User user = authService.login(email, password);
            if (user != null) {
                this.currentUser = chooseUserType(user, password);
                System.out.println("\nBienvenido " + currentUser.getName()
                        + " (" + currentUser.getClass().getSimpleName() + ")\n");
                return true;
            }
            System.out.println("Credenciales incorrectas. Intento " + (attempts + 1) + "/3\n");
        }
        return false;
    }

    private User chooseUserType(User original, String password) {
        System.out.println("\nQue tipo de usuario quieres usar para esta sesion?");
        System.out.println("  1. ClassicUser  (limite de 5 items y 3 colaboradores)");
        System.out.println("  2. PremiumUser  (sin limites)");
        System.out.print("Opcion: ");
        String option = scanner.nextLine().trim();

        return switch (option) {
            case "1" -> new ClassicUser(original.getId(), original.getName(),
                    original.getEmail(), password, 5, 3);
            case "2" -> new PremiumUser(original.getId(), original.getName(),
                    original.getEmail(), password);
            default -> {
                System.out.println("Opcion invalida. Se usa el tipo original ("
                        + original.getClass().getSimpleName() + ").");
                yield original;
            }
        };
    }

    private void mainLoop() {
        while (true) {
            System.out.println("\n========== MENU PRINCIPAL ==========");
            System.out.println("1. Crear Task");
            System.out.println("2. Crear Reminder          (Strategy)");
            System.out.println("3. Compartir item          (synchronized)");
            System.out.println("4. Cambiar estado de Task  (synchronized)");
            System.out.println("5. Disparar alerta Reminder");
            System.out.println("6. Ver mis items creados");
            System.out.println("7. Ver items compartidos conmigo");
            System.out.println("8. Ver todos los items del sistema");
            System.out.println("9. Cambiar de usuario");
            System.out.println("D. Ejecutar DEMO automatico de concurrencia");
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
                case "9" -> { if (login()) continue; else return; }
                case "D", "d" -> new DemoRunner(userService, items, idCounter).runDemo();
                case "0" -> { System.out.println("Hasta luego!"); return; }
                default -> System.out.println("Opcion invalida.");
            }
        }
    }

    private void createTask() {
        System.out.print("Titulo: ");
        String title = scanner.nextLine();
        System.out.print("Descripcion: ");
        String desc = scanner.nextLine();
        Priority priority = readPriority();

        Task task = new Task(idCounter.getAndIncrement(), title, desc, priority, currentUser);

        if (currentUser.addItem(task)) {
            items.put(task.getId(), task);
            System.out.println("Task creada con ID " + task.getId());
        }
    }

    private void createReminder() {
        System.out.print("Titulo: ");
        String title = scanner.nextLine();
        System.out.print("Descripcion: ");
        String desc = scanner.nextLine();
        Priority priority = readPriority();
        System.out.print("Fecha/Hora (ej 2026-05-15 10:00): ");
        String dateTime = scanner.nextLine();

        Reminder reminder = new Reminder(idCounter.getAndIncrement(), title, desc, priority, currentUser, dateTime);

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
        task.modifyStatus(s);
    }

    private void triggerReminder() {
        System.out.print("ID del Reminder: ");
        Item item = items.get(readInt());
        if (!(item instanceof Reminder r)) { System.out.println("No es un Reminder."); return; }
        r.notifyAlert();
    }

    private void listAllItems() {
        List<Item> all = new ArrayList<>(items.values());
        if (all.isEmpty()) { System.out.println("No hay items."); return; }
        for (Item i : all) {
            i.showDetails();
            i.showCollaborators();
            System.out.println();
        }
    }

    private Priority readPriority() {
        System.out.println("Prioridad: 1=HIGH 2=MEDIUM 3=LOW");
        System.out.print("Opcion: ");
        return switch (scanner.nextLine().trim()) {
            case "1" -> Priority.HIGH;
            case "3" -> Priority.LOW;
            default -> Priority.MEDIUM;
        };
    }

    private int readInt() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
