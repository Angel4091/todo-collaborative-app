package org.example.patterns;

/**
 * Interfaz del PATRON STRATEGY de notificaciones.
 *
 * <p>Cada implementacion concreta ({@link EmailNotification},
 * {@link MensajeTextoNotification}) define COMO se entrega un mensaje
 * de notificacion. Un {@link org.example.model.Reminder} mantiene una
 * referencia a una de estas estrategias y delega en ella sin saber
 * cual es la implementacion concreta.</p>
 *
 * <p>Es el UNICO patron de diseno del proyecto.</p>
 */
public interface NotificationStrategy {

    /**
     * Entrega el mensaje por el canal concreto que implemente la
     * estrategia.
     *
     * @param message texto del mensaje a notificar
     */
    void notify(String message);
}
