package org.example.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clase base que representa a un usuario del sistema. Implementa
 * {@link Authenticable} para que pueda loguearse.
 *
 * <p>De aca extienden {@link PremiumUser} (sin limites) y
 * {@link ClassicUser} (con limites de items y colaboradores).</p>
 *
 * <p>Las listas {@code createdItems} y {@code sharedItems} son
 * thread-safe (envueltas en {@code Collections.synchronizedList})
 * porque pueden ser modificadas desde varios hilos al mismo tiempo
 * cuando se cambia el estado de una task compartida.</p>
 */
public class User implements Authenticable {

    /** Identificador unico del usuario. */
    protected int id;

    /** Nombre visible del usuario. */
    protected String name;

    /** Email (sirve como clave logica de busqueda). */
    protected String email;

    /** Password en texto plano (para el entregable 3 deberia hashearse). */
    protected String password;

    /** Lista thread-safe de items creados por este usuario. */
    protected List<Item> createdItems;

    /** Lista thread-safe de items que otros usuarios compartieron con este. */
    protected List<Item> sharedItems;

    /**
     * Crea un usuario nuevo con listas vacias.
     *
     * @param id       identificador unico
     * @param name     nombre visible
     * @param email    email
     * @param password password en texto plano
     */
    public User(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdItems = Collections.synchronizedList(new ArrayList<>());
        this.sharedItems = Collections.synchronizedList(new ArrayList<>());
    }

    /**
     * Compara las credenciales recibidas con las del usuario.
     *
     * @param email    email a verificar
     * @param password password a verificar
     * @return {@code true} si ambas coinciden
     */
    @Override
    public boolean authenticate(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    /**
     * Agrega un item a la lista de creados. {@link PremiumUser} no tiene
     * tope; {@link ClassicUser} sobreescribe este metodo para chequear
     * contra su limite.
     *
     * @param item item a agregar
     * @return {@code true} si se agrego con exito
     */
    public boolean addItem(Item item) {
        createdItems.add(item);
        return true;
    }

    /**
     * Comparte un item con otro usuario. Internamente llama a
     * {@link Item#addCollaborator(User)}.
     *
     * @param item item a compartir
     * @param user usuario con quien compartir
     * @return {@code true} si se compartio
     */
    public boolean shareItem(Item item, User user) {
        item.addCollaborator(user);
        return true;
    }

    /**
     * Devuelve {@code true} si el usuario PUEDE crear otro item.
     * Por defecto siempre puede. {@link ClassicUser} lo sobreescribe
     * para chequear contra su limite. Lo usa la UI para avisar antes
     * de pedirle todos los datos al usuario.
     *
     * @return {@code true} si esta dentro del cupo
     */
    public boolean canAddItem() {
        return true;
    }

    /** Imprime por consola los items que este usuario creo. */
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

    /** Imprime por consola los items que otros usuarios compartieron con este. */
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

    /** @return id del usuario */
    public int getId() { return id; }

    /** @return nombre del usuario */
    public String getName() { return name; }

    /** @return email del usuario */
    public String getEmail() { return email; }

    /** @return lista de items creados (referencia mutable thread-safe) */
    public List<Item> getCreatedItems() { return createdItems; }

    /** @return lista de items compartidos con este usuario */
    public List<Item> getSharedItems() { return sharedItems; }

    /**
     * Dos usuarios son iguales si tienen el mismo id.
     *
     * @param o objeto a comparar
     * @return {@code true} si el otro es User con mismo id
     */
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
