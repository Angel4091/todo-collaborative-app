package org.example.patterns;

public class EmailNotification implements NotificationStrategy {
    @Override
    public void notify(String message) {
        System.out.println("[EMAIL] " + message);
    }
}
