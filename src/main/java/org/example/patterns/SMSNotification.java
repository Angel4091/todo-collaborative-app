package org.example.patterns;

public class SMSNotification implements NotificationStrategy {
    @Override
    public void notify(String message) {
        System.out.println("[SMS] " + message);
    }
}
