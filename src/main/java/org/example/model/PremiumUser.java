package org.example.model;

/**
 * Usuario Premium: sin limite de items ni de colaboradores.
 *
 * <p>Hereda todo el comportamiento por defecto de {@link User}, que ya
 * permite crear y compartir sin restricciones.</p>
 */
public class PremiumUser extends User {

    /**
     * Crea un usuario Premium.
     *
     * @param id       identificador unico
     * @param name     nombre visible
     * @param email    email
     * @param password password
     */
    public PremiumUser(int id, String name, String email, String password) {
        super(id, name, email, password);
    }

    /**
     * Marca explicitamente que este usuario tiene acceso completo.
     *
     * @return siempre {@code true}
     */
    public boolean fullAccess() {
        return true;
    }
}
