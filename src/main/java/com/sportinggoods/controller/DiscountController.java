package com.sportinggoods.controller;

import com.sportinggoods.model.Discount;
import com.sportinggoods.model.Inventory;
import com.sportinggoods.model.Item;
import com.sportinggoods.repository.DiscountRepository;
import java.util.List;

public class DiscountController {
    private DiscountRepository discountRepository;
    private Inventory inventory;

    public DiscountController(DiscountRepository discountRepository, Inventory inventory) {
        this.discountRepository = discountRepository;
        this.inventory = inventory;
        loadOriginalPrices(); // Load original prices at initialization
    }

    public String addDiscountToItem(String itemName, double value, String type) {
        Item item = inventory.getItem(itemName);
        if (item == null) {
            return "Item not found.";
        }

        // Save original price before applying discount
        discountRepository.addDiscount(new Discount(itemName, value, type), item.getPrice());

        double discountedPrice = calculateDiscountedPrice(item.getPrice(), value, type);
        item.setPrice(discountedPrice);

        inventory.saveItemsToFile(); // Save updated inventory
        return "Discount applied to item: " + itemName;
    }

    public String addDiscountToDepartment(String departmentName, double value, String type) {
        List<Item> departmentItems = inventory.getItemsByDepartment(departmentName);
        if (departmentItems.isEmpty()) return "No items found in department.";

        for (Item item : departmentItems) {
            discountRepository.addDiscount(new Discount(departmentName, value, type), item.getPrice());

            double discountedPrice = calculateDiscountedPrice(item.getPrice(), value, type);
            item.setPrice(discountedPrice);
        }

        inventory.saveItemsToFile(); // Save updated inventory
        return "Discount applied to department: " + departmentName;
    }

    public String addDiscountStoreWide(double value, String type) {
        List<Item> allItems = inventory.getAllItems();
        for (Item item : allItems) {
            discountRepository.addDiscount(new Discount("Store-Wide", value, type), item.getPrice());

            double discountedPrice = calculateDiscountedPrice(item.getPrice(), value, type);
            item.setPrice(discountedPrice);
        }

        inventory.saveItemsToFile(); // Save updated inventory
        return "Store-wide discount applied.";
    }

    public String removeDiscount(String target) {
        List<Item> itemsToUpdate;

        if (target.equalsIgnoreCase("store-wide")) {
            itemsToUpdate = inventory.getAllItems();
        } else if (!inventory.getItemsByDepartment(target).isEmpty()) {
            itemsToUpdate = inventory.getItemsByDepartment(target);
        } else {
            Item item = inventory.getItem(target);
            if (item == null) {
                return "Target not found in inventory.";
            }
            itemsToUpdate = List.of(item);
        }

        for (Item item : itemsToUpdate) {
            try {
                double originalPrice = discountRepository.restoreOriginalPrice(item.getName());
                item.setPrice(originalPrice); // Restore the original price
            } catch (IllegalArgumentException e) {
                return e.getMessage(); // Handle cases where original price is missing
            }
        }

        discountRepository.removeDiscount(target);
        inventory.saveItemsToFile(); // Save updated inventory
        return "Discount removed for target: " + target;
    }

    public List<Discount> listDiscounts() {
        return discountRepository.getDiscounts();
    }

    private double calculateDiscountedPrice(double price, double value, String type) {
        return type.equalsIgnoreCase("PERCENTAGE") ? price * (1 - value / 100) : price - value;
    }

    private void loadOriginalPrices() {
        discountRepository.loadOriginalPricesFromFile();
    }
}