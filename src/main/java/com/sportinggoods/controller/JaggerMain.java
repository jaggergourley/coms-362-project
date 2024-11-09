package com.sportinggoods.controller;

import com.sportinggoods.model.Supplier;
import com.sportinggoods.model.SupplierOrder;
import com.sportinggoods.repository.SupplierOrderRepository;
import com.sportinggoods.repository.SupplierRepository;
import com.sportinggoods.controller.SupplierController;
import com.sportinggoods.util.FileUtils;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;


public class JaggerMain {

    private static Scanner scanner = new Scanner(System.in);
    private static SupplierRepository supplierRepo;
    private static SupplierOrderRepository orderRepo;
    private static SupplierController supplierController;

    public static void main(String[] args) {
        initializeRepositories();
        supplierController = new SupplierController(supplierRepo, orderRepo);
        mainMenu();
    }

    /**
     * Initializes repositories and ensures data files are set up.
     */
    private static void initializeRepositories() {
        supplierRepo = new SupplierRepository();
        orderRepo = new SupplierOrderRepository();
    }

    /**
     * Displays the main menu and handles user role selection.
     */
    private static void mainMenu() {
        while (true) {
            System.out.println("\nWelcome to Sporting Goods Management System");
            System.out.println("Please select your role:");
            System.out.println("1. Manager");
            System.out.println("2. Cashier");
            System.out.println("3. Employee");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    managerMenu();
                    break;
                case "2":
                    cashierMenu();
                    break;
                case "3":
                    employeeMenu();
                    break;
                case "4":
                    System.out.println("Exiting the system. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Displays the Manager menu and handles manager-specific actions.
     */
    private static void managerMenu() {
        while (true) {
            System.out.println("\nManager Menu:");
            System.out.println("1. Coordinate Suppliers");
            System.out.println("2. Place Supplier Order");
            System.out.println("3. View All Suppliers");
            System.out.println("4. View All Supplier Orders");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    coordinateSuppliers();
                    break;
                case "2":
                    placeSupplierOrder();
                    break;
                case "3":
                    viewAllSuppliers();
                    break;
                case "4":
                    viewAllSupplierOrders();
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Placeholder for Cashier functionalities.
     */
    private static void cashierMenu() {
        System.out.println("\nCashier functionalities are under development.");
        // Implement Cashier-related actions here
    }

    /**
     * Placeholder for Employee functionalities.
     */
    private static void employeeMenu() {
        System.out.println("\nEmployee functionalities are under development.");
        // Implement Employee-related actions here
    }

    /**
     * Handles the "Coordinate Suppliers" use case.
     */
    private static void coordinateSuppliers() {
        while (true) {
            System.out.println("\nCoordinate Suppliers:");
            System.out.println("1. View Suppliers");
            System.out.println("2. Update Supplier Information");
            System.out.println("3. Register New Supplier");
            System.out.println("4. Back to Manager Menu");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    viewAllSuppliers();
                    break;
                case "2":
                    updateSupplierInformation();
                    break;
                case "3":
                    registerNewSupplier();
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Views all registered suppliers.
     */
    private static void viewAllSuppliers() {
        List<Supplier> suppliers = supplierController.getAllSuppliers();
        if (suppliers.isEmpty()) {
            System.out.println("\nNo suppliers registered.");
            return;
        }
        System.out.println("\nRegistered Suppliers:");
        for (Supplier supplier : suppliers) {
            System.out.println(supplier);
        }
    }

    /**
     * Updates information for an existing supplier.
     */
    private static void updateSupplierInformation() {
        System.out.print("\nEnter Supplier ID to update: ");
        String supplierId = scanner.nextLine();

        Supplier supplier = supplierController.getSupplierById(supplierId);
        if (supplier == null) {
            System.out.println("Supplier not found.");
            return;
        }

        System.out.println("Current Supplier Information:");
        System.out.println(supplier);

        System.out.print("Enter new Relationship Status (current: " + supplier.getRelationshipStatus() + "): ");
        String newStatus = scanner.nextLine();
        if (newStatus.isEmpty()) {
            newStatus = supplier.getRelationshipStatus(); // Retain current if input is empty
        }

        System.out.print("Enter Follow-Up Action (current: " + supplier.getFollowUpAction() + "): ");
        String newAction = scanner.nextLine();
        if (newAction.isEmpty()) {
            newAction = supplier.getFollowUpAction(); // Retain current if input is empty
        }

        boolean success = supplierController.coordinateSuppliers(supplierId, newStatus, newAction);
        if (success) {
            System.out.println("Supplier updated successfully.");
        } else {
            System.out.println("Failed to update supplier.");
        }
    }

    /**
     * Registers a new supplier.
     */
    private static void registerNewSupplier() {
        System.out.print("\nEnter Supplier ID: ");
        String supplierId = scanner.nextLine();

        if (supplierController.getSupplierById(supplierId) != null) {
            System.out.println("Supplier ID already exists. Please try a different ID.");
            return;
        }

        System.out.print("Enter Supplier Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Contact Information: ");
        String contactInfo = scanner.nextLine();

        System.out.print("Enter Relationship Status: ");
        String relationshipStatus = scanner.nextLine();

        System.out.print("Enter Follow-Up Action: ");
        String followUpAction = scanner.nextLine();

        Supplier newSupplier = new Supplier(supplierId, name, contactInfo, relationshipStatus, followUpAction);
        boolean success = supplierController.addSupplier(newSupplier);
        if (success) {
            System.out.println("New supplier registered successfully.");
        } else {
            System.out.println("Failed to register new supplier.");
        }
    }

    /**
     * Handles the "Place Supplier Order" use case.
     */
    private static void placeSupplierOrder() {
        System.out.println("\nPlace Supplier Order:");

        // Step 1: Select Supplier
        List<Supplier> suppliers = supplierController.getAllSuppliers();
        if (suppliers.isEmpty()) {
            System.out.println("No suppliers available. Please register a supplier first.");
            return;
        }

        System.out.println("Available Suppliers:");
        for (Supplier supplier : suppliers) {
            System.out.println(supplier);
        }

        System.out.print("Enter Supplier ID to place order: ");
        String supplierId = scanner.nextLine();
        Supplier supplier = supplierController.getSupplierById(supplierId);
        if (supplier == null) {
            System.out.println("Supplier not found.");
            return;
        }

        // Step 2: Enter Product Details
        System.out.print("Enter Product Details: ");
        String productDetails = scanner.nextLine();

        // Step 3: Enter Quantity
        int quantity = 0;
        while (true) {
            System.out.print("Enter Quantity: ");
            String qtyInput = scanner.nextLine();
            try {
                quantity = Integer.parseInt(qtyInput);
                if (quantity <= 0) {
                    System.out.println("Quantity must be greater than zero.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity. Please enter a valid integer.");
            }
        }

        // Step 4: Enter Total Price
        double totalPrice = 0.0;
        while (true) {
            System.out.print("Enter Total Price: ");
            String priceInput = scanner.nextLine();
            try {
                totalPrice = Double.parseDouble(priceInput);
                if (totalPrice <= 0) {
                    System.out.println("Total price must be greater than zero.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid price. Please enter a valid number.");
            }
        }

        // Handle funds and stock checks here
        // For now assume funds are sufficient and items are in stock

        // Place the order
        boolean success = supplierController.placeSupplierOrder(supplierId, productDetails, quantity, totalPrice);
        if (success) {
            System.out.println("Order placed successfully.");
        } else {
            System.out.println("Failed to place order.");
        }
    }

    /**
     * Views all supplier orders.
     */
    private static void viewAllSupplierOrders() {
        List<com.sportinggoods.model.SupplierOrder> orders = supplierController.getAllSupplierOrders();
        if (orders.isEmpty()) {
            System.out.println("\nNo supplier orders found.");
            return;
        }
        System.out.println("\nSupplier Orders:");
        for (com.sportinggoods.model.SupplierOrder order : orders) {
            System.out.println(order);
        }
    }
}
