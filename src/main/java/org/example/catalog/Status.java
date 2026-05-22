package org.example.catalog;

/**
 * Estados posibles de una {@link org.example.model.Task} durante su ciclo
 * de vida.
 *
 * <p>El flujo tipico es:
 * PENDING &rarr; IN_PROGRESS &rarr; COMPLETED. CANCELED puede aparecer
 * desde cualquier estado si la task se descarta.</p>
 */
public enum Status {
    /** Recien creada, sin empezar. */
    PENDING,
    /** Alguien la esta trabajando. */
    IN_PROGRESS,
    /** Ya se termino. */
    COMPLETED,
    /** Se descarto sin terminar. */
    CANCELED
}
