package org.example.model;

import org.example.catalog.Priority;
import org.example.catalog.Status;

public class Task extends Item {
    private Status status;

    public Task(int id, String title, String description, Priority priority, User owner) {
        super(id, title, description, priority, owner);
        this.status = Status.PENDING;
    }

    @Override
    public void showDetails() {
        System.out.println("=== TASK ===");
        System.out.println("ID         : " + id);
        System.out.println("Titulo     : " + title);
        System.out.println("Descripcion: " + description);
        System.out.println("Prioridad  : " + priority);
        System.out.println("Estado     : " + status);
        System.out.println("Owner      : " + owner.getName());
        System.out.println("Compartida : " + isShared());
    }

    public Status getStatus() {
        return status;
    }

    public synchronized void modifyStatus(Status newStatus) {
        Status oldStatus = this.status;
        this.status = newStatus;
        System.out.println("[" + Thread.currentThread().getName() + "] Task '"
                + title + "': " + oldStatus + " -> " + newStatus);
    }
}
