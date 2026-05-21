package org.example.thread;

import org.example.catalog.Status;
import org.example.model.Task;
import org.example.model.User;

// Envuelve la accion "cambiar el estado de una Task" para que se
// pueda ejecutar en un Thread aparte. Implementa Runnable: el codigo
// del metodo run() es lo que corre en el hilo nuevo.
// Es la pieza de concurrencia: se crean varios TaskWorker y se
// arrancan en paralelo con new Thread(taskWorker).start().
public class TaskWorker implements Runnable {
    private final Task task;            // la task compartida que se quiere modificar
    private final Status newStatus;     // el nuevo estado al que se quiere transicionar
    private final User user;            // el colaborador que dispara el cambio (para el log)

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
            // Pausa de 100ms: aumenta la chance de colision con otros
            // hilos asi se ve mas claro como synchronized los serializa.
            Thread.sleep(100);
            // Llamada al metodo synchronized: aca es donde se hace
            // la cola si hay otros hilos compitiendo.
            task.modifyStatus(newStatus);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("TaskWorker interrumpido: " + e.getMessage());
        }
    }
}
