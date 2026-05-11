package org.example.patterns;

import org.example.catalog.Priority;
import org.example.model.Item;
import org.example.model.Task;
import org.example.model.User;

public class TaskFactory extends ItemFactory {
    @Override
    public Item createItem(int id, String title, String description, Priority priority, User owner) {
        return new Task(id, title, description, priority, owner);
    }
}
