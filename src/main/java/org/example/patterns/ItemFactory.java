package org.example.patterns;

import org.example.catalog.Priority;
import org.example.model.Item;
import org.example.model.User;

public abstract class ItemFactory {
    public abstract Item createItem(int id, String title, String description, Priority priority, User owner);
}
