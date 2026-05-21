package org.example.model;

import org.example.catalog.Priority;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Clase abstracta base de los items del sistema (Tasks y Reminders).
 *
 * <p>Implementa {@link Shareable} para que un item pueda compartirse con
 * otros usuarios. La lista de colaboradores usa
 * {@link CopyOnWriteArrayList} para permitir iteracion sin bloquear
 * escrituras, y los metodos {@link #addCollaborator(User)} y
 * {@link #removeCollaborator(User)} estan marcados como
 * {@code synchronized} para evitar race conditions cuando dos hilos
 * modifican la lista en paralelo.</p>
 */
public abstract class Item implements Shareable {
    protected int id;
    protected String title;
    protected String description;
    protected Priority priority;
    protected User owner;
    protected List<User> collaborators;

    public Item(int id, String title, String description, Priority priority, User owner) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.owner = owner;
        this.collaborators = new CopyOnWriteArrayList<>();
    }

    @Override
    public synchronized void addCollaborator(User user) {
        if (!collaborators.contains(user)) {
            collaborators.add(user);
            user.getSharedItems().add(this);
            System.out.println("[Item " + title + "] " + user.getName() + " agregado como colaborador.");
        }
    }

    public synchronized void removeCollaborator(User user) {
        collaborators.remove(user);
        user.getSharedItems().remove(this);
    }

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

    @Override
    public boolean isShared() {
        return !collaborators.isEmpty();
    }

    public abstract void showDetails();

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Priority getPriority() { return priority; }
    public User getOwner() { return owner; }
    public List<User> getCollaborators() { return collaborators; }
}
