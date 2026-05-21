package org.example.service;

import org.example.model.User;

// Servicio de autenticacion. Se apoya en UserService para buscar
// al usuario y delega la verificacion de credenciales al propio User.
public class AuthService {
    private final UserService userService;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    // Intenta loguear con email + password. Devuelve el User si
    // las credenciales son validas, o null si fallan.
    public User login(String email, String password) {
        User user = userService.findByEmail(email);
        if (user != null && user.authenticate(email, password)) {
            return user;
        }
        return null;
    }
}
