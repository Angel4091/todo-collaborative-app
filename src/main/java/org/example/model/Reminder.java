package org.example.model;

import org.example.catalog.Priority;
import org.example.patterns.NotificationStrategy;

public class Reminder extends Item {
    private String dateTime;
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

    public void setNotificationStrategy(NotificationStrategy strategy) {
        this.notificationStrategy = strategy;
    }

    public void notifyAlert() {
        if (notificationStrategy == null) {
            System.out.println("[Reminder '" + title + "'] No hay estrategia de notificacion configurada.");
            return;
        }
        notificationStrategy.notify("Recordatorio '" + title + "' programado para " + dateTime);
    }
}
