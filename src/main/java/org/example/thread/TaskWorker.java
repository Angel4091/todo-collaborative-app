package org.example.thread;

import org.example.model.Item;
import org.example.model.User;

public class TaskWorker implements Runnable {
    private final Item task;
    private final String action;
    private final User user;

    public TaskWorker(Item task, String action, User user) {
        this.task = task;
        this.action = action;
        this.user = user;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(150);
            System.out.println("[" + Thread.currentThread().getName()
                    + "] Notificando a " + user.getName()
                    + " (" + action + ") sobre item: " + task.getTitle());
            user.update(task);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("TaskWorker interrumpido: " + e.getMessage());
        }
    }
}
