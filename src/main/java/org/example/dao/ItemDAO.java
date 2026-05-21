package org.example.dao;

import org.example.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

// Capa de persistencia en memoria para items.
// ConcurrentHashMap = thread-safe sin tener que sincronizar a mano.
// La clave es el id del item.
public class ItemDAO {
    private static final ConcurrentHashMap<Integer, Item> items = new ConcurrentHashMap<>();

    // Guarda o reemplaza el item.
    public void save(Item item) {
        items.put(item.getId(), item);
    }

    // Devuelve el item con ese id, o null si no existe.
    public Item findById(int id) {
        return items.get(id);
    }

    // Devuelve todos los items como lista.
    public List<Item> findAll() {
        return new ArrayList<>(items.values());
    }

    // Borra el item con ese id.
    public void delete(int id) {
        items.remove(id);
    }
}
