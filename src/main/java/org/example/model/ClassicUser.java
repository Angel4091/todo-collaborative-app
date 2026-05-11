package org.example.model;

public class ClassicUser extends User {
    private int limitTask;
    private int maxCollaborators;

    public ClassicUser(int id, String name, String email, String password, int limitTask, int maxCollaborators) {
        super(id, name, email, password);
        this.limitTask = limitTask;
        this.maxCollaborators = maxCollaborators;
    }

    @Override
    public boolean addItem(Item item) {
        if (createdItems.size() >= limitTask) {
            System.out.println("[ClassicUser " + name + "] Limite de items alcanzado (" + limitTask + ").");
            return false;
        }
        createdItems.add(item);
        return true;
    }

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
