package org.example.model;

import org.example.catalog.Priority;
import org.example.patterns.NotificationStrategy;

/**
 * Item del tipo recordatorio (Reminder). Ademas de los atributos de
 * {@link Item}, tiene una fecha/hora y una
 * {@link NotificationStrategy} que decide COMO notifica al usuario
 * cuando se dispara la alerta.
 *
 * <p>Esta clase es el punto donde se aplica el PATRON STRATEGY: la
 * estrategia se inyecta con {@link #setNotificationStrategy} y
 * {@link #notifyAlert()} delega en ella sin saber si es Email o
 * Mensaje de Texto.</p>
 */
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

    /**
     * Dispara la notificacion del recordatorio usando la estrategia
     * configurada. Si no hay estrategia asignada, muestra un mensaje
     * de aviso. Es el ejemplo en vivo del PATRON STRATEGY.
     */
    public void notifyAlert() {
        if (notificationStrategy == null) {
            System.out.println("[Reminder '" + title + "'] No hay estrategia de notificacion configurada.");
            return;
        }
        notificationStrategy.notify("Recordatorio '" + title + "' programado para " + dateTime);
    }
}
