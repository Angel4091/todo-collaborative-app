package org.example.thread;

import org.example.catalog.Status;
import org.example.model.Task;
import org.example.model.User;

/**
 * Encapsula la accion de un usuario que quiere cambiar el estado de una
 * {@link Task} compartida, y la prepara para ser ejecutada en su propio
 * hilo (envolviendola en un {@link Thread}).
 *
 * <p>Es la pieza de concurrencia del entregable 2: implementa
 * {@link Runnable} para que se pueda hacer
 * {@code new Thread(new TaskWorker(task, IN_PROGRESS, maria)).start()}
 * y que esa llamada a {@link Task#modifyStatus(Status)} ocurra en
 * paralelo con la de otro hilo. El {@code synchronized} de
 * {@link Task#modifyStatus(Status)} se encarga de serializar los
 * accesos.</p>
 *
 * @see Task#modifyStatus(Status)
 */
public class TaskWorker implements Runnable {
    private final Task task;
    private final Status newStatus;
    private final User user;

    /**
     * @param task      la Task compartida que se quiere modificar
     * @param newStatus el estado al que se quiere transicionar
     * @param user      el colaborador que dispara el cambio (para el log)
     */
    public TaskWorker(Task task, Status newStatus, User user) {
        this.task = task;
        this.newStatus = newStatus;
        this.user = user;
    }

    /**
     * Punto de entrada del hilo. Espera 100ms para aumentar la chance de
     * colision con otros hilos (puramente didactico, hace mas visible la
     * concurrencia) y despues llama a {@link Task#modifyStatus(Status)},
     * que es synchronized.
     */
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
