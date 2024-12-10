package com.sportinggoods.menu;

import com.sportinggoods.controller.*;
import com.sportinggoods.model.*;
import com.sportinggoods.repository.*;
import com.sportinggoods.util.InitializationManager;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
/**
 * Represents the Employee Menu in the Sporting Goods Management System.
 * Allows employees to perform various operational tasks.
 */
public class EmployeeMenu extends BaseMenu {

    // Controllers
    private CashierController cashierController;
    private ShippingController shippingController;

    // Repositories and Models
    private Employee employee;
    private Inventory inventory;
    private ShippingOrderRepository shippingRepo;

    private static final String LOW_STOCK_FILE = "data/lowStock.csv";
    private int storeId;

    /**
     * Constructs an EmployeeMenu with the provided InitializationManager and Scanner.
     *
     * @param initManager The InitializationManager instance for dependency injection.
     * @param scanner     The shared Scanner instance for user input.
     */
    public EmployeeMenu(InitializationManager initManager, Scanner scanner, int storeId) {
        super(initManager, scanner);
        this.storeId = storeId;
        // Initialize controllers and repositories
        this.cashierController = initManager.getCashierController();
        this.shippingController = initManager.getShippingController();
        this.shippingRepo = initManager.getShippingOrderRepo();
        this.inventory = initManager.getInventory(storeId);
        this.employee = initManager.getEmployee();
    }

    @Override
    protected void registerCommands() {
        invoker.register("1", this::viewInventory);
        invoker.register("2", this::processAndSendShipment);
        invoker.register("3", this::restockDepartmentItems);
        invoker.register("4", this::viewLowStockRequests);

    }

    @Override
    protected void printMenuOptions() {
        clearConsole();
        System.out.println("\nEmployee Menu:");
        System.out.println("1. View Inventory");
        System.out.println("2. Process Shipping Orders");
        System.out.println("3. Restock Department Items");
        System.out.println("4. View Low Stock Requests");
        System.out.println("5. Back to Main Menu");
    }

    @Override
    protected boolean isExitChoice(String choice) {
        return choice.equals("5");
    }

    @Override
    protected void handleExit() {
        System.out.println("Returning to Main Menu...");
    }

    // ==========================
    // Inventory Management
    // ==========================

    /**
     * Displays the current inventory.
     */
    private void viewInventory() {
        System.out.println("\nCurrent Inventory:");
        inventory.printInventory();
    }

    /**
     * Restocks department items by invoking the Inventory's method.
     */
    private void restockDepartmentItems() {
        clearConsole();
        inventory.restockDepartmentItems(this.storeId);
        promptReturn();
    }

    private void viewLowStockRequests() {
        clearConsole();
        File lowStockFile = new File(LOW_STOCK_FILE);

        if (!lowStockFile.exists()) {
            System.out.println("No low stock requests found.");
            promptReturn();
            return;
        }

        System.out.println("Low Stock Requests:");
        try (BufferedReader reader = new BufferedReader(new FileReader(lowStockFile))) {
            String header = reader.readLine(); // Skip header
            String line;
            boolean hasItems = false;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                hasItems = true;
            }
            if (!hasItems) {
                System.out.println("No items currently flagged for low stock.");
            }
        } catch (Exception e) {
            System.err.println("Error reading low stock requests: " + e.getMessage());
        }
        promptReturn();
    }

    // ==========================
    // Shipping Order Management
    // ==========================

    /**
     * Processes and sends a selected shipment.
     */
    private void processAndSendShipment() {
        System.out.println("\nProcess and Send Shipment:");

        List<ShippingOrder> orders = shippingRepo.getAllShippingOrdersByStoreId(storeId);
        List<ShippingOrder> confirmedOrders = orders.stream()
                .filter(order -> "Confirmed".equalsIgnoreCase(order.getStatus()))
                .collect(Collectors.toList());

        if (confirmedOrders.isEmpty()) {
            System.out.println("No confirmed orders available.");
            return;
        }

        System.out.println("Select a confirmed order by entering the corresponding number:");
        for (int i = 0; i < confirmedOrders.size(); i++) {
            ShippingOrder order = confirmedOrders.get(i);
            System.out.printf("%d. Order ID: %s, Customer: %s %s%n",
                    i + 1, order.getOrderId(), order.getCustomerFirstName(), order.getCustomerLastName());
        }

        System.out.print("Enter order number: ");
        int orderNumber = promptForInteger("", 1, confirmedOrders.size());

        if (orderNumber == -1) {
            return; // User chose to cancel
        }

        ShippingOrder selectedOrder = confirmedOrders.get(orderNumber - 1);
        System.out.println("You selected Order ID: " + selectedOrder.getOrderId());

        Shipper shipper = new Shipper("Ben Jackson", 1, true, null, shippingController, 1);
        shipper.shipOrder(selectedOrder, inventory);
        System.out.println("Order shipped successfully.");
    }

    // ==========================
    // Helper Methods
    // ==========================

    /**
     * Prompts the user for an integer within a specified range.
     *
     * @param prompt The prompt message.
     * @param min    The minimum acceptable value.
     * @param max    The maximum acceptable value.
     * @return The validated integer input, or -1 if the user chooses to cancel.
     */
    private int promptForInteger(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("cancel")) {
                return -1; // Indicates cancellation
            }
            try {
                int value = Integer.parseInt(input);
                if (value < min || value > max) {
                    System.out.println("Input must be between " + min + " and " + max + ".");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer or type 'cancel' to abort.");
            }
        }
    }

    /**
     * Prompts the user for a double within a specified range.
     *
     * @param prompt The prompt message.
     * @param min    The minimum acceptable value.
     * @param max    The maximum acceptable value.
     * @return The validated double input, or -1 if the user chooses to cancel.
     */
    private double promptForDouble(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("cancel")) {
                return -1; // Indicates cancellation
            }
            try {
                double value = Double.parseDouble(input);
                if (value < min || value > max) {
                    System.out.println("Input must be between " + min + " and " + max + ".");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number or type 'cancel' to abort.");
            }
        }
    }

    /**
     * Prompts the user to press Enter to return to the menu.
     */
    private void promptReturn() {
        System.out.println("\nPress Enter to return to the menu...");
        scanner.nextLine();
    }
}

