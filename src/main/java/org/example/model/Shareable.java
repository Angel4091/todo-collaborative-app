package org.example.model;

/**
 * Contrato para items que se pueden compartir con otros usuarios.
 * Lo implementa la clase {@link Item}.
 */
public interface Shareable {

    /**
     * Agrega un usuario como colaborador del item.
     *
     * @param user colaborador a agregar
     */
    void addCollaborator(User user);

    /**
     * Imprime por consola la lista de colaboradores del item.
     */
    void showCollaborators();

    /**
     * Indica si el item esta compartido (tiene al menos un colaborador).
     *
     * @return {@code true} si tiene uno o mas colaboradores
     */
    boolean isShared();
}
