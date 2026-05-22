package org.example.service;

import org.example.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Capa de servicio para items (Tasks y Reminders). Mantiene un mapa
 * thread-safe en memoria con todos los items del sistema, indexados
 * por su id.
 *
 * <p>La UI (MainMenu) usa esta clase para guardar, buscar y listar
 * items sin acceder directamente al storage.</p>
 */
public class ItemService {

    /**
     * Mapa thread-safe que contiene los items del sistema.
     * Clave: id del item. Valor: instancia de Item (Task o Reminder).
     */
    private final ConcurrentHashMap<Integer, Item> items = new ConcurrentHashMap<>();

    /**
     * Guarda un item nuevo o reemplaza uno existente con el mismo id.
     *
     * @param item item a guardar
     */
    public void save(Item item) {
        items.put(item.getId(), item);
    }

    /**
     * Busca un item por su id.
     *
     * @param id id del item a buscar
     * @return el {@link Item} encontrado, o {@code null} si no existe
     */
    public Item findById(int id) {
        return items.get(id);
    }

    /**
     * Devuelve una copia con todos los items del sistema.
     *
     * @return lista nueva con todos los items (snapshot)
     */
    public List<Item> findAll() {
        return new ArrayList<>(items.values());
    }
}
