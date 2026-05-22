package org.example.model;

import org.example.catalog.Priority;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Clase base abstracta de los items del sistema (Tasks y Reminders).
 * Implementa {@link Shareable}: cualquier item se puede compartir con
 * otros usuarios.
 *
 * <p>La lista de colaboradores y los metodos que la modifican son
 * thread-safe para soportar acceso concurrente desde varios hilos.</p>
 */
public abstract class Item implements Shareable {

    /** Identificador unico del item. */
    protected int id;

    /** Titulo corto del item. */
    protected String title;

    /** Descripcion mas detallada. */
    protected String description;

    /** Nivel de prioridad. */
    protected Priority priority;

    /** Usuario que creo el item. */
    protected User owner;

    /**
     * Lista de colaboradores. Usa {@link CopyOnWriteArrayList} para
     * permitir iterar sin bloquear escrituras.
     */
    protected List<User> collaborators;

    /**
     * Construye un item nuevo con la lista de colaboradores vacia.
     *
     * @param id          identificador unico
     * @param title       titulo
     * @param description descripcion
     * @param priority    prioridad
     * @param owner       usuario propietario
     */
    public Item(int id, String title, String description, Priority priority, User owner) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.owner = owner;
        this.collaborators = new CopyOnWriteArrayList<>();
    }

    /**
     * Agrega un colaborador al item, evitando duplicados. Es
     * {@code synchronized} para que dos hilos no metan el mismo
     * usuario al mismo tiempo (race condition).
     *
     * @param user colaborador a agregar
     */
    @Override
    public synchronized void addCollaborator(User user) {
        if (!collaborators.contains(user)) {
            collaborators.add(user);
            user.getSharedItems().add(this);
            System.out.println("[Item " + title + "] " + user.getName() + " agregado como colaborador.");
        }
    }

    /**
     * Saca a un colaborador del item. Tambien {@code synchronized}
     * por el mismo motivo de race conditions.
     *
     * @param user colaborador a remover
     */
    public synchronized void removeCollaborator(User user) {
        collaborators.remove(user);
        user.getSharedItems().remove(this);
    }

    /** Imprime por consola la lista de colaboradores del item. */
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

    /**
     * Indica si el item esta compartido (tiene al menos un colaborador).
     *
     * @return {@code true} si tiene uno o mas colaboradores
     */
    @Override
    public boolean isShared() {
        return !collaborators.isEmpty();
    }

    /**
     * Imprime los detalles especificos del item. Cada subclase
     * (Task / Reminder) lo implementa a su manera.
     */
    public abstract void showDetails();

    /** @return id del item */
    public int getId() { return id; }

    /** @return titulo del item */
    public String getTitle() { return title; }

    /** @return descripcion del item */
    public String getDescription() { return description; }

    /** @return prioridad del item */
    public Priority getPriority() { return priority; }

    /** @return usuario propietario del item */
    public User getOwner() { return owner; }

    /** @return lista de colaboradores (referencia mutable thread-safe) */
    public List<User> getCollaborators() { return collaborators; }
}
