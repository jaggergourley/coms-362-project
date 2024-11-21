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
    public boolean handleShippingOrder(String customerFirstName, String customerLastName, Map<Item, Integer> items,
                                       double totalPrice, String shippingAddress, String customerEmail, String customerPhoneNumber){

        String orderId = UUID.randomUUID().toString();

        LocalDate date =  LocalDate.now();


        ShippingOrder order = new ShippingOrder(
                orderId,
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
     * @param i
     * @return True if Shipping Order Repository is updated with the correct status
     */
    public boolean processShippingOrder(ShippingOrder order, Inventory i){

        boolean available = false;
        Map<Item, Integer> shippedItems = new HashMap<>();
        Map<Item, Integer> unfinishedShippingOrder = new HashMap<>();

        for(Map.Entry<Item, Integer> entry : order.getItems().entrySet()){
            Item item = entry.getKey();
            int quantity = entry.getValue();


            if(i.checkAvailability(item.getName(), quantity)){
                i.updateQuantity(item.getName(), -quantity);
                shippedItems.put(item, quantity);
                System.out.println("Shipped " + item.getName() + ", Items Left: " + i.getItem(item.getName()).getQuantity());
            }
            else{
                int amountSendable = i.getItem(item.getName()).getQuantity();
                i.updateQuantity(item.getName(), -i.getItem(item.getName()).getQuantity());
                unfinishedShippingOrder.put(item, quantity - amountSendable);
                shippedItems.put(item, amountSendable);
                System.out.println(item.getName() + " only shipped " + amountSendable + "/" + quantity + ". The rest will be sent in another order from another store");

            }
        }


        if(shippedItems.isEmpty()){
            return false;
        }


        for(Map.Entry<Item, Integer> entry : order.getItems().entrySet()){
            for(Map.Entry<Item, Integer> shippedEntry : order.getItems().entrySet()){
                if(entry.getKey().equals(shippedEntry.getKey()) && entry.getValue() == shippedEntry.getValue()){
                    return orderRepo.updateOrderStatus(order.getOrderId(), "Shipped");
                }
                else{

                    double newPrice = 0;



                    for(Map.Entry<Item, Integer> price : unfinishedShippingOrder.entrySet()) {
                        Item item = entry.getKey();

                        newPrice += Math.round(item.getPrice() * item.getQuantity() * 100.0) / 100.0;  //might need to change
                    }
                    handleShippingOrder(order.getCustomerFirstName(), order.getCustomerLastName(), unfinishedShippingOrder, newPrice, order.getShippingAddress(), order.getCustomerEmail(), order.getCustomerPhoneNumber());
                    return orderRepo.updateOrderStatus(order.getOrderId(), "Partially Shipped") && orderRepo.updateOrderQuantity(order.getOrderId(), shippedItems) && orderRepo.updateOrderPrice(order.getOrderId(), shippedItems);
                }
            }
        }


        return true;
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