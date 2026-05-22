package org.example.model;

/**
 * Usuario Classic: tiene topes en cuantos items puede crear y cuantos
 * colaboradores puede agregar a un item.
 *
 * <p>Sobreescribe {@link #addItem(Item)} y {@link #shareItem(Item, User)}
 * para aplicar los limites. Esto es polimorfismo: el mismo metodo se
 * comporta distinto segun el tipo concreto.</p>
 */
public class ClassicUser extends User {

    /** Tope de items que puede crear este usuario. */
    private int limitTask;

    /** Tope de colaboradores que puede agregar por item. */
    private int maxCollaborators;

    /**
     * Crea un usuario Classic con sus limites.
     *
     * @param id               identificador unico
     * @param name             nombre visible
     * @param email            email
     * @param password         password
     * @param limitTask        tope de items que puede crear
     * @param maxCollaborators tope de colaboradores por item
     */
    public ClassicUser(int id, String name, String email, String password, int limitTask, int maxCollaborators) {
        super(id, name, email, password);
        this.limitTask = limitTask;
        this.maxCollaborators = maxCollaborators;
    }

    /**
     * Agrega un item solo si no se supero el {@link #limitTask}.
     * Si ya esta en el tope, rechaza y devuelve false.
     *
     * @param item item a agregar
     * @return {@code true} si se agrego, {@code false} si se alcanzo el limite
     */
    @Override
    public boolean addItem(Item item) {
        if (createdItems.size() >= limitTask) {
            System.out.println("[ClassicUser " + name + "] Limite de items alcanzado (" + limitTask + ").");
            return false;
        }
        createdItems.add(item);
        return true;
    }

    /**
     * Comparte el item solo si no se supero el {@link #maxCollaborators}.
     *
     * @param item item a compartir
     * @param user usuario con quien compartir
     * @return {@code true} si se compartio, {@code false} si se alcanzo el limite
     */
    @Override
    public boolean shareItem(Item item, User user) {
        if (item.getCollaborators().size() >= maxCollaborators) {
            System.out.println("[ClassicUser " + name + "] Limite de colaboradores alcanzado (" + maxCollaborators + ").");
            return false;
        }
        item.addCollaborator(user);
        return true;
    }

    /**
     * Chequea el limite SIN agregar el item. Lo usa la UI para mostrar
     * el error antes de pedir titulo / descripcion / prioridad.
     *
     * @return {@code true} si todavia hay cupo
     */
    @Override
    public boolean canAddItem() {
        return createdItems.size() < limitTask;
    }

    /** @return tope de items configurado */
    public int getLimitTask() { return limitTask; }

    /** @return tope de colaboradores configurado */
    public int getMaxCollaborators() { return maxCollaborators; }
}
