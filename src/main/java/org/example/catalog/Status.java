package org.example.catalog;

// Estados posibles de una Task durante su ciclo de vida.
public enum Status {
    PENDING,        // recien creada
    IN_PROGRESS,    // alguien la esta trabajando
    COMPLETED,      // terminada
    CANCELED        // descartada
}
