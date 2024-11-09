package com.sportinggoods.controller;

import com.sportinggoods.model.*;
import com.sportinggoods.repository.ReceiptRepository;

import java.time.LocalDate;

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

    public Receipt processSale(Customer customer, String itemName, int quantity, String paymentMethod) {
        if (inventory.checkAvailability(itemName, quantity)) {
            Item item = inventory.getItem(itemName);
            double totalCost = item.getPrice() * quantity;

            if (registerController.processPayment(totalCost, paymentMethod)) {
                inventory.updateQuantity(itemName, -quantity);
                Receipt receipt = new Receipt(customer, cashier, itemName, quantity, totalCost, LocalDate.now());
                System.out.println("Sale completed: " + receipt);
                receiptRepo.logReceipt(receipt);
                return receipt;
            } else {
                System.out.println("Payment failed.");
            }
        } else {
            System.out.println("Item out of stock or insufficient quantity.");
        }
        return null;
    }

    public Receipt handleReturn(Customer customer, String itemName, int quantity) {
        Item item = inventory.getItem(itemName);
        if (item != null) {
            inventory.updateQuantity(itemName, quantity);
            double refundAmount = item.getPrice() * quantity;
            registerController.issueRefund(refundAmount);
            Receipt receipt = new Receipt(customer, cashier, itemName, -quantity, refundAmount, LocalDate.now());
            System.out.println("Return processed: " + receipt);
            receiptRepo.logReceipt(receipt);
            return receipt;
        }
        System.out.println("Item not found for return.");
        return null;
    }
}