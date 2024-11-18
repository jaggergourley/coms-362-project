package com.sportinggoods.menu;

import com.sportinggoods.commands.MenuInvoker;
import com.sportinggoods.controller.*;
import com.sportinggoods.model.*;
import com.sportinggoods.util.InitializationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Represents the Manager Menu in the Sporting Goods Management System.
 * Allows managers to perform various administrative tasks.
 */
public class ManagerMenu {
    private Scanner scanner;
    private MenuInvoker managerInvoker;
    private InitializationManager initManager;
    private Employee employee;

    // Controllers obtained from InitializationManager
    private SupplierController supplierController;
    private PricingController pricingController;
    private GiftCardController giftCardController;
    private ShippingController shippingController;
    private Inventory inventory;

    /**
     * Constructs a ManagerMenu with the provided InitializationManager.
     *
     * @param initManager The InitializationManager instance for dependency injection.
     */
    public ManagerMenu(InitializationManager initManager) {
        this.scanner = new Scanner(System.in);
        this.managerInvoker = new MenuInvoker();
        this.initManager = initManager;

        // Initialize controllers from InitializationManager
        this.supplierController = initManager.getSupplierController();
        this.pricingController = initManager.getPricingController();
        this.giftCardController = initManager.getGiftCardController();
        this.shippingController = initManager.getShippingController();
        this.inventory = initManager.getInventory();

        // Initialize Employee
        this.employee = initManager.getEmployee();

        registerCommands();
    }

    /**
     * Registers manager menu commands with their corresponding actions.
     */
    private void registerCommands() {
        managerInvoker.register("1", this::coordinateSuppliers);
        managerInvoker.register("2", this::placeSupplierOrder);
        managerInvoker.register("3", this::viewAllSuppliers);
        managerInvoker.register("4", this::viewAllSupplierOrders);
        managerInvoker.register("5", this::adjustPriceMenu);
        managerInvoker.register("6", this::updateInventory);
        managerInvoker.register("7", this::manageGiftCards);
        managerInvoker.register("8", this::manageShifts);
        managerInvoker.register("9", this::manageShippingOrders);
        managerInvoker.register("10", this::backToMainMenu);
    }

    /**
     * Displays the Manager Menu and handles user input.
     */
    public void display() {
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
            System.out.println("9. Manage Shipping Orders");
            System.out.println("10. Back to Main Menu");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();
            if (choice.equals("10")) {
                break; // Exit to Main Menu
            }
            managerInvoker.executeCommand(choice);
        }
    }

    // ==========================
    // Supplier Coordination
    // ==========================

    /**
     * Handles the "Coordinate Suppliers" use case.
     */
    private void coordinateSuppliers() {
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
     * Manager Functionality: View All Suppliers
     */
    private void viewAllSuppliers() {
        List<Supplier> suppliers = supplierController.getAllSuppliers();
        if (suppliers.isEmpty()) {
            System.out.println("\nNo suppliers registered.");
            return;
        }
        System.out.println("\nRegistered Suppliers:");
        suppliers.forEach(System.out::println);
    }

    /**
     * Manager Functionality: Register a New Supplier
     */
    private void registerNewSupplier() {
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
     * Manager Functionality: Update Supplier Information
     */
    private void updateSupplierInformation() {
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

    // ==========================
    // Supplier Orders
    // ==========================

    /**
     * Manager Functionality: Place Supplier Order
     */
    private void placeSupplierOrder() {
        System.out.println("\nPlace Supplier Order:");

        // Step 1: Select Supplier
        List<Supplier> suppliers = supplierController.getAllSuppliers();
        if (suppliers.isEmpty()) {
            System.out.println("No suppliers available. Please register a supplier first.");
            return;
        }

        System.out.println("Available Suppliers:");
        suppliers.forEach(System.out::println);

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
        int quantity = promptForInteger("Enter Quantity: ", 1, Integer.MAX_VALUE);

        // Step 4: Enter Total Price
        double totalPrice = promptForDouble("Enter Total Price: ", 0.01, Double.MAX_VALUE);

        // Place the order
        boolean success = supplierController.placeSupplierOrder(supplierId, productDetails, quantity, totalPrice);
        if (success) {
            System.out.println("Order placed successfully.");
        } else {
            System.out.println("Failed to place order.");
        }
    }

    /**
     * Manager Functionality: View All Supplier Orders
     */
    private void viewAllSupplierOrders() {
        List<SupplierOrder> orders = supplierController.getAllSupplierOrders();
        if (orders.isEmpty()) {
            System.out.println("\nNo supplier orders found.");
            return;
        }
        System.out.println("\nSupplier Orders:");
        orders.forEach(System.out::println);
    }

    // ==========================
    // Pricing Management
    // ==========================

    /**
     * Manager Functionality: Adjust Item Price
     */
    private void adjustPriceMenu() {
        while (true) {
            System.out.println("\nAdjust Item Price Menu:");
            System.out.println("1. Search by Name");
            System.out.println("2. Search by Department");
            System.out.println("3. Search by Store ID");
            System.out.println("4. Back to Manager Menu");
            System.out.print("Enter your choice: ");

            String searchChoice = scanner.nextLine().trim();
            String criteria = switch (searchChoice) {
                case "1" -> "name";
                case "2" -> "department";
                case "3" -> "storeid";
                case "4" -> {
                    System.out.println("Returning to Manager Menu.");
                    yield null;
                }
                default -> {
                    System.out.println("Invalid choice. Please try again.");
                    yield null;
                }
            };
            if (criteria == null) continue;

            System.out.print("Enter the search value: ");
            String value = scanner.nextLine().trim();

            List<Item> foundItems = pricingController.searchItems(criteria, value);

            if (foundItems.isEmpty()) {
                System.out.println("No items found with the specified criteria. Please try another search.");
                continue;
            }

            System.out.println("\nFound Items:");
            for (int i = 0; i < foundItems.size(); i++) {
                System.out.printf("%d. %s\n", i + 1, foundItems.get(i));
            }

            System.out.print("Enter the number of the item you want to adjust (or 0 to return to search menu): ");
            int itemIndex = promptForInteger("", -1, foundItems.size() - 1);
            if (itemIndex == -1) continue;
            if (itemIndex < 0 || itemIndex >= foundItems.size()) {
                System.out.println("Invalid selection. Returning to search menu.");
                continue;
            }

            Item selectedItem = foundItems.get(itemIndex);
            double newPrice = promptForDouble("Enter the new price: ", 0.01, Double.MAX_VALUE);

            String result = pricingController.adjustPrice(selectedItem, newPrice);
            System.out.println(result);
            break;
        }
    }

    // ==========================
    // Inventory Management
    // ==========================

    /**
     * Manager Functionality: Update Inventory
     */
    private void updateInventory() {
        System.out.println("\nCurrent Inventory:");
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
                addItem();
                break;
            case "2":
                deleteItem();
                break;
            case "3":
                addItemsInBulk();
                break;
            case "4":
                deleteItemsInBulk();
                break;
            case "5":
                sendItemToAnotherStore();
                break;
            default:
                System.out.println("Not an option");
                break;
        }
    }

    /**
     * Adds a single item to the inventory.
     */
    private void addItem() {
        System.out.print("Enter item name: ");
        String name = scanner.nextLine();

        double price = promptForDouble("Enter item price: ", 0.01, Double.MAX_VALUE);

        System.out.print("Enter item Department: ");
        String department = scanner.nextLine();

        int quantity = promptForInteger("Enter item Quantity: ", 1, Integer.MAX_VALUE);

        int storeID = promptForInteger("Enter item Store ID: ", 1, Integer.MAX_VALUE);

        Item newItem = new Item(name, price, department, quantity, storeID);
        inventory.addItem(newItem);

        System.out.println("Updated Inventory:");
        inventory.printInventory();
        System.out.println();
    }

    /**
     * Deletes a single item from the inventory.
     */
    private void deleteItem() {
        System.out.print("Enter item name: ");
        String name = scanner.nextLine();

        double price = promptForDouble("Enter item price: ", 0.01, Double.MAX_VALUE);

        System.out.print("Enter item Department: ");
        String department = scanner.nextLine();

        int quantity = promptForInteger("Enter item Quantity: ", 1, Integer.MAX_VALUE);

        int storeID = promptForInteger("Enter item Store ID: ", 1, Integer.MAX_VALUE);

        Item itemToDelete = new Item(name, price, department, quantity, storeID);
        inventory.deleteItem(itemToDelete);

        System.out.println("Updated Inventory:");
        inventory.printInventory();
        System.out.println();
    }

    /**
     * Adds multiple items to the inventory in bulk.
     */
    private void addItemsInBulk() {
        ArrayList<Item> itemList = new ArrayList<>();
        boolean adding = true;

        while (adding) {
            System.out.println("Current item List:");
            itemList.forEach(item -> System.out.println(item));

            System.out.println("\nOptions:");
            System.out.println("+ - add item to list");
            System.out.println("x - finished adding items");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "+":
                    Item newItem = createItem();
                    if (newItem != null) {
                        itemList.add(newItem);
                    }
                    break;
                case "x":
                    adding = false;
                    break;
                default:
                    System.out.println("Not an option");
                    break;
            }
        }

        inventory.addItems(itemList);
        System.out.println("Items added");
        System.out.println("Updated Inventory:");
        inventory.printInventory();
        System.out.println();
    }

    /**
     * Deletes multiple items from the inventory in bulk.
     */
    private void deleteItemsInBulk() {
        ArrayList<Item> itemList = new ArrayList<>();
        boolean adding = true;

        while (adding) {
            System.out.println("Current item List:");
            itemList.forEach(item -> System.out.println(item));

            System.out.println("\nOptions:");
            System.out.println("+ - add item to list");
            System.out.println("x - finished adding items");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "+":
                    Item itemToDelete = createItem();
                    if (itemToDelete != null) {
                        itemList.add(itemToDelete);
                    }
                    break;
                case "x":
                    adding = false;
                    break;
                default:
                    System.out.println("Not an option");
                    break;
            }
        }

        inventory.deleteItems(itemList);
        System.out.println("Items removed");
        System.out.println("Updated Inventory:");
        inventory.printInventory();
        System.out.println();
    }

    /**
     * Sends an item to another store.
     */
    private void sendItemToAnotherStore() {
        System.out.print("Enter item name: ");
        String name = scanner.nextLine();

        int storeID = promptForInteger("Enter item's new Store ID: ", 1, Integer.MAX_VALUE);

        inventory.swapStore(name, storeID);
        System.out.println("Updated Inventory:");
        inventory.printInventory();
        System.out.println();
    }

    /**
     * Creates an Item by prompting user input.
     *
     * @return The created Item, or null if input is invalid.
     */
    private Item createItem() {
        try {
            System.out.print("Enter item name: ");
            String name = scanner.nextLine();

            double price = promptForDouble("Enter item price: ", 0.01, Double.MAX_VALUE);

            System.out.print("Enter item Department: ");
            String department = scanner.nextLine();

            int quantity = promptForInteger("Enter item Quantity: ", 1, Integer.MAX_VALUE);

            int storeID = promptForInteger("Enter item Store ID: ", 1, Integer.MAX_VALUE);

            return new Item(name, price, department, quantity, storeID);
        } catch (Exception e) {
            System.out.println("Error creating item: " + e.getMessage());
            return null;
        }
    }

    // ==========================
    // Gift Card Management
    // ==========================

    /**
     * Manager Functionality: Manage Gift Cards
     */
    private void manageGiftCards() {
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
    private void sellGiftCardMenu() {
        System.out.print("\nEnter the amount for the new gift card: ");
        double amount = promptForDouble("", 0.01, Double.MAX_VALUE);

        String result = giftCardController.sellGiftCard(amount);
        System.out.println(result);
    }

    /**
     * Redeem an existing gift card.
     */
    private void redeemGiftCardMenu() {
        System.out.print("\nEnter the gift card code: ");
        String code = scanner.nextLine().trim();

        double amount = promptForDouble("Enter the amount to redeem: ", 0.01, Double.MAX_VALUE);

        String result = giftCardController.redeemGiftCard(code, amount);
        System.out.println(result);
    }

    // ==========================
    // Work Shift Management
    // ==========================

    /**
     * Manager Functionality: Manage Work Shifts
     */
    private void manageShifts() {
        while (true) {
            System.out.println("\nWork Shift Management:");
            System.out.println("1. Add a Work Shift");
            System.out.println("2. Remove a Work Shift");
            System.out.println("3. Back to Manager Menu");
            System.out.print("Please select an option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addWorkShift();
                    break;
                case "2":
                    removeWorkShift();
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
     * Adds a new work shift.
     */
    private void addWorkShift() {
        try {
            int date = promptForInteger("Enter the date of shift (DD): ", 1, 31);
            String startTime = promptForTime("Enter the start time of the shift (HH:mm): ");
            String endTime = promptForTime("Enter the end time of the shift (HH:mm): ");

            Shift shift = new Shift(date, startTime, endTime);
            employee.getWorkSchedule().addShift(shift);

            System.out.println("Shift added: " + startTime + " to " + endTime);
        } catch (Exception e) {
            System.out.println("Error adding shift: " + e.getMessage());
        }
    }

    /**
     * Removes an existing work shift.
     */
    private void removeWorkShift() {
        try {
            int dateToRemove = promptForInteger("Enter the date of shift to remove (DD): ", 1, 31);
            String startTimeToRemove = promptForTime("Enter the start time of the shift to remove (HH:mm): ");
            String endTimeToRemove = promptForTime("Enter the end time of the shift to remove (HH:mm): ");

            Shift shift = new Shift(dateToRemove, startTimeToRemove, endTimeToRemove);
            employee.getWorkSchedule().deleteShift(shift);

            System.out.println("Shift removed with date: " + dateToRemove +
                    ", start time: " + startTimeToRemove + " and end time: " + endTimeToRemove);
        } catch (Exception e) {
            System.out.println("Error removing shift: " + e.getMessage());
        }
    }

    // ==========================
    // Shipping Order Management
    // ==========================

    /**
     * Manager Functionality: Manage Shipping Orders
     */
    private void manageShippingOrders() {
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
    private void viewAllShippingOrders() {
        System.out.println("\nAll Shipping Orders:");
        List<ShippingOrder> orders = initManager.getShippingRepo().getAllShippingOrders();
        if (orders.isEmpty()) {
            System.out.println("No shipping orders found.");
        } else {
            orders.forEach(System.out::println);
        }
    }

    /**
     * Processes and sends a selected shipment.
     */
    private void processAndSendShipment() {
        System.out.println("\nProcess and Send Shipment:");

        List<ShippingOrder> orders = initManager.getShippingRepo().getAllShippingOrders();

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
            System.out.println((i + 1) + ". Order ID: " + order.getOrderId() +
                    ", Customer: " + order.getCustomerFirstName() + " " + order.getCustomerLastName());
        }

        int orderNumber = promptForInteger("Enter order number: ", 1, confirmedOrders.size());
        ShippingOrder selectedOrder = confirmedOrders.get(orderNumber - 1);
        System.out.println("You selected Order ID: " + selectedOrder.getOrderId());

        Shipper shipper = new Shipper("Ben Jackson", 1, true, null, shippingController);
        shipper.shipOrder(selectedOrder, inventory);
        System.out.println("Order shipped successfully.");
    }

    /**
     * Returns to the main menu.
     */
    private void backToMainMenu() {
        // Optional: Additional logic before returning
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
     * @return The validated integer input.
     */
    private int promptForInteger(String prompt, int min, int max) {
        while (true) {
            if (!prompt.isEmpty()) {
                System.out.print(prompt);
            }
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value < min || value > max) {
                    System.out.println("Input must be between " + min + " and " + max + ".");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    /**
     * Prompts the user for a double within a specified range.
     *
     * @param prompt The prompt message.
     * @param min    The minimum acceptable value.
     * @param max    The maximum acceptable value.
     * @return The validated double input.
     */
    private double promptForDouble(String prompt, double min, double max) {
        while (true) {
            if (!prompt.isEmpty()) {
                System.out.print(prompt);
            }
            String input = scanner.nextLine().trim();
            try {
                double value = Double.parseDouble(input);
                if (value < min || value > max) {
                    System.out.println("Input must be between " + min + " and " + max + ".");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    /**
     * Prompts the user for a time in HH:mm format.
     *
     * @param prompt The prompt message.
     * @return The validated time string.
     */
    private String promptForTime(String prompt) {
        while (true) {
            System.out.print(prompt);
            String time = scanner.nextLine().trim();
            if (time.matches("^([01]?\\d|2[0-3]):[0-5]\\d$")) {
                return time;
            } else {
                System.out.println("Invalid time format. Please enter in HH:mm format.");
            }
        }
    }
}
