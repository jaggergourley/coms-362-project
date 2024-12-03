package com.sportinggoods.repository;

import com.sportinggoods.model.Inventory;
import com.sportinggoods.model.PickupOrder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PickupOrderRepository {
    private final String filePath = "data/pickup_orders.csv";
    private final Inventory inventory;


    public PickupOrderRepository(Inventory inventory) {
        this.inventory = inventory;
    }

    public PickupOrder addOrder(PickupOrder order) {
        try {
            // Generate confirmation number and order ID
            order.setOrderId(generateOrderId());
            order.setConfirmationDetails(generateConfirmationNumber());

            // Save to CSV
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                writer.write(order.toCSV());
                writer.newLine();
            }

            return order;
        } catch (IOException e) {
            System.err.println("Error adding order: " + e.getMessage());
            return null;
        }
    }

    public List<PickupOrder> getAllOrders() {
        List<PickupOrder> orders = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                orders.add(PickupOrder.fromCSV(line, inventory));
            }
        } catch (IOException e) {
            System.err.println("Error reading orders: " + e.getMessage());
        }
        return orders;
    }

    public Optional<PickupOrder> getOrderByConfirmation(String confirmationDetails) {
        return getAllOrders().stream()
                .filter(order -> order.getConfirmationDetails().equals(confirmationDetails))
                .findFirst();
    }


    public boolean updateOrderStatus(int orderId, String newStatus) {
        List<PickupOrder> orders = getAllOrders();
        boolean updated = false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (PickupOrder order : orders) {
                if (order.getOrderId() == orderId) {
                    order.setStatus(newStatus);
                    updated = true;
                }
                writer.write(order.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error updating order status: " + e.getMessage());
        }

        return updated;
    }

    private int generateOrderId() {
        return (int) (Math.random() * 100000); // Simple random order ID generator
    }

    private String generateConfirmationNumber() {
        return "ORD" + (int) (Math.random() * 1000000);
    }
}