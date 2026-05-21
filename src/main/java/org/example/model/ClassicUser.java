package org.example.model;

// Usuario Classic: tiene topes en cuanto items puede crear y
// cuantos colaboradores puede agregar a un item.
// Sobreescribe addItem y shareItem para aplicar los limites
// (es polimorfismo: el mismo metodo se comporta distinto segun el tipo).
public class ClassicUser extends User {
    private int limitTask;          // tope de items que puede crear
    private int maxCollaborators;   // tope de colaboradores por item

    public ClassicUser(int id, String name, String email, String password, int limitTask, int maxCollaborators) {
        super(id, name, email, password);
        this.limitTask = limitTask;
        this.maxCollaborators = maxCollaborators;
    }

    // Si ya llego al tope, rechaza la creacion y devuelve false.
    @Override
    public boolean addItem(Item item) {
        if (createdItems.size() >= limitTask) {
            System.out.println("[ClassicUser " + name + "] Limite de items alcanzado (" + limitTask + ").");
            return false;
        }
        createdItems.add(item);
        return true;
    }

    // Si el item ya tiene el maximo de colaboradores, no deja agregar mas.
    @Override
    public boolean shareItem(Item item, User user) {
        if (item.getCollaborators().size() >= maxCollaborators) {
            System.out.println("[ClassicUser " + name + "] Limite de colaboradores alcanzado (" + maxCollaborators + ").");
            return false;
        }
        item.addCollaborator(user);
        return true;
    }

    public int getLimitTask() { return limitTask; }
    public int getMaxCollaborators() { return maxCollaborators; }
}
