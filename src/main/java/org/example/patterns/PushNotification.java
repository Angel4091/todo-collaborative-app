package org.example.patterns;

public class PushNotification implements NotificationStrategy {
    @Override
    public void notify(String message) {
        System.out.println("[PUSH] " + message);
    }
}
