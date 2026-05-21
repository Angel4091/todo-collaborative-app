package org.example.patterns;

/**
 * Estrategia concreta del PATRON STRATEGY que entrega el mensaje por
 * mensaje de texto (simulado por consola con el prefijo
 * [MENSAJE DE TEXTO]).
 */
public class MensajeTextoNotification implements NotificationStrategy {
    @Override
    public void notify(String message) {
        System.out.println("[MENSAJE DE TEXTO] " + message);
    }
}
