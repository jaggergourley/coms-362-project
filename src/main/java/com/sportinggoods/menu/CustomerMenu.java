package com.sportinggoods.menu;

import com.sportinggoods.controller.*;
import com.sportinggoods.model.*;
import com.sportinggoods.repository.*;
import com.sportinggoods.util.InitializationManager;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents the Customer Menu in the Sporting Goods Management System.
 * Allows customers to perform various shopping-related tasks.
 */
public class CustomerMenu extends BaseMenu {

    // Controllers
    private CashierController cashierController;
    private ShippingController shippingController;
    private FeedbackController feedbackController;

    // Repositories and Models
    private Inventory inventory;
    private ReceiptRepository receiptRepo;
    private static String appliedCouponCode = null;
    private Employee employee;
    private FeedbackRepository feedbackRepository;
    private PickupOrderRepository orderRepository;

    /**
     * Constructs a CustomerMenu with the provided InitializationManager and Scanner.
     *
     * @param initManager The InitializationManager instance for dependency injection.
     * @param scanner     The shared Scanner instance for user input.
     */
    public CustomerMenu(InitializationManager initManager, Scanner scanner, int storeId) {
        super(initManager, scanner);
        // Initialize controllers and repositories
        this.cashierController = new CashierController(
                initManager.getCashier(), initManager.getInventory(storeId), initManager.getRegisterController(),
                initManager.getReceiptRepo(), initManager.getCouponRepo()
        );
        this.shippingController = initManager.getShippingController();
        this.inventory = initManager.getInventory(storeId);
        this.receiptRepo = initManager.getReceiptRepo();
        this.feedbackController = initManager.getFeedbackController();
        this.orderRepository = initManager.getPickupOrderRepository();
    }

    @Override
    protected void registerCommands() {
        invoker.register("1", this::purchaseItemAsCustomer);
        invoker.register("2", this::returnItemAsCustomer);
        invoker.register("3", this::makeShippingOrderInput);
        invoker.register("4", this::placePickupOrder); // Placing a pickup order
        invoker.register("5", this::orderPickup);
        invoker.register("6", this::provideFeedback);
    }

    @Override
    protected void printMenuOptions() {
        clearConsole();
        System.out.println("\nCustomer Menu:");
        System.out.println("1. Shop for Items");
        System.out.println("2. Return Items");
        System.out.println("3. Place Shipping Order");
        System.out.println("4. Place Order for Pickup");
        System.out.println("5. Pickup Order");
        System.out.println("6. Provide Feedback");
        System.out.println("7. Back to Main Menu");
    }

    @Override
    protected boolean isExitChoice(String choice) {
        return choice.equals("7");
    }

    @Override
    protected void handleExit() {
        System.out.println("Returning to Main Menu...");
    }

    // ==========================
    // Shopping Operations
    // ==========================

    /**
     * Initiates the purchase process for a customer.
     */
    private void purchaseItemAsCustomer() {
        System.out.println("\nPurchasing items as a customer.");
        processSale();
    }

    /**
     * Handles the return process for a customer.
     */
    private void returnItemAsCustomer() {
        System.out.println("\nReturning items as a customer.");
        handleReturn();
    }

    /**
     * Initiates the shipping order placement for a customer.
     */
    private void makeShippingOrderInput() {
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
            List<Item> availableItems = new ArrayList<>(inventory.getItems());
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
     * Processes the sale of items.
     */
    private void processSale() {
        clearConsole();
        Customer customer = getCustomerDetails();
        Map<Item, Integer> itemsToBuy = new HashMap<>();
        String couponCode = appliedCouponCode;
        double discount = 0.0;

        while (true) {
            List<Item> availableItems = inventory.getItems().stream()
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
            clearConsole();
            System.out.println(quantity + " of " + selectedItem.getName() + " added to cart.");
        }

        // Calculate total cost
        double totalCost = itemsToBuy.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue())
                .sum();

        System.out.println("Total before discounts: $" + totalCost);

        // Coupon handling
        if (couponCode != null && !couponCode.isEmpty()) {
            discount = cashierController.previewCoupon(couponCode);
            if (discount > 0) {
                if (cashierController.isPercentageCoupon(couponCode)) {
                    System.out.printf("Coupon applied: %.2f%% off\n", discount);
                    discount = totalCost * (discount / 100);
                } else {
                    System.out.printf("Coupon applied: $%.2f off\n", discount);
                }
                System.out.printf("Total after coupon: $%.2f\n", totalCost - discount);
            } else {
                System.out.println("Previously applied coupon is invalid. Proceeding without a discount.");
                couponCode = ""; // Reset invalid coupon code
                discount = 0.0;
            }
        } else {
            System.out.print("Do you have a coupon code to apply? (yes/no): ");
            String hasCoupon = scanner.nextLine().trim();

            if (hasCoupon.equalsIgnoreCase("yes")) {
                System.out.print("Enter the coupon code: ");
                couponCode = scanner.nextLine().trim();

                discount = cashierController.previewCoupon(couponCode);
                if (discount > 0) {
                    if (cashierController.isPercentageCoupon(couponCode)) {
                        System.out.printf("Coupon applied: %.2f%% off\n", discount);
                        discount = totalCost * (discount / 100);
                    } else {
                        System.out.printf("Coupon applied: $%.2f off\n", discount);
                    }
                    totalCost -= discount;
                    System.out.printf("Total after coupon: $%.2f\n", totalCost);
                    appliedCouponCode = couponCode; // Save the applied coupon
                } else {
                    System.out.println("Invalid coupon. Proceeding without applying a discount.");
                    couponCode = ""; // Reset invalid coupon code
                    discount = 0.0;
                }
            }
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

            Receipt receipt = cashierController.processSale(customer, itemsToBuy, paymentMethod, couponCode);
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
     * Handles the return of items by a customer.
     */
    private void handleReturn() {
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

    // ==========================
    // Helper Methods
    // ==========================

    /**
     * Retrieves customer details, defaulting to "Guest" if no ID is provided.
     *
     * @return A Customer object representing the current customer.
     */
    private Customer getCustomerDetails() {
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
     * Displays available items to the user.
     *
     * @param availableItems List of available items.
     */
    private void displayAvailableItems(List<Item> availableItems) {
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
    private int promptForQuantity(Item selectedItem) {
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
                    System.out.printf("Input must be between %d and %d.%n", min, max);
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
                    System.out.printf("Input must be between %.2f and %.2f.%n", min, max);
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number or type 'cancel' to abort.");
            }
        }
    }

    private void orderPickup() {
        clearConsole();
        System.out.println("Pick Up an Existing Order:");

        // Step 1: Get confirmation details from the customer
        System.out.print("Enter Order Confirmation Number: ");
        String confirmationDetails = scanner.nextLine().trim();

        // Step 2: Fetch the order from the repository
        Optional<PickupOrder> optionalOrder = orderRepository.getOrderByConfirmation(confirmationDetails);
        if (optionalOrder.isEmpty()) {
            System.out.println("No order found with the provided confirmation number.");
            return;
        }

        PickupOrder order = optionalOrder.get();

        // Step 3: Generate receipt and complete the pickup
        Receipt receipt = cashierController.handleOrderPickup(
                new Customer(order.getCustomerName(), -1),
                order.getItems(),
                confirmationDetails
        );

        if (receipt != null) {
            orderRepository.updateOrderStatus(order.getOrderId(), "Completed");
            System.out.println("Order pickup completed successfully. Receipt Details:");
            System.out.println(receipt);
        } else {
            System.out.println("Order pickup failed. Please verify the details and try again.");
        }
    }

    private void placePickupOrder() {
        clearConsole();
        System.out.println("Place Pickup Order:");

        // Step 1: Collect customer name
        System.out.print("Enter Customer Name: ");
        String customerName = scanner.nextLine().trim();

        // Step 2: Collect items for the pickup order
        Map<Item, Integer> itemsToPickup = new HashMap<>();
        while (true) {
            System.out.print("Enter item name for pickup (or type 'done' to finish): ");
            String itemName = scanner.nextLine().trim();

            if (itemName.equalsIgnoreCase("done")) {
                break;
            }

            System.out.print("Enter quantity for pickup: ");
            String quantityInput = scanner.nextLine().trim();

            if (!quantityInput.matches("\\d+")) {
                System.out.println("Invalid quantity. Please enter a valid number.");
                continue;
            }

            int quantity = Integer.parseInt(quantityInput);
            Item item = inventory.getItem(itemName);

            if (item == null) {
                System.out.println("Item not found in inventory. Please try again.");
                continue;
            }

            itemsToPickup.put(item, quantity);
        }

        if (itemsToPickup.isEmpty()) {
            System.out.println("No items added to the order. Aborting.");
            return;
        }

        // Step 3: Create the pickup order
        PickupOrder order = new PickupOrder(customerName, itemsToPickup, LocalDate.now());

        PickupOrder confirmedOrder = orderRepository.addOrder(order);
        if (confirmedOrder != null) {
            System.out.println("Pickup order created successfully!");
            System.out.println("Confirmation Number: " + confirmedOrder.getConfirmationDetails());
            System.out.println("Order Date: " + confirmedOrder.getDate());
            System.out.println("Order Status: " + confirmedOrder.getStatus());
        } else {
            System.out.println("Failed to create the pickup order. Please try again.");
        }
    }

    private void provideFeedback() {
        clearConsole();
        System.out.println("Provide Feedback:");

        Customer customer = getCustomerDetails();

        System.out.print("Enter your feedback: ");
        String feedbackContent = scanner.nextLine().trim();

        System.out.print("Do you want this feedback to be escalated? (yes/no): ");
        boolean escalate = scanner.nextLine().trim().equalsIgnoreCase("yes");


        feedbackController.handleCustomerFeedback(customer.getCustomerId(), feedbackContent, escalate);
        System.out.println("Thank you for your feedback! It has been recorded.");
    }
}
