package com.sportinggoods.repository;

import com.sportinggoods.model.Item;
import java.util.List;
import java.util.Optional;

public class ItemRepository {
    private List<Item> items;

    public ItemRepository(List<Item> items) {
        this.items = items;
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

    public void save(Item item) {
        // Code to save or update the item in the database or data store
    }
}