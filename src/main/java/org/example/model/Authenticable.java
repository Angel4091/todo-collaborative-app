package org.example.model;

/**
 * Contrato para clases que se pueden autenticar con email + password.
 * Lo implementa la clase {@link User}.
 */
public interface Authenticable {

    /**
     * Verifica si las credenciales coinciden con las del usuario.
     *
     * @param email    email a verificar
     * @param password password a verificar
     * @return {@code true} si las credenciales son validas
     */
    boolean authenticate(String email, String password);
}
