package com.sportinggoods.controller;

import com.sportinggoods.model.*;
import com.sportinggoods.repository.CouponRepository;
import com.sportinggoods.repository.ReceiptRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class CashierController {
    private Cashier cashier;
    private Inventory inventory;
    private RegisterController registerController;
    private ReceiptRepository receiptRepo;
    private CouponRepository couponRepo;

    public CashierController(Cashier cashier, Inventory inventory, RegisterController registerController, ReceiptRepository receiptRepo, CouponRepository couponRepo) {
        this.cashier = cashier;
        this.inventory = inventory;
        this.registerController = registerController;
        this.receiptRepo = receiptRepo;
        this.couponRepo = couponRepo;
    }

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
    public Receipt processSale(Customer customer, Map<Item, Integer> items, String paymentMethod, String couponCode) {
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

        // Apply coupon
        double discount = applyCoupon(couponCode, totalCost, receiptDetails);

        // Adjust total cost with discount
        totalCost -= discount;

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

    public double applyCoupon(String couponCode, double totalCost, StringBuilder receiptDetails) {
        double discount = 0.0;
    
        if (couponCode != null && !couponCode.isEmpty()) {
            Optional<Coupon> optionalCoupon = couponRepo.findByCode(couponCode);
            if (optionalCoupon.isPresent()) {
                Coupon coupon = optionalCoupon.get();
                if (!coupon.isExpired()) {
                    if (coupon.getDiscountType().equalsIgnoreCase("PERCENTAGE")) {
                        discount = totalCost * (coupon.getDiscountValue() / 100);
                    } else if (coupon.getDiscountType().equalsIgnoreCase("FIXED")) {
                        discount = coupon.getDiscountValue();
                    }
                    receiptDetails.append("Coupon applied: ").append(couponCode)
                        .append(" (-$").append(discount).append("), ");
                } else {
                    System.out.println("The coupon has expired.");
                }
            } else {
                System.out.println("Invalid coupon code.");
            }
        }
    
        return discount;
    }

    public double previewCoupon(String couponCode) {
        Optional<Coupon> coupon = couponRepo.findByCode(couponCode);
        if (coupon.isPresent() && !coupon.get().isExpired()) {
            return coupon.get().getDiscountValue(); // Return discount value
        }
        return 0.0; // Invalid or expired coupon
    }

    public boolean isPercentageCoupon(String couponCode) {
        Optional<Coupon> coupon = couponRepo.findByCode(couponCode);
        return coupon.isPresent() && "PERCENTAGE".equalsIgnoreCase(coupon.get().getDiscountType());
    }

    public void viewAllCoupons() {
        List<Coupon> activeCoupons = couponRepo.getActiveCoupons();
        if (activeCoupons.isEmpty()) {
            System.out.println("No active coupons available.");
        } else {
            System.out.println("\nActive Coupons:");
            for (Coupon coupon : activeCoupons) {
                System.out.println(coupon);
            }
        }
    }

    public void addNewCoupon() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter coupon code: ");
        String code = scanner.nextLine().trim();

        System.out.print("Enter discount type (PERCENTAGE/FIXED): ");
        String discountType = scanner.nextLine().trim().toUpperCase();

        System.out.print("Enter discount value: ");
        double discountValue;
        try {
            discountValue = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid discount value. Please enter a number.");
            return;
        }

        System.out.print("Enter expiration date (YYYY-MM-DD): ");
        String expirationDateInput = scanner.nextLine().trim();

        try {
            LocalDate expirationDate = LocalDate.parse(expirationDateInput);
            Coupon newCoupon = new Coupon(code, discountType, discountValue, expirationDate);
            couponRepo.addCoupon(newCoupon);
            System.out.println("Coupon added successfully.");
        } catch (Exception e) {
            System.out.println("Invalid date format. Coupon not added.");
        }
    }

    public void deleteCoupon() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the coupon code to delete: ");
        String code = scanner.nextLine().trim();

        boolean success = couponRepo.removeCoupon(code);
        if (success) {
            System.out.println("Coupon deleted successfully.");
        } else {
            System.out.println("Coupon not found. No changes made.");
        }
    }

    /**
     * Handles an order pickup by verifying customer details,
     * retrieving the order, and updating the order status.
     *
     * @param customer      The customer picking up the order.
     * @param items         A map of items and their quantities to be picked up.
     * @param confirmationDetails The details provided by the customer (e.g., order ID or receipt number).
     * @return The receipt if the pickup was successful, null otherwise.
     */
    public Receipt handleOrderPickup(Customer customer, Map<Item, Integer> items, String confirmationDetails) {
        System.out.println("Handling order pickup for Customer: " + customer.getName());

        // Step 1: Verify customer details
        if (confirmationDetails == null || confirmationDetails.isEmpty()) {
            System.out.println("Order confirmation details are missing.");
            return null;
        }
        System.out.println("Order confirmed with details: " + confirmationDetails);

        // Step 2: Verify item availability in inventory
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();

            if (!inventory.checkAvailability(item.getName(), quantity)) {
                System.out.println("Item " + item.getName() + " is out of stock or insufficient quantity for pickup.");
                return null;
            }
        }

        // Step 3: Update inventory and prepare the receipt details
        double totalCost = 0.0;
        StringBuilder receiptDetails = new StringBuilder();
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();

            // Update inventory
            inventory.updateQuantity(item.getName(), -quantity);

            // Build receipt details
            double itemCost = item.getPrice() * quantity;
            totalCost += itemCost;
            receiptDetails.append(item.getName())
                    .append(": ")
                    .append(quantity)
                    .append(" x $")
                    .append(item.getPrice())
                    .append(", ");
        }

        // Remove trailing comma and space
        if (receiptDetails.length() > 0) {
            receiptDetails.setLength(receiptDetails.length() - 2);
        }

        // Step 4: Log the pickup as a transaction and generate a receipt
        Receipt receipt = new Receipt(customer, cashier, receiptDetails.toString(), totalCost, LocalDate.now());
        receiptRepo.logReceipt(receipt);

        // Step 5: Confirm order pickup completion
        System.out.println("Order pickup completed successfully. Receipt: " + receipt);
        return receipt;
    }

}