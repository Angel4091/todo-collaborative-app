package org.example.model;

import org.example.catalog.Priority;
import org.example.patterns.NotificationStrategy;

/**
 * Subclase de {@link Item} que representa un recordatorio con una
 * fecha/hora y un canal de notificacion.
 *
 * <p>Es la clase donde se aplica el PATRON STRATEGY: la
 * {@link NotificationStrategy} se inyecta con
 * {@link #setNotificationStrategy(NotificationStrategy)} y se puede
 * cambiar en tiempo de ejecucion sin tocar esta clase.</p>
 */
public class Reminder extends Item {

    /** Fecha y hora programadas para el recordatorio. */
    private String dateTime;

    /** Estrategia que decide COMO se entrega la notificacion. */
    private NotificationStrategy notificationStrategy;

    /**
     * Crea un Reminder. La estrategia se asigna despues con
     * {@link #setNotificationStrategy(NotificationStrategy)}.
     *
     * @param id          identificador unico
     * @param title       titulo
     * @param description descripcion
     * @param priority    prioridad
     * @param owner       usuario propietario
     * @param dateTime    fecha/hora programadas (formato libre)
     */
    public Reminder(int id, String title, String description, Priority priority, User owner, String dateTime) {
        super(id, title, description, priority, owner);
        this.dateTime = dateTime;
    }

    /** Imprime por consola los detalles del reminder. */
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

    /** @return fecha/hora programadas */
    public String getDateTime() {
        return dateTime;
    }

    /**
     * Cambia la fecha/hora del recordatorio.
     *
     * @param dateTime nueva fecha/hora
     */
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    /** @return la estrategia actualmente asignada (puede ser null) */
    public NotificationStrategy getNotificationStrategy() {
        return notificationStrategy;
    }

    /**
     * Cambia la estrategia de notificacion en runtime
     * (esto es el patron Strategy en accion).
     *
     * @param strategy nueva estrategia de notificacion
     */
    public void setNotificationStrategy(NotificationStrategy strategy) {
        this.notificationStrategy = strategy;
    }

    /**
     * Dispara la alerta del recordatorio delegando en la estrategia
     * configurada. Si no hay estrategia, avisa por consola.
     */
    public void notifyAlert() {
        if (notificationStrategy == null) {
            System.out.println("[Reminder '" + title + "'] No hay estrategia de notificacion configurada.");
            return;
        }
        notificationStrategy.notify("Recordatorio '" + title + "' programado para " + dateTime);
    }
}
