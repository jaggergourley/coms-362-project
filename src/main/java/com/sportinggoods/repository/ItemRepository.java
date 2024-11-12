package com.sportinggoods.repository;

import com.sportinggoods.model.Item;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemRepository {
    private List<Item> items;

    // Constructor initializing with sample data for testing
    public ItemRepository() {
        this.items = new ArrayList<>();
        initializeSampleData();
    }

    public ItemRepository(List<Item> items) {
        this.items = items != null ? items : new ArrayList<>();
    }

    // Search item by name
    public Optional<Item> findByName(String name) {
        return items.stream()
                .filter(item -> item.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    // Search items by department
    public List<Item> findByDepartment(String department) {
        return items.stream()
                .filter(item -> item.getDepartment().equalsIgnoreCase(department))
                .toList();
    }

    // Save or update an item
    public void save(Item item) {
        Optional<Item> existingItemOpt = findByName(item.getName());
        if (existingItemOpt.isPresent()) {
            Item existingItem = existingItemOpt.get();
            existingItem.setPrice(item.getPrice());
            existingItem.setQuantity(item.getQuantity());
            existingItem.setDepartment(item.getDepartment());
        } else {
            items.add(item); // Add new item if not present
        }
    }

    // Method to initialize sample data for testing
    private void initializeSampleData() {
        items.add(new Item("Basketball", 25.00, "Sports", 10, 1));
        items.add(new Item("Soccer Ball", 20.00, "Sports", 15, 1));
        items.add(new Item("Tennis Racket", 75.00, "Sports", 5, 1));
    }

    // Get all items
    public List<Item> getAllItems() {
        return new ArrayList<>(items); // Return a copy to prevent modification
    }
}