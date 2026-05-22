package org.example.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Clase base que representa a un usuario del sistema.
// Implementa Authenticable para que se pueda loguear.
// De aca extienden PremiumUser (sin limites) y ClassicUser (con limites).
public class User implements Authenticable {
    protected int id;
    protected String name;
    protected String email;
    protected String password;
    // Listas thread-safe: pueden ser leidas/escritas desde varios hilos.
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

    // Compara email y password contra los del usuario.
    @Override
    public boolean authenticate(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    // Agrega un item a la lista de creados. PremiumUser no tiene tope;
    // ClassicUser sobreescribe este metodo para chequear el limite.
    public boolean addItem(Item item) {
        createdItems.add(item);
        return true;
    }

    // Devuelve true si el usuario PUEDE crear otro item.
    // Por defecto si (PremiumUser). ClassicUser lo sobreescribe para
    // chequear contra su limite. Lo usa la UI para avisar al usuario
    // ANTES de pedirle todos los datos.
    public boolean canAddItem() {
        return true;
    }

    // Comparte un item con otro usuario.
    public boolean shareItem(Item item, User user) {
        item.addCollaborator(user);
        return true;
    }

    // Imprime por consola los items que este usuario creo.
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

    // Imprime por consola los items que otros usuarios compartieron con este.
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

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public List<Item> getCreatedItems() { return createdItems; }
    public List<Item> getSharedItems() { return sharedItems; }

    // Dos usuarios son iguales si tienen el mismo id.
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
