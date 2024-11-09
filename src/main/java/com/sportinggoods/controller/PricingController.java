package com.sportinggoods.controller;

import com.sportinggoods.model.Item;
import com.sportinggoods.repository.ItemRepository;

import java.util.Optional;

public class PricingController {
    private ItemRepository itemRepository;
    private static final double AUTHORIZATION_THRESHOLD = 1000.0; // Example threshold

    public PricingController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public String adjustPrice(String itemName, double newPrice) {
        Optional<Item> itemOpt = itemRepository.findByName(itemName);

        if (itemOpt.isEmpty()) {
            return "Error: Item not found.";
        }

        Item item = itemOpt.get();

        if (newPrice <= 0) {
            return "Error: Invalid price. Price must be greater than 0.";
        }

        if (newPrice > AUTHORIZATION_THRESHOLD) {
            boolean authorized = requestAuthorization();
            if (!authorized) {
                return "Error: Authorization required for high-price adjustments.";
            }
        }

        item.setPrice(newPrice);
        itemRepository.save(item);

        logPriceAdjustment(item);

        return "Price adjusted successfully for " + item.getName() + ". New price: $" + item.getPrice();
    }

    private boolean requestAuthorization() {
        // Authorization logic placeholder
        return true; // Always granted
    }

    private void logPriceAdjustment(Item item) {
        System.out.println("Log: Price of " + item.getName() + " changed to $" + item.getPrice());
    }
}