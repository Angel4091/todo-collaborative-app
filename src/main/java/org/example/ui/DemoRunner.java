package org.example.ui;

import org.example.catalog.Priority;
import org.example.catalog.Status;
import org.example.model.Item;
import org.example.model.Task;
import org.example.model.User;
import org.example.service.UserService;
import org.example.thread.TaskWorker;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class DemoRunner {
    private final UserService userService;
    private final ConcurrentHashMap<Integer, Item> items;
    private final AtomicInteger idCounter;

    public DemoRunner(UserService userService,
                      ConcurrentHashMap<Integer, Item> items,
                      AtomicInteger idCounter) {
        this.userService = userService;
        this.items = items;
        this.idCounter = idCounter;
    }

    public void runDemo() {
        System.out.println("\n========== DEMO CONCURRENCIA ==========");
        User angel = userService.findByEmail("angel@mail.com");
        User maria = userService.findByEmail("maria@mail.com");
        User pedro = userService.findByEmail("pedro@mail.com");

        Task t1 = new Task(idCounter.getAndIncrement(),
                "Sprint Review", "Demo de concurrencia", Priority.HIGH, angel);

        angel.addItem(t1);
        items.put(t1.getId(), t1);

        System.out.println("\n>>> Angel comparte la task con Maria y Pedro:");
        angel.shareItem(t1, maria);
        angel.shareItem(t1, pedro);

        System.out.println("\n>>> Maria y Pedro intentan cambiar el estado concurrentemente:");
        System.out.println("    (synchronized en modifyStatus garantiza que solo");
        System.out.println("     un hilo a la vez modifique la task compartida)");
        Thread h1 = new Thread(new TaskWorker(t1, Status.IN_PROGRESS, maria), "Hilo-Maria");
        Thread h2 = new Thread(new TaskWorker(t1, Status.COMPLETED, pedro), "Hilo-Pedro");
        h1.start();
        h2.start();

        try {
            h1.join();
            h2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n>>> Estado final de la Task:");
        t1.showDetails();
        t1.showCollaborators();
        System.out.println("\n========== FIN DEMO ==========\n");
    }
}
