package org.example.service;

import org.example.dao.ItemDAO;
import org.example.model.Item;

import java.util.List;

// Capa de servicio para items (Tasks y Reminders). Es un wrapper
// sobre ItemDAO que el resto del codigo usa para guardar, buscar y
// listar items. Asi MainMenu no toca el storage directamente.
public class ItemService {
    private final ItemDAO itemDAO;

    public ItemService() {
        this.itemDAO = new ItemDAO();
    }

    // Guarda (o reemplaza) un item.
    public void save(Item item) {
        itemDAO.save(item);
    }

    // Busca un item por id. Devuelve null si no existe.
    public Item findById(int id) {
        return itemDAO.findById(id);
    }

    // Devuelve todos los items del sistema.
    public List<Item> findAll() {
        return itemDAO.findAll();
    }
}
