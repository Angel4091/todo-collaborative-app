package org.example.model;

import org.example.catalog.Priority;
import org.example.catalog.Status;
import org.example.thread.TaskWorker;

import java.util.ArrayList;
import java.util.List;

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
        System.out.println("[Task '" + title + "'] " + oldStatus + " -> " + newStatus);

        List<TaskObserver> snapshot;
        synchronized (observers) {
            snapshot = new ArrayList<>(observers);
        }

        for (TaskObserver obs : snapshot) {
            if (obs instanceof User u) {
                Thread t = new Thread(new TaskWorker(this, "estado=" + newStatus, u));
                t.setName("Notifier-" + u.getName());
                t.start();
            } else {
                obs.update(this);
            }
        }
    }
}
