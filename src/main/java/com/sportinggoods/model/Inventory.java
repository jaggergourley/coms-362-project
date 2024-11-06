package com.sportinggoods.model;

import java.util.List;

// Manages inventory
public class Inventory {
    private List<Item> items;

    public Inventory() {
        items = loadItemsFromFile(); // Load from file on initialization
    }

    public boolean checkAvailability(String itemName, int quantity) {
        // Logic to check if an item is available
        return false;
    }

    public void updateQuantity(String itemName, int quantity) {
        // Logic to update item quantity
    }

    private List<Item> loadItemsFromFile() {
        // Code to load items from a file
        return null;
    }

    public void saveItemsToFile() {
        // Code to save items to a file
    }
}
