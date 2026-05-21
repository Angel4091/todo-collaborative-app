package org.example.patterns;

// Interface del PATRON STRATEGY de notificaciones.
// Cada implementacion concreta (Email, Mensaje de Texto) define COMO
// se entrega el mensaje. Un Reminder usa una de estas estrategias
// sin saber cual es. Es el UNICO patron de diseno del proyecto.
public interface NotificationStrategy {
    void notify(String message);
}
