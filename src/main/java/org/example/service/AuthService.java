package org.example.service;

import org.example.model.User;

public class AuthService {
    private final UserService userService;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public User login(String email, String password) {
        User user = userService.findByEmail(email);
        if (user != null && user.authenticate(email, password)) {
            return user;
        }
        return null;
    }
}
