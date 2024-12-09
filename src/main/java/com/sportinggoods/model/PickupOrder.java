package com.sportinggoods.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PickupOrder {
    private int orderId;
    private String confirmationDetails; //confirmation number
    private String customerName; // customer name
    private Map<Item, Integer> items; // Items and their quantities
    private String status; // e.g., "Pending", "Completed"
    private LocalDate date;

    public PickupOrder(String customerName, Map<Item, Integer> items, LocalDate date) {
        this.customerName = customerName;
        this.items = items;
        this.status = "Pending";
        this.date = date;
    }

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getConfirmationDetails() {
        return confirmationDetails;
    }

    public void setConfirmationDetails(String confirmationDetails) {
        this.confirmationDetails = confirmationDetails;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Map<Item, Integer> getItems() {
        return items;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Convert to CSV row
    public String toCSV() {
        StringBuilder itemsCSV = new StringBuilder();
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            itemsCSV.append(entry.getKey().getName()).append(":").append(entry.getValue()).append("|");
        }
        if (itemsCSV.length() > 0) itemsCSV.setLength(itemsCSV.length() - 1); // Remove trailing '|'

        return orderId + "," + confirmationDetails + "," + customerName + "," + itemsCSV + "," + date + "," + status;
    }

    public static PickupOrder fromCSV(String line, Inventory inventory) {
        String[] parts = line.split(",");
        if (parts.length < 6) throw new IllegalArgumentException("Invalid CSV format for PickupOrder.");

        int orderId = Integer.parseInt(parts[0]);
        String confirmationDetails = parts[1];
        String customerName = parts[2];
        String[] itemsArray = parts[3].split("\\|");
        Map<Item, Integer> items = new HashMap<>();

        for (String itemEntry : itemsArray) {
            String[] itemParts = itemEntry.split(":");
            Item item = inventory != null ? inventory.getItem(itemParts[0]) : new Item(itemParts[0], 0.0, "Unknown", 0, -1);
            if (item == null) throw new IllegalArgumentException("Item not found: " + itemParts[0]);
            items.put(item, Integer.parseInt(itemParts[1]));
        }

        LocalDate orderDate = LocalDate.parse(parts[4]);
        String status = parts[5];

        PickupOrder order = new PickupOrder(customerName, items, orderDate);
        order.setOrderId(orderId);
        order.setConfirmationDetails(confirmationDetails);
        order.setStatus(status);
        return order;
    }
}
