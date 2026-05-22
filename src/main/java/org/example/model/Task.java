package org.example.model;

import org.example.catalog.Priority;
import org.example.catalog.Status;

/**
 * Subclase de {@link Item} que representa una tarea con un estado
 * ({@link Status}).
 *
 * <p>Es la clase central de la concurrencia: cuando varios hilos
 * intentan cambiar el estado al mismo tiempo, el metodo
 * {@link #modifyStatus(Status)} es {@code synchronized}, por lo que
 * el JVM serializa los accesos automaticamente. El primer hilo
 * adquiere el monitor; los demas quedan en estado {@code BLOCKED}
 * hasta que se libere.</p>
 */
public class Task extends Item {

    /** Estado actual de la task. Arranca en {@link Status#PENDING}. */
    private Status status;

    /**
     * Crea una Task que arranca en {@link Status#PENDING}.
     *
     * @param id          identificador unico
     * @param title       titulo
     * @param description descripcion
     * @param priority    prioridad
     * @param owner       usuario propietario
     */
    public Task(int id, String title, String description, Priority priority, User owner) {
        this(id, title, description, priority, owner, Status.PENDING);
    }

    /**
     * Crea una Task con el estado inicial elegido por el usuario.
     * Permite, por ejemplo, registrar una task que ya empezo en
     * {@link Status#IN_PROGRESS}.
     *
     * @param id          identificador unico
     * @param title       titulo
     * @param description descripcion
     * @param priority    prioridad
     * @param owner       usuario propietario
     * @param status      estado inicial
     */
    public Task(int id, String title, String description, Priority priority, User owner, Status status) {
        super(id, title, description, priority, owner);
        this.status = status;
    }

    /** Imprime por consola los detalles de la task. */
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

    /** @return estado actual de la task */
    public Status getStatus() {
        return status;
    }

    /**
     * Cambia el estado de la task. El metodo es {@code synchronized}
     * para que solo un hilo a la vez pueda entrar a modificar el
     * campo. Si otro hilo llama a este metodo mientras esta en uso,
     * el JVM lo bloquea automaticamente (estado {@code BLOCKED})
     * hasta que el monitor se libere.
     *
     * @param newStatus nuevo estado a asignar
     */
    public synchronized void modifyStatus(Status newStatus) {
        Status oldStatus = this.status;
        this.status = newStatus;
        System.out.println("[" + Thread.currentThread().getName() + "] Task '"
                + title + "': " + oldStatus + " -> " + newStatus);
    }
}
