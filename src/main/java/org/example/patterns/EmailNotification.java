package org.example.patterns;

// Estrategia concreta del Strategy: entrega el mensaje por correo
// (lo simula imprimiendo con el prefijo [EMAIL] en consola).
public class EmailNotification implements NotificationStrategy {
    @Override
    public void notify(String message) {
        System.out.println("[EMAIL] " + message);
    }
}
