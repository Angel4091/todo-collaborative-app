package org.example.service;

import org.example.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Capa de servicio para usuarios. Mantiene un mapa thread-safe en
 * memoria con los usuarios registrados, indexados por su email.
 *
 * <p>El resto del codigo (UI, AuthService) usa esta clase para
 * buscar, registrar y listar usuarios sin acceder directamente al
 * storage. Asi, si en el futuro se reemplaza el mapa en memoria por
 * una base de datos, solo cambia esta clase.</p>
 */
public class UserService {

    /**
     * Mapa thread-safe que contiene los usuarios del sistema.
     * Clave: email del usuario. Valor: instancia de User.
     */
    private final ConcurrentHashMap<String, User> usersByEmail = new ConcurrentHashMap<>();

    /**
     * Busca un usuario por su email.
     *
     * @param email email del usuario a buscar
     * @return el {@link User} encontrado, o {@code null} si no existe
     */
    public User findByEmail(String email) {
        return usersByEmail.get(email);
    }

    /**
     * Devuelve una copia con todos los usuarios registrados.
     *
     * @return lista nueva con todos los usuarios (snapshot)
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(usersByEmail.values());
    }

    /**
     * Registra un usuario nuevo o reemplaza uno existente.
     * Si ya existe un usuario con el mismo email, se sobreescribe.
     *
     * @param user usuario a registrar
     */
    public void register(User user) {
        usersByEmail.put(user.getEmail(), user);
    }
}
