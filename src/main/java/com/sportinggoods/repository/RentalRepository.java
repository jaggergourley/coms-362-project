package com.sportinggoods.repository;

import java.util.*;

public class RentalRepository {
    private Map<String, String> rentalItems; // Key: Item ID, Value: Status (e.g., "available", "rented")
    private List<String> rentalLogs;

    public RentalRepository() {
        this.rentalItems = new HashMap<>();
        this.rentalLogs = new ArrayList<>();
    }

    public boolean isItemAvailable(String itemId) {
        return rentalItems.getOrDefault(itemId, "not found").equals("available");
    }

    public void rentItem(String itemId) {
        rentalItems.put(itemId, "rented");
        rentalLogs.add("Item ID: " + itemId + " rented on " + new Date());
    }

    public void returnItem(String itemId, boolean isDamaged) {
        rentalItems.put(itemId, "available");
        rentalLogs.add("Item ID: " + itemId + " returned on " + new Date() + (isDamaged ? " with damage" : ""));
    }

    public List<String> getRentalLogs() {
        return new ArrayList<>(rentalLogs);
    }
}

