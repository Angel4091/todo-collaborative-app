package org.example.patterns;

public class MensajeTextoNotification implements NotificationStrategy {
    @Override
    public void notify(String message) {
        System.out.println("[MENSAJE DE TEXTO] " + message);
    }
}
