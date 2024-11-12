package com.sportinggoods.controller;

import com.sportinggoods.model.*;
import com.sportinggoods.repository.*;
import com.sportinggoods.util.*;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class SportingGoodsMain {

    private static Scanner scanner = new Scanner(System.in);

    // Supplier-related repositories and controllers
    private static SupplierRepository supplierRepo;
    private static SupplierOrderRepository orderRepo;
    private static SupplierController supplierController;

    // Item-related controllers and repositories
    private static PricingController pricingController;
    private static ItemRepository itemRepository;

    // Gift card-related controllers and repositories
    private static GiftCardRepository giftCardRepository;
    private static GiftCardController giftCardController;

    // Cashier-related controllers and repositories
    private static CashierController cashierController;
    private static Inventory inventory;
    private static ReceiptRepository receiptRepo;
    private static RegisterController registerController;

    // Shipping-related repositories and controllers
    private static ShippingOrderRepository shippingRepo;
    private static ShippingController shippingController;

    // Employee and Schedule
    private static Employee employee;
    private static Schedule schedule;

    public static void main(String[] args) {
        initializeRepositories();
        initializeCashierSystem();
        fillInventory(); // Initialize inventory with items
        mainMenu();
    }

    /**
     * Initializes repositories and controllers.
     */
    private static void initializeRepositories() {
        supplierRepo = new SupplierRepository();
        orderRepo = new SupplierOrderRepository();
        supplierController = new SupplierController(supplierRepo, orderRepo);
        itemRepository = new ItemRepository();
        pricingController = new PricingController(itemRepository);
        giftCardRepository = new GiftCardRepository(new ArrayList<>());
        giftCardController = new GiftCardController(giftCardRepository);

        schedule = new Schedule();
        employee = new Employee("Mason", 1, schedule);

        shippingRepo = new ShippingOrderRepository();
        shippingController = new ShippingController(shippingRepo);
    }

    /**
     * Initializes cashier-related systems.
     */
    private static void initializeCashierSystem() {
        Cashier cashier = new Cashier("John Doe", 101, null); // Sample cashier
        inventory = new Inventory();
        Register register = new Register();
        receiptRepo = new ReceiptRepository();
        registerController = new RegisterController(register);
        cashierController = new CashierController(cashier, inventory, registerController, receiptRepo);
    }

    /**
     * Fills the inventory with initial items.
     */
    private static void fillInventory() {
        File file = new File("data/inventory.csv");

        // Check if file exists and is not empty
        if (file.exists() && file.length() > 0) {
            System.out.println("Inventory file already initialized.");
            return;
        }

        // Initialize items only if the file is missing or empty
        Item item1 = new Item("Tennis Ball", 29.99, "Sports", 10, 0);
        Item item2 = new Item("Tennis Racket", 89.99, "Sports", 5, 0);
        Item item3 = new Item("Football", 24.99, "Sports", 5, 0);
        Item item4 = new Item("Bike", 309.99, "Sports", 5, 0);
        Item item5 = new Item("Shorts", 89.99, "Apparel", 5, 0);

        inventory.addItem(item1);
        inventory.addItem(item2);
        inventory.addItem(item3);
        inventory.addItem(item4);
        inventory.addItem(item5);
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
            System.out.println("4. Customer");
            System.out.println("5. Exit");
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
                    customerMenu();
                    break;
                case "5":
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
            System.out.println("5. Adjust Item Price");
            System.out.println("6. Update Inventory");
            System.out.println("7. Manage Gift Cards");
            System.out.println("8. Manage Work Schedule");
            System.out.println("9. Manage Shipping Orders"); // New option
            System.out.println("10. Back to Main Menu");
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
                    adjustPriceMenu(); // Call the adjust price method
                    break;
                case "6":
                    updateInventory(); // Call the update inventory method
                    break;
                case "7":
                    manageGiftCards(); // Call the manage gift cards method
                    break;
                case "8":
                    manageShifts(); // Call the manage shifts method
                    break;
                case "9":
                    manageShippingOrders(); // New method for shipping orders
                    break;
                case "10":
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Displays the Cashier menu and handles cashier-specific actions.
     */
    private static void cashierMenu() {
        while (true) {
            System.out.println("\nCashier Menu:");
            System.out.println("1. Process Sale");
            System.out.println("2. Handle Return");
            System.out.println("3. Sell Gift Card");
            System.out.println("4. Redeem Gift Card");
            System.out.println("5. Back to Main Menu");
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
                    sellGiftCardMenu();
                    break;
                case "4":
                    redeemGiftCardMenu();
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Displays the Employee menu with placeholder functionalities.
     */
    private static void employeeMenu() {
        while (true) {
            System.out.println("\nEmployee Menu:");
            System.out.println("1. View Inventory");
            System.out.println("2. Process Shipping Orders"); // Employee can process shipping orders
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    getInventory();
                    break;
                case "2":
                    processAndSendShipment();
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Displays the Customer menu with options for shopping or returning items.
     */
    private static void customerMenu() {
        while (true) {
            System.out.println("\nCustomer Menu:");
            System.out.println("1. Shop for Items");
            System.out.println("2. Return Items");
            System.out.println("3. Place Shipping Order"); // Customers can place shipping orders
            System.out.println("4. Back to Main Menu");
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
                    makeShippingOrderInput();
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Manager Functionality: Coordinate Suppliers
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
     * Manager Functionality: Place Supplier Order
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

        // Place the order
        boolean success = supplierController.placeSupplierOrder(supplierId, productDetails, quantity, totalPrice);
        if (success) {
            System.out.println("Order placed successfully.");
        } else {
            System.out.println("Failed to place order.");
        }
    }

    /**
     * Manager Functionality: View All Suppliers
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
     * Manager Functionality: View All Supplier Orders
     */
    private static void viewAllSupplierOrders() {
        List<SupplierOrder> orders = supplierController.getAllSupplierOrders();
        if (orders.isEmpty()) {
            System.out.println("\nNo supplier orders found.");
            return;
        }
        System.out.println("\nSupplier Orders:");
        for (SupplierOrder order : orders) {
            System.out.println(order);
        }
    }

    /**
     * Manager Functionality: Adjust Item Price
     */
    private static void adjustPriceMenu() {
        System.out.print("\nEnter the name of the item to adjust the price: ");
        String itemName = scanner.nextLine().trim();

        Optional<Item> itemOpt = itemRepository.findByName(itemName);
        if (itemOpt.isEmpty()) {
            System.out.println("Error: Item not found. Returning to Manager Menu.");
            return;
        }

        double newPrice = -1;
        while (newPrice <= 0) {
            System.out.print("Enter the new price: ");
            try {
                newPrice = Double.parseDouble(scanner.nextLine().trim());
                if (newPrice <= 0) {
                    System.out.println("Error: Price must be greater than 0. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid price format. Please enter a valid number.");
            }
        }

        String result = pricingController.adjustPrice(itemName, newPrice);
        System.out.println(result);
    }

    /**
     * Manager Functionality: Manage Gift Cards
     */
    private static void manageGiftCards() {
        while (true) {
            System.out.println("\nGift Card Management:");
            System.out.println("1. Sell New Gift Card");
            System.out.println("2. Redeem Gift Card");
            System.out.println("3. Back to Manager Menu");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    sellGiftCardMenu();
                    break;
                case "2":
                    redeemGiftCardMenu();
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
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
     * Manager Functionality: Update Supplier Information
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
     * Manager Functionality: Registers a new supplier.
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

    /**
     * Manager Functionality: Update Inventory
     */
    private static void updateInventory() {
        System.out.println();
        System.out.println("Current Inventory:");
        inventory.printInventory();
        System.out.println("------------------");
        System.out.println("Select Inventory Operation");
        System.out.println("------------------");
        System.out.println("1. Add item");
        System.out.println("2. Delete item");
        System.out.println("3. Add items in bulk");
        System.out.println("4. Delete items in bulk");
        System.out.println("5. Send item to another store");

        System.out.print("Enter choice: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                System.out.print("Enter item name: ");
                String name = scanner.nextLine();

                System.out.print("Enter item price: ");
                double price = scanner.nextDouble();
                scanner.nextLine();

                System.out.print("Enter item Department: ");
                String department = scanner.nextLine();

                System.out.print("Enter item Quantity: ");
                int quantity = scanner.nextInt();

                System.out.print("Enter item Store ID: ");
                int storeID = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                // Create a new Item with the user input
                Item newItem = new Item(name, price, department, quantity, storeID);

                inventory.addItem(newItem);

                System.out.println("Updated Inventory: ");
                inventory.printInventory();
                System.out.println();
                break;
            case "2":
                System.out.print("Enter item name: ");
                name = scanner.nextLine();

                System.out.print("Enter item price: ");
                price = scanner.nextDouble();
                scanner.nextLine();

                System.out.print("Enter item Department: ");
                department = scanner.nextLine();

                System.out.print("Enter item Quantity: ");
                quantity = scanner.nextInt();

                System.out.print("Enter item Store ID: ");
                storeID = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                // Create a new Item with the user input
                newItem = new Item(name, price, department, quantity, storeID);
                inventory.deleteItem(newItem);

                System.out.println("Updated Inventory: ");
                inventory.printInventory();
                System.out.println();
                break;
            case "3":
                ArrayList<Item> itemList = new ArrayList<>();
                int x = 0;
                while(x == 0){
                    System.out.println("Current item List: ");
                    for(int i = 0; i < itemList.size(); i++){
                        System.out.println(itemList.get(i).toString());
                    }
                    System.out.println();

                    System.out.println("+ - add item to list");
                    System.out.println("x - finished adding items");
                    System.out.print("Enter choice: ");
                    choice = scanner.nextLine();

                    switch (choice) {
                        case "+":
                            System.out.print("Enter item name: ");
                            name = scanner.nextLine();

                            System.out.print("Enter item price: ");
                            price = scanner.nextDouble();
                            scanner.nextLine();

                            System.out.print("Enter item Department: ");
                            department = scanner.nextLine();

                            System.out.print("Enter item Quantity: ");
                            quantity = scanner.nextInt();

                            System.out.print("Enter item Store ID: ");
                            storeID = scanner.nextInt();
                            scanner.nextLine();

                            newItem = new Item(name, price, department, quantity, storeID);
                            itemList.add(newItem);
                            break;
                        case "x":
                            x = 1;
                            break;

                        default:
                            System.out.println("Not an option");
                            break;
                    }
                }
                inventory.addItems(itemList);
                System.out.println("Items added");
                System.out.println("Updated Inventory: ");
                inventory.printInventory();
                System.out.println();
                break;
            case "4":
                itemList = new ArrayList<>();
                x = 0;
                while(x == 0){
                    System.out.println("Current item List: ");
                    for(int i = 0; i < itemList.size(); i++){
                        System.out.println(itemList.get(i).toString());
                    }
                    System.out.println();

                    System.out.println("+ - add item to list");
                    System.out.println("x - finished adding items");
                    System.out.print("Enter choice: ");
                    choice = scanner.nextLine();

                    switch (choice) {
                        case "+":
                            System.out.print("Enter item name: ");
                            name = scanner.nextLine();

                            System.out.print("Enter item price: ");
                            price = scanner.nextDouble();
                            scanner.nextLine();

                            System.out.print("Enter item Department: ");
                            department = scanner.nextLine();

                            System.out.print("Enter item Quantity: ");
                            quantity = scanner.nextInt();

                            System.out.print("Enter item Store ID: ");
                            storeID = scanner.nextInt();
                            scanner.nextLine();

                            newItem = new Item(name, price, department, quantity, storeID);
                            itemList.add(newItem);
                            break;
                        case "x":
                            x = 1;
                            break;

                        default:
                            System.out.println("Not an option");
                            break;
                    }
                }
                inventory.deleteItems(itemList);
                System.out.println("Items removed");
                System.out.println("Updated Inventory: ");
                inventory.printInventory();
                System.out.println();
                break;
            case "5":
                System.out.print("Enter item name: ");
                name = scanner.nextLine();

                System.out.print("Enter item's new Store ID: ");
                storeID = scanner.nextInt();
                scanner.nextLine();

                inventory.swapStore(name, storeID);
                System.out.println("Updated Inventory: ");
                inventory.printInventory();
                System.out.println();
                break;
            default:
                System.out.println("Not an option");
                break;
        }
    }

    /**
     * Manager Functionality: Manage Work Shifts
     */
    private static void manageShifts() {
        while (true) {
            System.out.println("\nWork Shift Management:");
            System.out.println("1. Add a Work Shift");
            System.out.println("2. Remove a Work Shift");
            System.out.println("3. Back to Manager Menu");
            System.out.print("Please select an option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    // Add a Work Shift
                    System.out.print("Enter the date of shift (DD): ");
                    int date = Integer.parseInt(scanner.nextLine().trim());

                    System.out.print("Enter the start time of the shift (HH:mm): ");
                    String startTime = scanner.nextLine().trim();

                    System.out.print("Enter the end time of the shift (HH:mm): ");
                    String endTime = scanner.nextLine().trim();

                    Shift s = new Shift(date, startTime, endTime);
                    employee.getWorkSchedule().addShift(s);

                    System.out.println("Shift added: " + startTime + " to " + endTime);
                    break;

                case "2":
                    System.out.print("Enter the date of shift to remove (DD): ");
                    int dateToRemove = Integer.parseInt(scanner.nextLine().trim());

                    // Remove a Work Shift
                    System.out.print("Enter the start time of the shift to remove (HH:mm): ");
                    String startTimeToRemove = scanner.nextLine().trim();

                    System.out.print("Enter the end time of the shift to remove (HH:mm): ");
                    String endTimeToRemove = scanner.nextLine().trim();

                    s = new Shift(dateToRemove, startTimeToRemove, endTimeToRemove);
                    employee.getWorkSchedule().deleteShift(s);
                    System.out.println("Shift removed with date: "+ dateToRemove + ", start time: " + startTimeToRemove + " and end time: " + endTimeToRemove);
                    break;

                case "3":
                    return;  // Exit to Manager Menu

                default:
                    System.out.println("Invalid choice, please try again.");
                    break;
            }
        }
    }

    /**
     * Employee Functionality: View Inventory
     */
    private static void getInventory() {
        System.out.println("\nCurrent Inventory:");
        inventory.printInventory();
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

    /**
     * Employee Functionality: Process and Send Shipment
     */
    private static void processAndSendShipment() {
        System.out.println("\nProcess and Send Shipment:");

        List<ShippingOrder> orders = shippingRepo.getAllShippingOrders();

        List<ShippingOrder> confirmedOrders = orders.stream()
                .filter(order -> "Confirmed".equalsIgnoreCase(order.getStatus()))
                .collect(Collectors.toList());

        if (confirmedOrders.isEmpty()) {
            System.out.println("No confirmed orders available.");
        } else {
            System.out.println("Select a confirmed order by entering the corresponding number:");
            for (int i = 0; i < confirmedOrders.size(); i++) {
                ShippingOrder order = confirmedOrders.get(i);
                System.out.println((i + 1) + ". Order ID: " + order.getOrderId() + ", Customer: " +
                        order.getCustomerFirstName() + " " + order.getCustomerLastName());
            }

            System.out.print("Enter order number: ");
            int orderNumber;
            try {
                orderNumber = Integer.parseInt(scanner.nextLine().trim()) - 1;
                if (orderNumber < 0 || orderNumber >= confirmedOrders.size()) {
                    System.out.println("Invalid selection. Please enter a number within the list range.");
                    return;
                } else {
                    ShippingOrder selectedOrder = confirmedOrders.get(orderNumber);
                    System.out.println("You selected Order ID: " + selectedOrder.getOrderId());

                    Shipper shipper = new Shipper("Ben Jackson", 1, true, null, shippingController);
                    shipper.shipOrder(selectedOrder, inventory);
                    System.out.println("Order shipped successfully.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    /**
     * Manager Functionality: Manage Shipping Orders
     */
    private static void manageShippingOrders() {
        while (true) {
            System.out.println("\nShipping Order Management:");
            System.out.println("1. View All Shipping Orders");
            System.out.println("2. Process and Send Shipment");
            System.out.println("3. Back to Manager Menu");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    viewAllShippingOrders();
                    break;
                case "2":
                    processAndSendShipment();
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Manager Functionality: View All Shipping Orders
     */
    private static void viewAllShippingOrders() {
        System.out.println("\nAll Shipping Orders:");
        List<ShippingOrder> orders = shippingRepo.getAllShippingOrders();
        if (orders.isEmpty()) {
            System.out.println("No shipping orders found.");
        } else {
            for (ShippingOrder order : orders) {
                System.out.println(order);
            }
        }
    }
}
