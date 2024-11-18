package com.sportinggoods.menu;

import com.sportinggoods.commands.MenuInvoker;
import com.sportinggoods.controller.InventoryController;
import com.sportinggoods.controller.ShippingController;
import com.sportinggoods.model.Customer;
import com.sportinggoods.model.Item;
import com.sportinggoods.repository.InventoryRepository;
import com.sportinggoods.util.InitializationManager;

import java.util.*;
import java.util.Map.Entry;

public class CustomerMenu {
    private Scanner scanner;
    private MenuInvoker customerInvoker;
    private InitializationManager initManager;

    // Controllers obtained from InitializationManager
    private InventoryController inventoryController;
    private ShippingController shippingController;

    public CustomerMenu(InitializationManager initManager) {
        this.scanner = new Scanner(System.in);
        this.customerInvoker = new MenuInvoker();
        this.initManager = initManager;

        // Initialize controllers from InitializationManager
        this.inventoryController = initManager.getInventoryController(); // Assuming such a method exists
        this.shippingController = initManager.getShippingController();

        registerCommands();
    }

    private void registerCommands() {
        customerInvoker.register("1", this::shopForItems);
        customerInvoker.register("2", this::returnItems);
        customerInvoker.register("3", this::placeShippingOrder);
        customerInvoker.register("4", this::backToMainMenu);
    }

    public void display() {
        while (true) {
            System.out.println("\nCustomer Menu:");
            System.out.println("1. Shop for Items");
            System.out.println("2. Return Items");
            System.out.println("3. Place Shipping Order");
            System.out.println("4. Back to Main Menu");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();
            if (choice.equals("4")) {
                break; // Exit to Main Menu
            }
            customerInvoker.executeCommand(choice);
        }
    }

    /**
     * Customer Functionality: Purchase Item
     */
    private static void purchaseItemAsCustomer() {
        System.out.println("\nPurchasing items as a customer.");
        processSale();
    }

    /**
     * Customer Functionality: Return Item
     */
    private static void returnItemAsCustomer() {
        System.out.println("\nReturning items as a customer.");
        handleReturn();
    }

    /**
     * Customer Functionality: Make Shipping Order
     */
    private static void makeShippingOrderInput() {
        System.out.println("\nPlace a Shipping Order:");

        // Get customer details
        System.out.print("Enter your first name: ");
        String customerFirstName = scanner.nextLine();

        System.out.print("Enter your last name: ");
        String customerLastName = scanner.nextLine();

        System.out.print("Enter shipping address: ");
        String shippingAddress = scanner.nextLine();

        System.out.print("Enter your email: ");
        String customerEmail = scanner.nextLine();

        System.out.print("Enter your phone number: ");
        String customerPhoneNumber = scanner.nextLine();

        // Collect items for the order
        Map<Item, Integer> items = new HashMap<>();
        double totalPrice = 0.0;

        while (true) {
            // Display inventory with numbered options
            List<Item> availableItems = new ArrayList<>(inventory.getItems().values());
            System.out.println("Select an item to add to the order (type 'done' to finish):");
            for (int i = 0; i < availableItems.size(); i++) {
                System.out.println((i + 1) + ". " + availableItems.get(i).getName() + " - $" + availableItems.get(i).getPrice());
            }

            // Get user input for item selection
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("done")) {
                break;
            }

            // Parse selection number and validate
            int itemIndex;
            try {
                itemIndex = Integer.parseInt(input) - 1;
                if (itemIndex < 0 || itemIndex >= availableItems.size()) {
                    System.out.println("Invalid selection. Please enter a valid item number.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number corresponding to an item or 'done' to finish.");
                continue;
            }

            Item selectedItem = availableItems.get(itemIndex);

            // Get quantity to order
            System.out.print("Quantity to order: ");
            int orderQuantity;
            try {
                orderQuantity = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity. Please enter a valid number.");
                continue;
            }

            if (!inventory.checkAvailability(selectedItem.getName(), orderQuantity)) {
                System.out.println("Insufficient quantity in inventory. Please enter a valid quantity.");
                continue;
            }

            Item newItem = new Item(selectedItem.getName(), selectedItem.getPrice(), selectedItem.getDepartment(), orderQuantity, selectedItem.getStoreID());
            items.put(newItem, orderQuantity);
            totalPrice += Math.round(selectedItem.getPrice() * orderQuantity * 100.0) / 100.0;
            System.out.println("Added " + orderQuantity + " of " + selectedItem.getName() + " to the order.");
        }

        // Create and handle the shipping order
        boolean orderCreated = shippingController.handleShippingOrder(
                customerFirstName, customerLastName, items, totalPrice, shippingAddress, customerEmail, customerPhoneNumber
        );

        if (orderCreated) {
            System.out.println("\nOrder created successfully!");
            System.out.println("Order Details:");
            System.out.println("Customer Name: " + customerFirstName + " " + customerLastName);
            System.out.println("Shipping Address: " + shippingAddress);
            System.out.println("Email: " + customerEmail);
            System.out.println("Phone Number: " + customerPhoneNumber);
            System.out.println("Items Ordered:");

            for (Item orderedItem : items.keySet()) {
                int quantity = orderedItem.getQuantity();
                System.out.println("Item: " + orderedItem.getName() + ", Quantity: " + quantity);
            }

            // Format the total price to two decimal places
            System.out.printf("Total Price: $%.2f\n", totalPrice);
            System.out.println("Order Status: Confirmed");
        } else {
            System.out.println("Failed to create the order.");
        }
    }
}
