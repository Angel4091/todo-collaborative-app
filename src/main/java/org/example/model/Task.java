package org.example.model;

import org.example.catalog.Priority;
import org.example.catalog.Status;

// Subclase de Item que representa una tarea con un estado (Status).
// Es la clase central de la concurrencia: cuando varios hilos quieren
// cambiar el estado al mismo tiempo, synchronized los serializa.
public class Task extends Item {
    private Status status;

    // Constructor por defecto: arranca en PENDING.
    public Task(int id, String title, String description, Priority priority, User owner) {
        this(id, title, description, priority, owner, Status.PENDING);
    }

    // Constructor con estado inicial elegido por el usuario.
    // Permite crear una Task que ya arranca en IN_PROGRESS si la
    // persona ya empezo a trabajar antes de registrarla en el sistema.
    public Task(int id, String title, String description, Priority priority, User owner, Status status) {
        super(id, title, description, priority, owner);
        this.status = status;
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

    // synchronized = solo un hilo a la vez entra a cambiar el estado.
    // Si dos colaboradores intentan modificar en paralelo, el segundo
    // espera a que el primero termine. Asi no queda el campo corrupto.
    public synchronized void modifyStatus(Status newStatus) {
        Status oldStatus = this.status;
        this.status = newStatus;
        System.out.println("[" + Thread.currentThread().getName() + "] Task '"
                + title + "': " + oldStatus + " -> " + newStatus);
    }
}
