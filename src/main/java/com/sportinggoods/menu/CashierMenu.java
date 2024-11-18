package com.sportinggoods.menu;

import com.sportinggoods.commands.MenuInvoker;
import com.sportinggoods.controller.*;
import com.sportinggoods.model.Customer;
import com.sportinggoods.model.Item;
import com.sportinggoods.util.InitializationManager;

import java.util.List;
import java.util.Scanner;

public class CashierMenu {
    private Scanner scanner;
    private MenuInvoker cashierInvoker;
    private InitializationManager initManager;

    // Controllers obtained from InitializationManager
    private CashierController cashierController;
    private GiftCardController giftCardController;

    public CashierMenu(InitializationManager initManager) {
        this.scanner = new Scanner(System.in);
        this.cashierInvoker = new MenuInvoker();
        this.initManager = initManager;

        // Initialize controllers from InitializationManager
        this.cashierController = initManager.getCashierController();
        this.giftCardController = initManager.getGiftCardController();

        registerCommands();
    }

    private void registerCommands() {
        cashierInvoker.register("1", this::processSale);
        cashierInvoker.register("2", this::handleReturn);
        cashierInvoker.register("3", this::sellGiftCardMenu);
        cashierInvoker.register("4", this::redeemGiftCardMenu);
        cashierInvoker.register("5", this::backToMainMenu);
    }

    public void display() {
        while (true) {
            System.out.println("\nCashier Menu:");
            System.out.println("1. Process Sale");
            System.out.println("2. Handle Return");
            System.out.println("3. Sell Gift Card");
            System.out.println("4. Redeem Gift Card");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();
            if (choice.equals("5")) {
                break; // Exit to Main Menu
            }
            cashierInvoker.executeCommand(choice);
        }
    }

    /**
     * Sell a new gift card.
     */
    private static void sellGiftCardMenu() {
        System.out.print("\nEnter the amount for the new gift card: ");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid amount format. Please enter a valid number.");
            return;
        }

        String result = giftCardController.sellGiftCard(amount);
        System.out.println(result);
    }

    /**
     * Redeem an existing gift card.
     */
    private static void redeemGiftCardMenu() {
        System.out.print("\nEnter the gift card code: ");
        String code = scanner.nextLine().trim();

        System.out.print("Enter the amount to redeem: ");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid amount format. Please enter a valid number.");
            return;
        }

        String result = giftCardController.redeemGiftCard(code, amount);
        System.out.println(result);
    }

    /**
     * Cashier Functionality: Process Sale
     */
    private static void processSale() {
        Customer customer = getCustomerDetails();
        Map<Item, Integer> itemsToBuy = new HashMap<>();

        while (true) {
            List<Item> availableItems = inventory.getItems().values().stream()
                    .filter(item -> item.getStoreID() == 1) // Assuming store ID 1 for simplicity
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
     * Cashier Functionality: Handle Return
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
