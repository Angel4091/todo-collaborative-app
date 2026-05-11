package org.example.ui;

import org.example.catalog.Priority;
import org.example.catalog.Status;
import org.example.model.Item;
import org.example.model.Task;
import org.example.model.User;
import org.example.patterns.ItemFactory;
import org.example.patterns.TaskFactory;
import org.example.service.TaskManager;
import org.example.service.UserService;

import java.util.concurrent.atomic.AtomicInteger;

public class DemoRunner {
    private final UserService userService;
    private final TaskManager taskManager;
    private final AtomicInteger idCounter;

    public DemoRunner(UserService userService, TaskManager taskManager, AtomicInteger idCounter) {
        this.userService = userService;
        this.taskManager = taskManager;
        this.idCounter = idCounter;
    }

    public void runDemo() {
        System.out.println("\n========== DEMO CONCURRENCIA ==========");
        User angel = userService.findByEmail("angel@mail.com");
        User maria = userService.findByEmail("maria@mail.com");
        User pedro = userService.findByEmail("pedro@mail.com");

        ItemFactory factory = new TaskFactory();
        Task t1 = (Task) factory.createItem(idCounter.getAndIncrement(),
                "Sprint Review", "Demo de patrones", Priority.HIGH, angel);

        angel.addItem(t1);
        taskManager.addTask(t1);

        System.out.println("\n>>> Angel comparte con Maria y Pedro:");
        angel.shareItem(t1, maria);
        angel.shareItem(t1, pedro);

        System.out.println("\n>>> Hilo 1 y Hilo 2 cambian estado concurrentemente:");
        Thread h1 = new Thread(() -> t1.modifyStatus(Status.IN_PROGRESS), "Hilo-1");
        Thread h2 = new Thread(() -> t1.modifyStatus(Status.COMPLETED), "Hilo-2");
        h1.start();
        h2.start();

        try {
            h1.join();
            h2.join();
            Thread.sleep(500); // dar tiempo a los TaskWorkers
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n>>> Estado final de la Task:");
        t1.showDetails();
        t1.showCollaborators();
        System.out.println("\n========== FIN DEMO ==========\n");
    }
}
