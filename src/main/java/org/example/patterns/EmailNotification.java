package org.example.patterns;

/**
 * Estrategia concreta del PATRON STRATEGY. Entrega el mensaje por
 * correo electronico (simulado por consola con el prefijo
 * {@code [EMAIL]}).
 */
public class EmailNotification implements NotificationStrategy {

    /**
     * Imprime el mensaje precedido de {@code [EMAIL]}, simulando
     * un envio de correo.
     *
     * @param message texto del mensaje
     */
    @Override
    public void notify(String message) {
        System.out.println("[EMAIL] " + message);
    }
}
