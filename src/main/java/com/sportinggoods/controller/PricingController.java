package com.sportinggoods.controller;

import com.sportinggoods.model.Inventory;
import com.sportinggoods.model.Item;
import java.util.List;
import java.util.stream.Collectors;

public class PricingController {
    private Inventory inventory;
    private static final double AUTHORIZATION_THRESHOLD = 1000.0;

    public PricingController(Inventory inventory) {
        this.inventory = inventory;
    }

    public List<Item> searchItems(String criteria, String value) {
        return switch (criteria.toLowerCase()) {
            case "name" -> {
                Item itemByName = inventory.getItem(value);
                yield itemByName != null ? List.of(itemByName) : List.of();
            }
            case "department" -> inventory.getItems().values().stream()
                    .filter(item -> item.getDepartment().equalsIgnoreCase(value))
                    .collect(Collectors.toList());
            case "storeid" -> {
                try {
                    int storeID = Integer.parseInt(value);
                    yield inventory.getItems().values().stream()
                            .filter(item -> item.getStoreID() == storeID)
                            .collect(Collectors.toList());
                } catch (NumberFormatException e) {
                    yield List.of();
                }
            }
            default -> List.of();
        };
    }

    public String adjustPrice(Item item, double newPrice) {
        if (newPrice <= 0) {
            return "Error: Price must be greater than 0.";
        }

        if (newPrice > AUTHORIZATION_THRESHOLD && !requestAuthorization()) {
            return "Error: Authorization required for high-price adjustments.";
        }

        item.setPrice(newPrice);
        inventory.saveItemsToFile(); // Save updated inventory to file
        logPriceAdjustment(item);

        return "Price adjusted successfully for " + item.getName() + ". New price: $" + item.getPrice();
    }

    private boolean requestAuthorization() {
        return true;
    }

    private void logPriceAdjustment(Item item) {
        System.out.println("Log: Price of " + item.getName() + " changed to $" + item.getPrice());
    }
}