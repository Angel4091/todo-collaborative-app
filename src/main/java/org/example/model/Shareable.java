package org.example.model;

// Contrato para items que se pueden compartir con otros usuarios.
// Lo implementa la clase Item.
public interface Shareable {
    void addCollaborator(User user);
    void showCollaborators();
    boolean isShared();
}
