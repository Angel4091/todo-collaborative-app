package org.example.service;

import org.example.dao.UserDAO;
import org.example.model.User;

import java.util.List;

// Capa de servicio para usuarios. Es un wrapper sobre UserDAO
// que el resto del codigo usa para buscar y registrar usuarios.
public class UserService {
    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    // Busca un usuario por su email. Devuelve null si no existe.
    public User findByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    // Devuelve todos los usuarios registrados.
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    // Registra (o sobreescribe) un usuario en el sistema.
    public void register(User user) {
        userDAO.save(user);
    }
}
