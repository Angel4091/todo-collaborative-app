package org.example.service;

import org.example.dao.UserDAO;
import org.example.model.User;

import java.util.List;

public class UserService {
    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public User findByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    public void register(User user) {
        userDAO.save(user);
    }
}
