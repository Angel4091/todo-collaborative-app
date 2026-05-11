package org.example.dao;

import org.example.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ItemDAO {
    private static final ConcurrentHashMap<Integer, Item> items = new ConcurrentHashMap<>();

    public void save(Item item) {
        items.put(item.getId(), item);
    }

    public Item findById(int id) {
        return items.get(id);
    }

    public List<Item> findAll() {
        return new ArrayList<>(items.values());
    }

    public void delete(int id) {
        items.remove(id);
    }
}
