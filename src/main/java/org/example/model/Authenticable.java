package org.example.model;

// Contrato para clases que se pueden autenticar con email + password.
// Lo implementa la clase User.
public interface Authenticable {
    boolean authenticate(String email, String password);
}
