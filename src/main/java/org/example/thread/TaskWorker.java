package org.example.thread;

import org.example.catalog.Status;
import org.example.model.Task;
import org.example.model.User;

public class TaskWorker implements Runnable {
    private final Task task;
    private final Status newStatus;
    private final User user;

    public TaskWorker(Task task, Status newStatus, User user) {
        this.task = task;
        this.newStatus = newStatus;
        this.user = user;
    }

    @Override
    public void run() {
        try {
            System.out.println("[" + Thread.currentThread().getName() + "] "
                    + user.getName() + " intenta cambiar '" + task.getTitle()
                    + "' a " + newStatus);
            Thread.sleep(100);
            task.modifyStatus(newStatus);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("TaskWorker interrumpido: " + e.getMessage());
        }
    }
}
