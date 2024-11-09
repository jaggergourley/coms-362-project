package com.sportinggoods.model;

public class Customer {
    private String name;
    private int customerId;

    //Constructor
    public Customer(String name, int customerId) {
        this.name = name;
        this.customerId = customerId;
    }

    //Getters and Setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getCustomerId() {
        return customerId;
    }
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    // Additional methods or attributes

    // Converts a Customer object to a CSV string
    public String toCSV() {
        return customerId + "," + name;
    }

    // Creates a Customer object from a CSV string
    public static Customer fromCSV(String csvLine) {
        String[] tokens = csvLine.split(",");
        if (tokens.length != 2) {
            return null;  // Invalid format
        }
        int customerId = Integer.parseInt(tokens[0]);
        String name = tokens[1];
        return new Customer(name, customerId);
    }

    @Override
    public String toString() {
        return "Customer: " + name + " (ID: " + customerId + ")";
    }
}
