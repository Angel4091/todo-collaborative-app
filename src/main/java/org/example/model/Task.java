package org.example.model;

import org.example.catalog.Priority;
import org.example.catalog.Status;

/**
 * Representa una tarea (Task) del sistema, que extiende de {@link Item} y
 * agrega el atributo {@link Status} con su ciclo de vida
 * (PENDING -> IN_PROGRESS -> COMPLETED / CANCELED).
 *
 * <p>Es la clase central del entregable 2 desde el punto de vista de la
 * concurrencia: cuando una Task esta compartida entre varios colaboradores,
 * cada uno puede intentar cambiar su estado desde un hilo distinto
 * (envuelto en un {@link org.example.thread.TaskWorker}). El metodo
 * {@link #modifyStatus(Status)} esta marcado como {@code synchronized}
 * para que solo un hilo a la vez pueda entrar a la seccion critica.</p>
 *
 * @see Item
 * @see org.example.thread.TaskWorker
 */
public class Task extends Item {
    private Status status;

    /**
     * Construye una Task nueva con status inicial {@link Status#PENDING}.
     */
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

    /**
     * Cambia el estado de la Task de forma thread-safe.
     *
     * <p>Esta marcado como {@code synchronized} para garantizar que solo
     * un hilo a la vez modifique el campo {@code status}. Cuando dos
     * colaboradores intentan cambiar el estado de la misma Task en
     * paralelo, el monitor del objeto los serializa: el primero entra,
     * cambia el estado, sale, y recien ahi el segundo puede entrar.
     * Asi se evita estado intermedio corrupto.</p>
     *
     * @param newStatus el nuevo estado al que se quiere transicionar
     */
    public synchronized void modifyStatus(Status newStatus) {
        Status oldStatus = this.status;
        this.status = newStatus;
        System.out.println("[" + Thread.currentThread().getName() + "] Task '"
                + title + "': " + oldStatus + " -> " + newStatus);
    }
}
