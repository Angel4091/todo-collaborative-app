package org.example.dao;

import org.example.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

// Capa de persistencia en memoria para usuarios.
// Usa ConcurrentHashMap para que sea thread-safe (varios hilos
// pueden leer/escribir al mismo tiempo sin trabarse).
// La clave es el email.
public class UserDAO {
    private static final ConcurrentHashMap<String, User> usersByEmail = new ConcurrentHashMap<>();

    // Guarda o reemplaza un usuario (la clave es el email).
    public void save(User user) {
        usersByEmail.put(user.getEmail(), user);
    }

    // Devuelve el usuario con ese email, o null si no existe.
    public User findByEmail(String email) {
        return usersByEmail.get(email);
    }

    // Devuelve todos los usuarios como lista.
    public List<User> findAll() {
        return new ArrayList<>(usersByEmail.values());
    }

    // Borra el usuario con ese email.
    public void delete(String email) {
        usersByEmail.remove(email);
    }
}
