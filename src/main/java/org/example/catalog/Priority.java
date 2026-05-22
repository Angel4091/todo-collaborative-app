package org.example.catalog;

/**
 * Niveles de prioridad que puede tener un {@link org.example.model.Item}
 * (sea {@link org.example.model.Task} o {@link org.example.model.Reminder}).
 */
public enum Priority {
    /** Prioridad alta: lo mas urgente. */
    HIGH,
    /** Prioridad media: lo normal. */
    MEDIUM,
    /** Prioridad baja: lo menos urgente. */
    LOW
}
