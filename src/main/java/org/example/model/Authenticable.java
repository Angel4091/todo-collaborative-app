package org.example.model;

public interface Authenticable {
    boolean authenticate(String email, String password);
}
