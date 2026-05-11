package org.example.model;

public class PremiumUser extends User {

    public PremiumUser(int id, String name, String email, String password) {
        super(id, name, email, password);
    }

    public boolean fullAccess() {
        return true;
    }
}
