package com.sportinggoods.model;

import java.time.LocalDate;

public class Receipt {
    private int receiptId;
    private Customer customer;
    private Cashier cashier;
    private String receiptDetails; // Stores multiple items with quantities
    private double totalCost;
    private LocalDate date;

    // Constructor
    public Receipt(Customer customer, Cashier cashier, String receiptDetails, double totalCost, LocalDate date) {
        this.customer = customer;
        this.cashier = cashier;
        this.receiptDetails = receiptDetails;
        this.totalCost = totalCost;
        this.date = date;
    }

    // Default constructor
    public Receipt() {}

    // Getters and setters
    public int getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(int receiptId) {
        this.receiptId = receiptId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Cashier getCashier() {
        return cashier;
    }

    public String getReceiptDetails() {
        return receiptDetails;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public LocalDate getDate() {
        return date;
    }

    // Convert Receipt information to CSV format
    public String toCSV() {
        return receiptId + "," + customer.getCustomerId() + ",\"" + receiptDetails + "\"," + totalCost + "," + date;
    }

    @Override
    public String toString() {
        return "Receipt: " + receiptDetails + " for Customer: " + customer.getName() +
                " (ID: " + customer.getCustomerId() + "), Total: $" + totalCost + ", Date: " + date;
    }
}
