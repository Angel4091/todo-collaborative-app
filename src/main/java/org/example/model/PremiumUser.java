package org.example.model;

// Usuario Premium: sin limite de items ni de colaboradores.
// Hereda todo el comportamiento por defecto de User.
public class PremiumUser extends User {

    public PremiumUser(int id, String name, String email, String password) {
        super(id, name, email, password);
    }

    // Marca que tiene acceso completo (es un getter explicativo).
    public boolean fullAccess() {
        return true;
    }
}
