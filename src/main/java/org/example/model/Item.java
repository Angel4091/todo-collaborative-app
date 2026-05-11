package org.example.model;

import org.example.catalog.Priority;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Item implements Shareable {
    protected int id;
    protected String title;
    protected String description;
    protected Priority priority;
    protected User owner;
    protected List<User> collaborators;
    protected List<TaskObserver> observers;

    public Item(int id, String title, String description, Priority priority, User owner) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.owner = owner;
        this.collaborators = Collections.synchronizedList(new ArrayList<>());
        this.observers = Collections.synchronizedList(new ArrayList<>());
    }

    @Override
    public synchronized void addCollaborator(User user) {
        if (!collaborators.contains(user)) {
            collaborators.add(user);
            addObserver(user);
            user.getSharedItems().add(this);
            System.out.println("[Item " + title + "] " + user.getName() + " agregado como colaborador.");
        }
    }

    public synchronized void removeCollaborator(User user) {
        collaborators.remove(user);
        removeObserver(user);
        user.getSharedItems().remove(this);
    }

    public void addObserver(TaskObserver obs) {
        if (!observers.contains(obs)) {
            observers.add(obs);
        }
    }

    public void removeObserver(TaskObserver obs) {
        observers.remove(obs);
    }

    public void notifyObservers() {
        List<TaskObserver> snapshot;
        synchronized (observers) {
            snapshot = new ArrayList<>(observers);
        }
        for (TaskObserver obs : snapshot) {
            obs.update(this);
        }
    }

    @Override
    public void showCollaborators() {
        System.out.println("Colaboradores de '" + title + "':");
        synchronized (collaborators) {
            if (collaborators.isEmpty()) {
                System.out.println(" (sin colaboradores)");
            } else {
                for (User u : collaborators) {
                    System.out.println(" - " + u.getName() + " (" + u.getEmail() + ")");
                }
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
    public List<TaskObserver> getObservers() { return observers; }
}
