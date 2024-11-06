package com.sportinggoods.model;

public class Item {
    private String name;
    private double price;
    private String department;
    private int quantity;

    // Constructor, getters, setters
    public Item(String name, double price, String department, int quantity) {
        this.name = name;
        this.price = price;
        this.department = department;
        this.quantity = quantity;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
