package org.example.thread;

import org.example.catalog.Status;
import org.example.model.Task;
import org.example.model.User;

/**
 * Envuelve la accion "cambiar el estado de una {@link Task}" para que
 * se pueda ejecutar en un {@link Thread} aparte. Implementa
 * {@link Runnable}: el codigo de {@link #run()} es lo que corre en
 * el hilo nuevo.
 *
 * <p>Es la pieza de concurrencia del proyecto: se crean varios
 * {@code TaskWorker} y se arrancan en paralelo con
 * {@code new Thread(taskWorker).start()}. Como
 * {@link Task#modifyStatus(Status)} es {@code synchronized}, el JVM
 * serializa los accesos sin necesidad de codigo de retry.</p>
 */
public class TaskWorker implements Runnable {

    /** Task compartida que se quiere modificar. */
    private final Task task;

    /** Nuevo estado al que se quiere transicionar. */
    private final Status newStatus;

    /** Colaborador que dispara el cambio (se usa solo para el log). */
    private final User user;

    /**
     * Crea un TaskWorker listo para correr en su propio hilo.
     *
     * @param task      task compartida que se va a modificar
     * @param newStatus estado al que se quiere transicionar
     * @param user      colaborador que dispara el cambio
     */
    public TaskWorker(Task task, Status newStatus, User user) {
        this.task = task;
        this.newStatus = newStatus;
        this.user = user;
    }

    /**
     * Punto de entrada del hilo. Hace una pausa de 100ms (didactica,
     * para que se vea la colision entre hilos) y despues llama al
     * metodo {@code synchronized} {@link Task#modifyStatus(Status)}.
     * El JVM se encarga del bloqueo y desbloqueo automaticos.
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
