package org.example.service;

import org.example.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class TaskManager {
    private static volatile TaskManager instance;
    private static final Object MUTEX = new Object();

    private final ConcurrentHashMap<Integer, Item> tasks;
    private final ReentrantLock lock;

    private TaskManager() {
        this.tasks = new ConcurrentHashMap<>();
        this.lock = new ReentrantLock();
    }

    public static TaskManager getInstance() {
        if (instance == null) {
            synchronized (MUTEX) {
                if (instance == null) {
                    instance = new TaskManager();
                }
            }
        }
        return instance;
    }

    public void addTask(Item item) {
        lock.lock();
        try {
            tasks.put(item.getId(), item);
            System.out.println("[TaskManager] Item #" + item.getId() + " agregado: " + item.getTitle());
        } finally {
            lock.unlock();
        }
    }

    public void removeTask(int id) {
        lock.lock();
        try {
            tasks.remove(id);
        } finally {
            lock.unlock();
        }
    }

    public void updateTask(Item item) {
        lock.lock();
        try {
            tasks.put(item.getId(), item);
        } finally {
            lock.unlock();
        }
    }

    public Item getTask(int id) {
        return tasks.get(id);
    }

    public List<Item> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public int size() {
        return tasks.size();
    }
}
