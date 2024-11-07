package src.main.java.com.sportinggoods.model;

import java.time.LocalDate;

public class Receipt {
    private Customer customer;
    private Cashier cashier;
    private String itemName;
    private int quantity;
    private double totalCost;
    private LocalDate date;

    //Constructor
    public Receipt(Customer customer, Cashier cashier, String itemName, int quantity, double totalCost, LocalDate date) {
        this.customer = customer;
        this.cashier = cashier;
        this.itemName = itemName;
        this.quantity = quantity;
        this.totalCost = totalCost;
        this.date = date;
    }

    //Convert Receipt information to CSV file
    public String toCSV() {
        return customer.getCustomerId() + "," + itemName + "," + quantity + "," + totalCost + "," + date;
    }

    @Override
    public String toString() {
        return "Receipt: " + quantity + " x " + itemName + " for " + customer + ", Total: $" + totalCost + ", Date: " + date;
    }
}
