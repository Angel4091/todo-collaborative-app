package org.example.model;

import org.example.catalog.Priority;
import org.example.patterns.NotificationStrategy;

// Subclase de Item que representa un recordatorio.
// Es donde se aplica el PATRON STRATEGY: la estrategia de notificacion
// (Email o Mensaje de Texto) se inyecta y se puede cambiar en runtime.
public class Reminder extends Item {
    private String dateTime;
    // La estrategia decide COMO se entrega el aviso.
    private NotificationStrategy notificationStrategy;

    public Reminder(int id, String title, String description, Priority priority, User owner, String dateTime) {
        super(id, title, description, priority, owner);
        this.dateTime = dateTime;
    }

    @Override
    public void showDetails() {
        System.out.println("=== REMINDER ===");
        System.out.println("ID         : " + id);
        System.out.println("Titulo     : " + title);
        System.out.println("Descripcion: " + description);
        System.out.println("Prioridad  : " + priority);
        System.out.println("Fecha/Hora : " + dateTime);
        System.out.println("Owner      : " + owner.getName());
        System.out.println("Estrategia : " + (notificationStrategy != null ? notificationStrategy.getClass().getSimpleName() : "(sin estrategia)"));
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public NotificationStrategy getNotificationStrategy() {
        return notificationStrategy;
    }

    // Permite cambiar la estrategia en cualquier momento (Strategy pattern).
    public void setNotificationStrategy(NotificationStrategy strategy) {
        this.notificationStrategy = strategy;
    }

    // Dispara la notificacion delegando en la estrategia configurada.
    // Si no hay estrategia, avisa por consola.
    public void notifyAlert() {
        if (notificationStrategy == null) {
            System.out.println("[Reminder '" + title + "'] No hay estrategia de notificacion configurada.");
            return;
        }
        notificationStrategy.notify("Recordatorio '" + title + "' programado para " + dateTime);
    }
}
