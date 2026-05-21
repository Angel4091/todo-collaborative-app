package org.example.patterns;

/**
 * Estrategia concreta del PATRON STRATEGY que entrega el mensaje por
 * correo electronico (simulado por consola con el prefijo [EMAIL]).
 */
public class EmailNotification implements NotificationStrategy {
    @Override
    public void notify(String message) {
        System.out.println("[EMAIL] " + message);
    }
}
