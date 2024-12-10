package com.sportinggoods.controller;

import com.sportinggoods.model.Inventory;
import com.sportinggoods.model.Item;
import com.sportinggoods.model.ShippingOrder;
import com.sportinggoods.repository.ShippingOrderRepository;

import java.time.LocalDate;
import java.util.*;



public class ShippingController {

    private ShippingOrderRepository orderRepo;
    private Inventory inventory;

    public ShippingController(ShippingOrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    // Updated constructor to include Inventory
    public ShippingController(ShippingOrderRepository orderRepo, Inventory inventory) {
        this.orderRepo = orderRepo;
        this.inventory = inventory;
    }

    /**
     * Creates a new shipping order
     * @param customerFirstName
     * @param customerLastName
     * @param items
     * @param totalPrice
     * @param shippingAddress
     * @param customerEmail
     * @param customerPhoneNumber
     * @return True when added to the ShippingOrderRespository
     */
    public boolean handleShippingOrder(String customerFirstName, int storeId, String customerLastName, Map<Item, Integer> items,
                                       double totalPrice, String shippingAddress, String customerEmail, String customerPhoneNumber){

        String orderId = UUID.randomUUID().toString();

        LocalDate date =  LocalDate.now();


        ShippingOrder order = new ShippingOrder(
                orderId,
                storeId,
                customerFirstName,
                customerLastName,
                items,
                totalPrice,
                shippingAddress,
                customerEmail,
                customerPhoneNumber,
                date,
                "Confirmed"
        );

        return orderRepo.addShippingOrder(order);
    }

    /**
     * Ensures shipping order is available in a stores inventory
     * @param order
     * @param inventory
     * @return True if Shipping Order Repository is updated with the correct status
     */
    public boolean processShippingOrder(ShippingOrder order, Inventory inventory, int storeId) {
        Map<Item, Integer> shippedItems = new HashMap<>();
        Map<Item, Integer> unshippedItems = new HashMap<>();
        double shippedPrice = 0.0;

        // Process each item in the order
        for (Map.Entry<Item, Integer> entry : order.getItems().entrySet()) {
            Item item = entry.getKey();
            int requiredQuantity = entry.getValue();

            // Check availability in inventory
            if (inventory.checkAvailability(item.getName(), requiredQuantity)) {
                inventory.updateQuantity(item.getName(), -requiredQuantity);
                shippedItems.put(item, requiredQuantity);
                shippedPrice += item.getPrice() * requiredQuantity;
            } else {
                int availableQuantity = inventory.getItem(item.getName()).getQuantity();
                if (availableQuantity > 0) {
                    inventory.updateQuantity(item.getName(), -availableQuantity);
                    shippedItems.put(item, availableQuantity);
                    unshippedItems.put(item, requiredQuantity - availableQuantity);
                    shippedPrice += item.getPrice() * availableQuantity;
                } else {
                    unshippedItems.put(item, requiredQuantity);
                }
            }
        }

        if (!shippedItems.isEmpty()) {
            // Update the order's quantity and price for shipped items
            orderRepo.updateOrderQuantity(order.getOrderId(), shippedItems);
            orderRepo.updateOrderPrice(order.getOrderId(), shippedItems);

            // Handle completely shipped orders
            if (unshippedItems.isEmpty()) {
                return orderRepo.updateOrderStatus(order.getOrderId(), "Shipped");
            } else {
                // Adjust the original order's price and quantity
                double adjustedPrice = shippedPrice;
                order.setTotalPrice(adjustedPrice);
                orderRepo.updateOrderPrice(order.getOrderId(), shippedItems);
                orderRepo.updateOrderQuantity(order.getOrderId(), shippedItems);

                // Create a new order for unshipped items
                double newOrderPrice = unshippedItems.entrySet()
                        .stream()
                        .mapToDouble(e -> e.getKey().getPrice() * e.getValue())
                        .sum();
                handleShippingOrder(
                        order.getCustomerFirstName(), storeId, order.getCustomerLastName(),
                        unshippedItems, newOrderPrice, order.getShippingAddress(),
                        order.getCustomerEmail(), order.getCustomerPhoneNumber()
                );

                // Mark the original order as partially shipped
                return orderRepo.updateOrderStatus(order.getOrderId(), "Partially Shipped");
            }
        }

        System.out.println("No items could be shipped for order ID: " + order.getOrderId());
        return false;
    }




    /**
     * Handles in-store order pickup by verifying customer details, retrieving items, and updating order status.
     *
     * @param orderId The ID of the order to be picked up.
     * @param customerDetails Details provided by the customer (e.g., email or phone number).
     * @return True if the order is successfully picked up, false otherwise.
     */
    public boolean handleOrderPickup(String orderId, String customerDetails) {
        // Step 1: Retrieve the order by ID
        ShippingOrder order = orderRepo.getOrderById(orderId);
        if (order == null) {
            System.out.println("Order not found for ID: " + orderId);
            return false; // Order not found
        }

        // Step 2: Verify customer details (email or phone number)
        if (!order.getCustomerEmail().equalsIgnoreCase(customerDetails) &&
                !order.getCustomerPhoneNumber().equals(customerDetails)) {
            System.out.println("Customer verification failed. Details do not match.");
            return false; // Customer verification failed
        }

        // Step 3: Confirm item availability in inventory
        boolean allItemsAvailable = true;
        for (var entry : order.getItems().entrySet()) {
            String itemName = entry.getKey().getName();
            int quantity = entry.getValue();

            if (!inventory.checkAvailability(itemName, quantity)) {
                System.out.println("Item not available: " + itemName);
                allItemsAvailable = false;
            }
        }

        // Step 5a: If items are missing, notify the customer
        if (!allItemsAvailable) {
            System.out.println("Order cannot be picked up as some items are unavailable.");
            return false; // Items missing or unavailable
        }

        // Step 4: Retrieve items and confirm with the customer
        for (var entry : order.getItems().entrySet()) {
            inventory.updateQuantity(entry.getKey().getName(), -entry.getValue());
            System.out.println("Retrieved item: " + entry.getKey().getName() + " | Quantity: " + entry.getValue());
        }

        // Step 6: Update order status to 'Picked Up'
        boolean statusUpdated = orderRepo.updateOrderStatus(orderId, "Picked Up");
        if (!statusUpdated) {
            System.out.println("Failed to update order status.");
            return false; // Failed to update order status
        }

        // Step 7: Complete the transaction
        System.out.println("Order successfully picked up by the customer.");
        return true; // Order pickup successful
    }

}