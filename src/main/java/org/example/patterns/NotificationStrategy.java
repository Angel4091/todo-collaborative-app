package org.example.patterns;

/**
 * Contrato del PATRON STRATEGY de notificaciones.
 *
 * <p>Cada implementacion concreta ({@link EmailNotification},
 * {@link MensajeTextoNotification}) define COMO se entrega un mensaje
 * de notificacion. Un {@link org.example.model.Reminder} mantiene una
 * referencia a una {@code NotificationStrategy} y delega en ella sin
 * conocer la implementacion concreta. Esto permite cambiar el canal
 * de notificacion en tiempo de ejecucion con
 * {@code reminder.setNotificationStrategy(...)} sin tocar la clase
 * Reminder.</p>
 *
 * <p>Es el UNICO patron de diseno del proyecto (por indicacion del
 * catedratico).</p>
 */
public interface NotificationStrategy {
    /**
     * Entrega un mensaje de notificacion por el canal concreto que
     * implementa cada estrategia.
     *
     * @param message texto del mensaje a notificar
     */
    void notify(String message);
}
