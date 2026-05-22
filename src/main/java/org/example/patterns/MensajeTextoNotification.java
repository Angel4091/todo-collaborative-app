package org.example.patterns;

/**
 * Estrategia concreta del PATRON STRATEGY. Entrega el mensaje por
 * mensaje de texto (simulado por consola con el prefijo
 * {@code [MENSAJE DE TEXTO]}).
 */
public class MensajeTextoNotification implements NotificationStrategy {

    /**
     * Imprime el mensaje precedido de {@code [MENSAJE DE TEXTO]},
     * simulando un envio de SMS.
     *
     * @param message texto del mensaje
     */
    @Override
    public void notify(String message) {
        System.out.println("[MENSAJE DE TEXTO] " + message);
    }
}
