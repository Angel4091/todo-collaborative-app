package org.example.service;

import org.example.model.User;

/**
 * Servicio de autenticacion. Se apoya en {@link UserService} para
 * buscar al usuario por email y delega la verificacion de credenciales
 * al propio {@link User} (que implementa {@code Authenticable}).
 */
public class AuthService {

    /** Servicio de usuarios usado para buscar por email. */
    private final UserService userService;

    /**
     * Crea el AuthService recibiendo el UserService que ya tiene los
     * usuarios registrados (inyeccion de dependencias).
     *
     * @param userService servicio de usuarios
     */
    public AuthService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Intenta loguear al usuario con las credenciales dadas.
     *
     * @param email    email del usuario
     * @param password password en texto plano
     * @return el {@link User} si las credenciales son validas, o
     *         {@code null} si no existe o no coinciden
     */
    public User login(String email, String password) {
        User user = userService.findByEmail(email);
        if (user != null && user.authenticate(email, password)) {
            return user;
        }
        return null;
    }
}
