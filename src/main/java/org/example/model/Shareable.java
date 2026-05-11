package org.example.model;

public interface Shareable {
    void addCollaborator(User user);
    void showCollaborators();
    boolean isShared();
}
