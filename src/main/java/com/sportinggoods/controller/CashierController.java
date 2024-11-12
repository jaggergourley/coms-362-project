package com.sportinggoods.controller;

import com.sportinggoods.model.*;
import com.sportinggoods.repository.ReceiptRepository;

import java.time.LocalDate;
import java.util.Map;

public class CashierController {
    private Cashier cashier;
    private Inventory inventory;
    private RegisterController registerController;
    private ReceiptRepository receiptRepo;

    public CashierController(Cashier cashier, Inventory inventory, RegisterController registerController, ReceiptRepository receiptRepo) {
        this.cashier = cashier;
        this.inventory = inventory;
        this.registerController = registerController;
        this.receiptRepo = receiptRepo;
    }

    /**
     * Processes a multi-item sale.
     *
     * @param customer      The customer making the purchase.
     * @param items         A map of items and their quantities.
     * @param paymentMethod The payment method used.
     * @return The receipt if the sale was successful, null otherwise.
     */
    public Receipt processSale(Customer customer, Map<Item, Integer> items, String paymentMethod) {
        double totalCost = 0.0;
        StringBuilder receiptDetails = new StringBuilder();

        // Calculate total cost and build receipt details
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();

            if (inventory.checkAvailability(item.getName(), quantity)) {
                double itemCost = item.getPrice() * quantity;
                totalCost += itemCost;
                receiptDetails.append(item.getName()).append(": ").append(quantity).append(" x $").append(item.getPrice()).append(" each, ");

                // Update inventory for each item
                inventory.updateQuantity(item.getName(), -quantity);
            } else {
                System.out.println("Item " + item.getName() + " is out of stock or insufficient quantity.");
                return null; // Exit if any item is unavailable
            }
        }

        // Remove trailing comma and space from receipt details
        if (receiptDetails.length() > 0) {
            receiptDetails.setLength(receiptDetails.length() - 2);
        }

        // Process single payment for the entire cart
        if (registerController.processPayment(totalCost, paymentMethod)) {
            Receipt receipt = new Receipt(customer, cashier, receiptDetails.toString(), totalCost, LocalDate.now());
            receiptRepo.logReceipt(receipt);
            System.out.println("Sale completed: " + receipt);
            return receipt;
        } else {
            System.out.println("Payment failed.");
        }

        return null;
    }

    /**
     * Handles a multi-item return.
     *
     * @param customer The customer returning the items.
     * @param items    A map of items and their quantities to return.
     * @return The receipt if the return was successful, null otherwise.
     */
    public Receipt handleReturn(Customer customer, Map<Item, Integer> items) {
        double totalRefund = 0.0;
        StringBuilder returnDetails = new StringBuilder();

        // Process each item in the return
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();

            if (item != null) {
                // Update inventory to add returned quantity back
                inventory.updateQuantity(item.getName(), quantity);

                // Calculate refund for the current item
                double itemRefund = item.getPrice() * quantity;
                totalRefund += itemRefund;

                // Build return details for the receipt
                returnDetails.append(item.getName()).append(": ").append(quantity).append(" x $").append(item.getPrice()).append(" each, ");
            } else {
                System.out.println("Item not found in inventory. Please try again.");
                return null;
            }
        }

        // Remove trailing comma and space from return details
        if (returnDetails.length() > 0) {
            returnDetails.setLength(returnDetails.length() - 2);
        }

        // Issue total refund
        registerController.issueRefund(totalRefund);
        Receipt receipt = new Receipt(customer, cashier, returnDetails.toString(), -totalRefund, LocalDate.now());
        receiptRepo.logReceipt(receipt);
        System.out.println("Return processed: " + receipt);
        return receipt;
    }
}