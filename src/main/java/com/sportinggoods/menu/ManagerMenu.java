package com.sportinggoods.menu;

import com.sportinggoods.controller.*;
import com.sportinggoods.model.*;
import com.sportinggoods.repository.*;
import com.sportinggoods.util.InitializationManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Represents the Manager Menu in the Sporting Goods Management System.
 * Allows managers to perform various administrative tasks.
 */
public class ManagerMenu extends BaseMenu {

    // Controllers
    private CashierController cashierController;
    private DiscountController discountController;
    private GiftCardController giftCardController;
    private PricingController pricingController;
    private ShippingController shippingController;
    private SupplierController supplierController;

    // Repositories and Models
    private Employee employee;
    private Inventory inventory;
    private ShippingOrderRepository shippingRepo;

    /**
     * Constructs a ManagerMenu with the provided InitializationManager and Scanner.
     *
     * @param initManager The InitializationManager instance for dependency injection.
     * @param scanner     The shared Scanner instance for user input.
     */
    public ManagerMenu(InitializationManager initManager, Scanner scanner) {
        super(initManager, scanner);
        // Initialize controllers and models
        this.cashierController = initManager.getCashierController();
        this.discountController = initManager.getDiscountController();
        this.giftCardController = initManager.getGiftCardController();
        this.pricingController = initManager.getPricingController();
        this.shippingController = initManager.getShippingController();
        this.supplierController = initManager.getSupplierController();
        this.shippingRepo = initManager.getShippingOrderRepo();
        this.inventory = initManager.getInventory();
        this.employee = initManager.getEmployee();
    }

    @Override
    protected void registerCommands() {
        invoker.register("1", this::coordinateSuppliers);
        invoker.register("2", this::placeSupplierOrder);
        invoker.register("3", this::viewAllSuppliers);
        invoker.register("4", this::viewAllSupplierOrders);
        invoker.register("5", this::adjustPriceMenu);
        invoker.register("6", this::updateInventory);
        invoker.register("7", this::manageGiftCards);
        invoker.register("8", this::manageShifts);
        invoker.register("9", this::manageShippingOrders);
        invoker.register("10", this::manageCoupons);
        invoker.register("11", this::manageDiscounts);
        invoker.register("12", this::generateLowStockRequest);
    }

    @Override
    protected void printMenuOptions() {
        clearConsole();
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
        System.out.println("10. Manage Coupons");
        System.out.println("11. Manage Discounts");
        System.out.println("12. Generate Low Stock Request");
        System.out.println("13. Back to Main Menu");
    }

    @Override
    protected boolean isExitChoice(String choice) {
        return choice.equals("12");
    }

    @Override
    protected void handleExit() {
        System.out.println("Returning to Main Menu...");
    }

    // ==========================
    // Coupon Management
    // ==========================

    /**
     * Manages coupon-related operations.
     */
    private void manageCoupons() {
        clearConsole();
        while (true) {
            System.out.println("\nManage Coupon Codes:");
            System.out.println("1. View All Coupons");
            System.out.println("2. Add New Coupon");
            System.out.println("3. Delete a Coupon");
            System.out.println("4. Back to Manager Menu");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    cashierController.viewAllCoupons();
                    break;
                case "2":
                    cashierController.addNewCoupon();
                    break;
                case "3":
                    cashierController.deleteCoupon();
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // ==========================
    // Discount Management
    // ==========================

    /**
     * Manages discount-related operations.
     */
    private void manageDiscounts() {
        while (true) {
            clearConsole();
            System.out.println("\nManage Discounts:");
            System.out.println("1. Add Discount");
            System.out.println("2. Remove Discount");
            System.out.println("3. View All Discounts");
            System.out.println("4. Back to Manager Menu");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleAddDiscount();
                    break;
                case "2":
                    handleRemoveDiscount();
                    break;
                case "3":
                    viewAllDiscounts();
                    break;
                case "4":
                    clearConsole();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Adds a new discount based on user input.
     */
    private void handleAddDiscount() {
        clearConsole();
        System.out.println("\nSelect Discount Type:");
        System.out.println("1. Apply Discount to Item");
        System.out.println("2. Apply Discount to Department");
        System.out.println("3. Apply Store-Wide Discount");
        System.out.print("Enter your choice: ");

        String discountTypeChoice = scanner.nextLine().trim();

        clearConsole();
        System.out.print("Enter discount value (numeric): ");
        double value;
        try {
            value = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Discount value must be a number.");
            promptReturn();
            return;
        }

        System.out.print("Enter discount type (PERCENTAGE/FIXED): ");
        String type = scanner.nextLine().trim();

        switch (discountTypeChoice) {
            case "1":
                System.out.print("Enter item name: ");
                String itemName = scanner.nextLine().trim();
                System.out.println(discountController.addDiscountToItem(itemName, value, type));
                break;
            case "2":
                System.out.print("Enter department name: ");
                String department = scanner.nextLine().trim();
                System.out.println(discountController.addDiscountToDepartment(department, value, type));
                break;
            case "3":
                System.out.println(discountController.addDiscountStoreWide(value, type));
                break;
            default:
                System.out.println("Invalid choice. Returning to Manage Discounts menu.");
        }

        promptReturn();
    }

    /**
     * Removes an existing discount based on user selection.
     */
    private void handleRemoveDiscount() {
        clearConsole();
        List<Discount> discounts = discountController.listDiscounts();

        if (discounts.isEmpty()) {
            System.out.println("No active discounts to remove.");
            promptReturn();
            return;
        }

        System.out.println("Active Discounts:");
        for (int i = 0; i < discounts.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, discounts.get(i));
        }

        System.out.print("\nEnter the number of the discount to remove (or 0 to cancel): ");
        int choice = promptForInteger("", 0, discounts.size());

        if (choice == 0) {
            return; // Cancel removal
        }

        Discount selectedDiscount = discounts.get(choice - 1);
        String result = discountController.removeDiscount(selectedDiscount.getTarget());
        System.out.println(result);
        promptReturn();
    }

    /**
     * Displays all active discounts.
     */
    private void viewAllDiscounts() {
        clearConsole();
        List<Discount> discounts = discountController.listDiscounts();
        if (discounts.isEmpty()) {
            System.out.println("No active discounts.");
        } else {
            System.out.println("Active Discounts:");
            discounts.forEach(System.out::println);
        }
        promptReturn();
    }

    // ==========================
    // Gift Card Management
    // ==========================

    /**
     * Manages gift card-related operations.
     */
    private void manageGiftCards() {
        clearConsole();
        while (true) {
            System.out.println("\nGift Card Management:");
            System.out.println("1. Sell New Gift Card");
            System.out.println("2. Redeem Gift Card");
            System.out.println("3. View Gift Card Details");
            System.out.println("4. Back to Manager Menu");
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
                    viewGiftCardDetailsMenu();
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Sells a new gift card based on user input.
     */
    private void sellGiftCardMenu() {
        clearConsole();
        System.out.print("\nEnter the amount for the new gift card: ");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid amount format. Please enter a valid number.");
            return;
        }

        System.out.print("Enter customer information (optional): ");
        String customerInfo = scanner.nextLine().trim();

        String result = giftCardController.sellGiftCard(amount, customerInfo);
        System.out.println(result);
    }

    /**
     * Redeems an existing gift card based on user input.
     */
    private void redeemGiftCardMenu() {
        System.out.print("\nEnter the gift card code: ");
        String code = scanner.nextLine().trim();

        System.out.print("Enter the amount to redeem: ");
        double amount = promptForDouble("", 0.01, Double.MAX_VALUE);

        String result = giftCardController.redeemGiftCard(code, amount);
        System.out.println(result);
    }

    /**
     * Views details of a specific gift card.
     */
    private void viewGiftCardDetailsMenu() {
        clearConsole();
        System.out.print("\nEnter the gift card code to view details: ");
        String code = scanner.nextLine().trim();

        String details = giftCardController.viewGiftCardDetails(code);
        System.out.println(details);
    }

    // ==========================
    // Inventory Management
    // ==========================

    /**
     * Manages inventory-related operations.
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
     * Sends an item to another store based on user input.
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

    /**
     * Generates a low stock request by invoking the Inventory's method.
     */
    private void generateLowStockRequest() {
        clearConsole();
        inventory.generateLowStockRequest();
        promptReturn();
    }

    // ==========================
    // Pricing Management
    // ==========================

/**
 * Manager Functionality: Adjust Item Price
 */
private void adjustPriceMenu() {
    clearConsole();
    while (true) {
        System.out.println("\nAdjust Item Price Menu:");
        System.out.println("1. Search by Name");
        System.out.println("2. Search by Department");
        System.out.println("3. Search by Store ID");
        System.out.println("4. Back to Manager Menu");
        System.out.print("Enter your choice: ");

        String searchChoice = scanner.nextLine().trim();
        String criteria = null;

        switch (searchChoice) {
            case "1":
                criteria = "name";
                break;
            case "2":
                criteria = "department";
                break;
            case "3":
                criteria = "storeid";
                break;
            case "4":
                System.out.println("Returning to Manager Menu.");
                clearConsole();
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
                clearConsole();
                continue;
        }

        System.out.print("Enter the search value: ");
        String value = scanner.nextLine().trim();

        List<Item> foundItems = pricingController.searchItems(criteria, value);

        if (foundItems.isEmpty()) {
            clearConsole();
            System.out.println("No items found with the specified criteria. Please try another search.");
            continue;
        }

        System.out.println("\nFound Items:");
        for (int i = 0; i < foundItems.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, foundItems.get(i));
        }

        System.out.print("Enter the number of the item you want to adjust (or 0 to return to search menu): ");
        int itemIndex;
        try {
            itemIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (itemIndex == -1) continue; // Return to search menu
            if (itemIndex < 0 || itemIndex >= foundItems.size()) {
                clearConsole();
                System.out.println("Invalid selection. Returning to search menu.");
                continue;
            }
        } catch (NumberFormatException e) {
            clearConsole();
            System.out.println("Error: Please enter a valid number.");
            continue;
        }

        Item selectedItem = foundItems.get(itemIndex);
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

        String result = pricingController.adjustPrice(selectedItem, newPrice);
        clearConsole();
        System.out.println(result);
        break; // Break the loop after adjusting the price
    }
}

    // ==========================
    // Shipping Order Management
    // ==========================

    /**
     * Manages shipping order-related operations.
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
     * Displays all shipping orders.
     */
    private void viewAllShippingOrders() {
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

    /**
     * Processes and sends a selected shipment.
     */
    private void processAndSendShipment() {
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
            int orderNumber = promptForInteger("", 1, confirmedOrders.size());

            ShippingOrder selectedOrder = confirmedOrders.get(orderNumber - 1);
            System.out.println("You selected Order ID: " + selectedOrder.getOrderId());

            Shipper shipper = new Shipper("Ben Jackson", 1, true, null, shippingController);
            shipper.shipOrder(selectedOrder, inventory);
            System.out.println("Order shipped successfully.");
        }
    }

    // ==========================
    // Shift Management
    // ==========================

    /**
     * Manages employee work shifts.
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
     * Adds a new work shift based on user input.
     */
    private void addWorkShift() {
        try {
            System.out.print("Enter the date of shift (DD): ");
            int date = Integer.parseInt(scanner.nextLine().trim());

            String startTime = promptForTime("Enter the start time of the shift (HH:mm): ");
            String endTime = promptForTime("Enter the end time of the shift (HH:mm): ");

            Shift shift = new Shift(date, startTime, endTime);
            employee.getWorkSchedule().addShift(shift);

            System.out.println("Shift added: " + startTime + " to " + endTime);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter numeric values for the date.");
        }
    }

    /**
     * Removes an existing work shift based on user input.
     */
    private void removeWorkShift() {
        try {
            System.out.print("Enter the date of shift to remove (DD): ");
            int dateToRemove = Integer.parseInt(scanner.nextLine().trim());

            String startTimeToRemove = promptForTime("Enter the start time of the shift to remove (HH:mm): ");
            String endTimeToRemove = promptForTime("Enter the end time of the shift to remove (HH:mm): ");

            Shift shift = new Shift(dateToRemove, startTimeToRemove, endTimeToRemove);
            employee.getWorkSchedule().deleteShift(shift);

            System.out.println("Shift removed with date: " + dateToRemove + ", start time: " + startTimeToRemove + " and end time: " + endTimeToRemove);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter numeric values for the date.");
        }
    }

    // ==========================
    // Supplier Coordination
    // ==========================

    /**
     * Coordinates supplier-related operations.
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
     * Views all registered suppliers.
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
     * Registers a new supplier based on user input.
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
     * Updates information of an existing supplier based on user input.
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
     * Places a new supplier order based on user input.
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
     * Views all supplier orders.
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

    /**
     * Prompts the user to press Enter to return to the menu.
     */
    private void promptReturn() {
        System.out.println("\nPress Enter to return to the menu...");
        scanner.nextLine();
    }
}
