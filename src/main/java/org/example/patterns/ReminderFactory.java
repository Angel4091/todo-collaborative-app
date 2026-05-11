package org.example.patterns;

import org.example.catalog.Priority;
import org.example.model.Item;
import org.example.model.Reminder;
import org.example.model.User;

public class ReminderFactory extends ItemFactory {
    private final String dateTime;

    public ReminderFactory(String dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public Item createItem(int id, String title, String description, Priority priority, User owner) {
        return new Reminder(id, title, description, priority, owner, dateTime);
    }
}
