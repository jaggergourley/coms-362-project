package com.sportinggoods.controller;

import com.sportinggoods.model.*;
import com.sportinggoods.repository.*;
import com.sportinggoods.util.*;

import java.util.*;
import java.util.stream.Collectors;

public class TonyMain {

    private static final Scanner scanner = new Scanner(System.in);
    private static CashierController cashierController;
    private static Inventory inventory;
    private static ReceiptRepository receiptRepo;

    public static void main(String[] args) {
        initializeSystem();
        mainMenu();
    }

    /**
     * Initializes all necessary components of the system.
     */
    private static void initializeSystem() {
        Cashier cashier = new Cashier("John Doe", 101, null); // Sample cashier
        inventory = new Inventory();
        Register register = new Register();
        receiptRepo = new ReceiptRepository();
        RegisterController registerController = new RegisterController(register);
        cashierController = new CashierController(cashier, inventory, registerController, receiptRepo);
    }

    /**
     * Displays the main menu and handles user role selection.
     */
    private static void mainMenu() {
        while (true) {
            System.out.println("\nSporting Goods Store - Main Menu:");
            System.out.println("1. Cashier");
            System.out.println("2. Customer");
            System.out.println("3. Exit");
            System.out.print("Enter your role: ");
            String roleChoice = scanner.nextLine().trim();

            switch (roleChoice) {
                case "1":
                    cashierMenu();
                    break;
                case "2":
                    customerMenu();
                    break;
                case "3":
                    System.out.println("Exiting the system. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Displays the cashier menu with options to process sales or handle returns.
     */
    private static void cashierMenu() {
        while (true) {
            System.out.println("\nCashier Menu:");
            System.out.println("1. Process Sale");
            System.out.println("2. Handle Return");
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    processSale();
                    break;
                case "2":
                    handleReturn();
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Displays the customer menu with options for shopping or returning items.
     */
    private static void customerMenu() {
        while (true) {
            System.out.println("\nCustomer Menu:");
            System.out.println("1. Shop for Items");
            System.out.println("2. Return Items");
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    purchaseItemAsCustomer();
                    break;
                case "2":
                    returnItemAsCustomer();
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Processes a multi-item sale.
     */
    private static void processSale() {
        Customer customer = getCustomerDetails();
        Map<Item, Integer> itemsToBuy = new HashMap<>();

        while (true) {
            List<Item> availableItems = inventory.getItems().values().stream()
                    .filter(item -> item.getStoreID() == 1)
                    .collect(Collectors.toList());

            if (availableItems.isEmpty()) {
                System.out.println("No items are available for sale at the moment.");
                return;
            }

            displayAvailableItems(availableItems);
            System.out.print("Enter the number of the item you want to add to the cart (or type 'checkout' to finish): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("checkout")) {
                break;
            }

            int itemChoice;
            try {
                itemChoice = Integer.parseInt(input) - 1;
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice. Please enter a valid number or type 'checkout'.");
                continue;
            }

            if (itemChoice < 0 || itemChoice >= availableItems.size()) {
                System.out.println("Invalid choice. Please try again.");
                continue;
            }

            Item selectedItem = availableItems.get(itemChoice);
            int quantity = promptForQuantity(selectedItem);

            if (quantity == -1) {
                continue; // Invalid quantity entered
            }

            // Add to cart
            itemsToBuy.put(selectedItem, itemsToBuy.getOrDefault(selectedItem, 0) + quantity);
            System.out.println(quantity + " of " + selectedItem.getName() + " added to cart.");
        }

        // Checkout
        if (!itemsToBuy.isEmpty()) {
            System.out.println("\nSelect Payment Method:");
            System.out.println("1. Cash");
            System.out.println("2. Card");
            System.out.print("Enter your choice (1 or 2): ");

            String paymentChoice = scanner.nextLine().trim();
            String paymentMethod;

            switch (paymentChoice) {
                case "1":
                    paymentMethod = "Cash";
                    break;
                case "2":
                    paymentMethod = "Card";
                    break;
                default:
                    System.out.println("Invalid choice. Defaulting to Cash.");
                    paymentMethod = "Cash";
            }

            Receipt receipt = cashierController.processSale(customer, itemsToBuy, paymentMethod);
            if (receipt != null) {
                System.out.println("Checkout completed successfully.");
            } else {
                System.out.println("Checkout failed.");
            }
        } else {
            System.out.println("No items to purchase.");
        }
    }

    /**
     * Displays available items to the user.
     *
     * @param availableItems List of available items.
     */
    private static void displayAvailableItems(List<Item> availableItems) {
        System.out.println("\nAvailable Items:");
        for (int i = 0; i < availableItems.size(); i++) {
            Item item = availableItems.get(i);
            System.out.printf("%d. %s (Price: $%.2f, Quantity: %d)\n",
                    i + 1, item.getName(), item.getPrice(), item.getQuantity());
        }
    }

    /**
     * Prompts the user to enter a valid quantity for a selected item.
     *
     * @param selectedItem The item selected by the user.
     * @return The quantity entered, or -1 if invalid.
     */
    private static int promptForQuantity(Item selectedItem) {
        System.out.print("Enter quantity: ");
        String quantityInput = scanner.nextLine().trim();

        if (!quantityInput.matches("\\d+")) {
            System.out.println("Invalid quantity. Please enter a valid number.");
            return -1;
        }

        int quantity = Integer.parseInt(quantityInput);
        if (quantity > selectedItem.getQuantity()) {
            System.out.println("Not enough stock available. Please enter a smaller quantity.");
            return -1;
        }

        return quantity;
    }

    /**
     * Handles the return process for the customer.
     */
    private static void handleReturn() {
        Customer customer = getCustomerDetails();
        Map<Item, Integer> itemsToReturn = new HashMap<>();

        while (true) {
            System.out.print("Enter item name (or type 'done' to finish adding items): ");
            String itemName = scanner.nextLine().trim();
            if (itemName.equalsIgnoreCase("done")) {
                break;
            }

            System.out.print("Enter quantity to return: ");
            String quantityInput = scanner.nextLine().trim();

            if (!quantityInput.matches("\\d+")) {
                System.out.println("Invalid quantity. Please enter a number.");
                continue;
            }

            int quantity = Integer.parseInt(quantityInput);

            // Validate the return against existing receipts
            if (!receiptRepo.hasReceiptForReturn(customer.getCustomerId(), itemName, quantity)) {
                System.out.println("Return denied: No valid receipt found for " + itemName + " with quantity " + quantity);
                continue;
            }

            // Retrieve the item from inventory based on name
            Item item = inventory.getItem(itemName);
            if (item == null) {
                System.out.println("Item not found in inventory. Please try again.");
                continue;
            }

            // Add item and quantity to return cart
            itemsToReturn.put(item, quantity);
            System.out.println("Added " + quantity + " of " + itemName + " to return cart.");
        }

        // Process the entire return cart as a single transaction
        if (!itemsToReturn.isEmpty()) {
            Receipt receipt = cashierController.handleReturn(customer, itemsToReturn);
            if (receipt != null) {
                System.out.println("Return processed successfully.");
            } else {
                System.out.println("Return processing failed.");
            }
        } else {
            System.out.println("No items to return.");
        }
    }

    /**
     * Handles a purchase by the customer.
     */
    private static void purchaseItemAsCustomer() {
        System.out.println("\nPurchasing items as a customer.");
        processSale();
    }

    /**
     * Handles a return by the customer.
     */
    private static void returnItemAsCustomer() {
        System.out.println("\nReturning items as a customer.");
        handleReturn();
    }

    /**
     * Gets customer details, either by ID for existing customers or as "Guest" for one-time purchases.
     *
     * @return A Customer object representing the current customer.
     */
    private static Customer getCustomerDetails() {
        System.out.print("Enter Customer ID (or press Enter for Guest): ");
        String customerIdInput = scanner.nextLine().trim();
        if (customerIdInput.isEmpty()) {
            return new Customer("Guest", -1); // Guest customer
        } else {
            try {
                int customerId = Integer.parseInt(customerIdInput);
                return new Customer("Returning Customer", customerId); // Returning customer
            } catch (NumberFormatException e) {
                System.out.println("Invalid Customer ID. Defaulting to Guest.");
                return new Customer("Guest", -1);
            }
        }
    }
}
