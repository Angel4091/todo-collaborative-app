package org.example.dao;

import org.example.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class UserDAO {
    private static final ConcurrentHashMap<String, User> usersByEmail = new ConcurrentHashMap<>();

    public void save(User user) {
        usersByEmail.put(user.getEmail(), user);
    }

    public User findByEmail(String email) {
        return usersByEmail.get(email);
    }

    public List<User> findAll() {
        return new ArrayList<>(usersByEmail.values());
    }

    public void delete(String email) {
        usersByEmail.remove(email);
    }
}
