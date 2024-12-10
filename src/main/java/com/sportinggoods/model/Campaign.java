package com.sportinggoods.model;

import java.time.LocalDate;

public class Campaign {
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private String type; // "Coupon" or "Discount"
    private double value; // Percentage or fixed amount
    private String customerMessage;
    private String status; // "Active" or "Ended"

    // Constructor
    public Campaign(String title, LocalDate startDate, LocalDate endDate, String type, double value, String customerMessage, String status) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.value = value;
        this.customerMessage = customerMessage;
        this.status = status;
    }

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }
    public String getCustomerMessage() { return customerMessage; }
    public void setCustomerMessage(String customerMessage) { this.customerMessage = customerMessage; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Campaign: " + title +
               " | Start: " + startDate +
               " | End: " + endDate +
               " | Type: " + type +
               " | Value: " + value +
               " | Status: " + status;
    }
}