package org.example.patterns;

// Estrategia concreta del Strategy: entrega el mensaje por mensaje de
// texto (lo simula imprimiendo con el prefijo [MENSAJE DE TEXTO]).
public class MensajeTextoNotification implements NotificationStrategy {
    @Override
    public void notify(String message) {
        System.out.println("[MENSAJE DE TEXTO] " + message);
    }
}
