package org.example.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User implements Authenticable, TaskObserver {
    protected int id;
    protected String name;
    protected String email;
    protected String password;
    protected List<Item> createdItems;
    protected List<Item> sharedItems;

    public User(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdItems = Collections.synchronizedList(new ArrayList<>());
        this.sharedItems = Collections.synchronizedList(new ArrayList<>());
    }

    @Override
    public boolean authenticate(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    public boolean addItem(Item item) {
        createdItems.add(item);
        return true;
    }

    public boolean shareItem(Item item, User user) {
        item.addCollaborator(user);
        return true;
    }

    public void printCreatedItems() {
        System.out.println("--- Items creados por " + name + " ---");
        synchronized (createdItems) {
            if (createdItems.isEmpty()) {
                System.out.println(" (sin items)");
            } else {
                for (Item i : createdItems) {
                    System.out.println(" #" + i.getId() + " " + i.getClass().getSimpleName() + ": " + i.getTitle());
                }
            }
        }
    }

    public void printSharedItems() {
        System.out.println("--- Items compartidos con " + name + " ---");
        synchronized (sharedItems) {
            if (sharedItems.isEmpty()) {
                System.out.println(" (sin items)");
            } else {
                for (Item i : sharedItems) {
                    System.out.println(" #" + i.getId() + " " + i.getClass().getSimpleName() + ": " + i.getTitle() + " (owner: " + i.getOwner().getName() + ")");
                }
            }
        }
    }

    @Override
    public void update(Item item) {
        System.out.println("  >> [" + name + "] notificado: '" + item.getTitle() + "' actualizado.");
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public List<Item> getCreatedItems() { return createdItems; }
    public List<Item> getSharedItems() { return sharedItems; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User other)) return false;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
