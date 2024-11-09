package com.sportinggoods.model;

import java.util.ArrayList;
import java.util.List;

public class GiftCard {
    private String code;
    private double balance;
    private boolean isActive;
    private List<String> transactionHistory;

    public GiftCard(String code, double initialBalance) {
        this.code = code;
        this.balance = initialBalance;
        this.isActive = true;
        this.transactionHistory = new ArrayList<>();
        logTransaction("Gift Card created with initial balance: $" + initialBalance);
    }

    public String getCode() {
        return code;
    }

    public double getBalance() {
        return balance;
    }

    public boolean isActive() {
        return isActive;
    }

    public List<String> getTransactionHistory() {
        return transactionHistory;
    }

    // Log a transaction in the history
    private void logTransaction(String transaction) {
        transactionHistory.add(transaction);
    }

    // Redeem an amount from the balance
    public String redeem(double amount) {
        if (!isActive || balance <= 0) {
            return "Error: Gift card is inactive or fully redeemed.";
        }
        if (amount <= 0 || amount > balance) {
            return "Error: Invalid amount. Please enter a valid amount within balance limits.";
        }
        balance -= amount;
        logTransaction("Redeemed: $" + amount + ". Remaining balance: $" + balance);
        if (balance == 0) {
            isActive = false;
        }
        return "Redemption successful. Remaining balance: $" + balance;
    }

    // Activate a new gift card or reactivate one with added balance
    public void activate(double amount) {
        this.balance = amount;
        this.isActive = true;
        logTransaction("Gift Card activated with balance: $" + amount);
    }

    @Override
    public String toString() {
        return "Gift Card [Code: " + code + ", Balance: $" + balance + ", Active: " + isActive + "]";
    }
}