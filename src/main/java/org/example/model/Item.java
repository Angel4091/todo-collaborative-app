package org.example.model;

import org.example.catalog.Priority;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

// Clase base abstracta de los items del sistema (Tasks y Reminders).
// Implementa Shareable: se puede compartir con otros usuarios.
// La lista de colaboradores y los metodos que la tocan son thread-safe
// para soportar acceso concurrente desde varios hilos.
public abstract class Item implements Shareable {
    protected int id;
    protected String title;
    protected String description;
    protected Priority priority;
    protected User owner;
    // CopyOnWriteArrayList: permite iterar sin bloquear escrituras.
    protected List<User> collaborators;

    public Item(int id, String title, String description, Priority priority, User owner) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.owner = owner;
        this.collaborators = new CopyOnWriteArrayList<>();
    }

    // synchronized: si dos hilos llaman a la vez, solo uno entra.
    // Asi no hay race condition al agregar colaboradores.
    @Override
    public synchronized void addCollaborator(User user) {
        if (!collaborators.contains(user)) {
            collaborators.add(user);
            user.getSharedItems().add(this);
            System.out.println("[Item " + title + "] " + user.getName() + " agregado como colaborador.");
        }
    }

    // Mismo principio que addCollaborator: synchronized para evitar race conditions.
    public synchronized void removeCollaborator(User user) {
        collaborators.remove(user);
        user.getSharedItems().remove(this);
    }

    // Imprime la lista de colaboradores del item.
    @Override
    public void showCollaborators() {
        System.out.println("Colaboradores de '" + title + "':");
        if (collaborators.isEmpty()) {
            System.out.println(" (sin colaboradores)");
        } else {
            for (User u : new ArrayList<>(collaborators)) {
                System.out.println(" - " + u.getName() + " (" + u.getEmail() + ")");
            }
        }
    }

    // El item esta compartido si tiene al menos un colaborador.
    @Override
    public boolean isShared() {
        return !collaborators.isEmpty();
    }

    // Cada subclase (Task / Reminder) define como se muestra.
    public abstract void showDetails();

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Priority getPriority() { return priority; }
    public User getOwner() { return owner; }
    public List<User> getCollaborators() { return collaborators; }
}
