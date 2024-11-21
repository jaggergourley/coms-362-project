package com.sportinggoods.menu;

import com.sportinggoods.controller.*;
import com.sportinggoods.model.*;
import com.sportinggoods.repository.*;
import com.sportinggoods.util.InitializationManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Represents the Cashier Menu in the Sporting Goods Management System.
 * Handles cashier-specific actions such as processing sales, handling returns, managing gift cards, and applying coupons.
 */
public class CashierMenu extends BaseMenu {

    // Controllers
    private CashierController cashierController;
    private GiftCardController giftCardController;
    private RegisterController registerController;
    private DiscountController discountController;

    // Repositories
    private CouponRepository couponRepository;
    private ReceiptRepository receiptRepository;
    private DiscountRepository discountRepository;

    // Models
    private Inventory inventory;

    // State
    private String appliedCouponCode = null;

    /**
     * Constructs a CashierMenu with the provided InitializationManager and Scanner.
     *
     * @param initManager The InitializationManager instance for dependency injection.
     * @param scanner     The shared Scanner instance for user input.
     */
    public CashierMenu(InitializationManager initManager, Scanner scanner) {
        super(initManager, scanner);
        // Initialize controllers and repositories
        this.cashierController = initManager.getCashierController();
        this.giftCardController = initManager.getGiftCardController();
        this.registerController = initManager.getRegisterController();
        this.couponRepository = initManager.getCouponRepo();
        this.receiptRepository = initManager.getReceiptRepo();
        this.inventory = initManager.getInventory();
        this.discountController = initManager.getDiscountController();
        this.discountRepository = initManager.getDiscountRepo();
    }

    @Override
    protected void registerCommands() {
        invoker.register("1", this::processSale);
        invoker.register("2", this::handleReturn);
        invoker.register("3", this::sellGiftCardMenu);
        invoker.register("4", this::redeemGiftCardMenu);
        invoker.register("5", this::applyCouponMenu);
    }

    @Override
    protected void printMenuOptions() {
        clearConsole();
        System.out.println("\nCashier Menu:");
        System.out.println("1. Process Sale");
        System.out.println("2. Handle Return");
        System.out.println("3. Sell Gift Card");
        System.out.println("4. Redeem Gift Card");
        System.out.println("5. Apply Coupon");
        System.out.println("6. Back to Main Menu");
    }

    @Override
    protected boolean isExitChoice(String choice) {
        return choice.equals("6");
    }

    @Override
    protected void handleExit() {
        System.out.println("Returning to Main Menu...");
    }

    // ==========================
    // Sales Operations
    // ==========================

    /**
     * Processes a sale transaction.
     */
    private void processSale() {
        clearConsole();
        Customer customer = getCustomerDetails();
        Map<Item, Integer> itemsToBuy = new HashMap<>();
        String couponCode = appliedCouponCode;
        double discount = 0.0;
    
        while (true) {
            List<Item> availableItems = inventory.getItems().values().stream()
                    .filter(item -> item.getStoreID() == 1) // Assuming store ID 1
                    .collect(Collectors.toList());
    
            if (availableItems.isEmpty()) {
                System.out.println("No items are available for sale at the moment.");
                return;
            }
    
            displayAvailableItemsWithDiscounts(availableItems);
            System.out.print("Enter the number of the item to add to the cart (or type 'checkout' to finish): ");
            String input = scanner.nextLine().trim();
    
            if (input.equalsIgnoreCase("checkout")) {
                break;
            }
    
            int itemChoice;
            try {
                itemChoice = Integer.parseInt(input) - 1;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number or 'checkout'.");
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
                .mapToDouble(entry -> inventory.getEffectivePrice(entry.getKey().getName(), discountRepository) * entry.getValue())
                .sum();
    
        System.out.println("Total before coupons: $" + totalCost);
    
        // Handle coupon logic (unchanged)
        handleCouponLogic(totalCost, couponCode);
    
        // Proceed to checkout
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
     * Displays available items along with discount information.
     */
    private void displayAvailableItemsWithDiscounts(List<Item> availableItems) {
        System.out.println("\nAvailable Items:");
        for (int i = 0; i < availableItems.size(); i++) {
            Item item = availableItems.get(i);
            double originalPrice = item.getPrice();
            double discountedPrice = inventory.getEffectivePrice(item.getName(), discountRepository);
    
            if (discountedPrice < originalPrice) {
                double discountValue = originalPrice - discountedPrice;
                String discountType = discountValue == originalPrice * (discountValue / 100) ? "percentage" : "fixed";
                String discountDisplay = discountType.equals("percentage")
                        ? String.format("(-%.2f%%)", (discountValue / originalPrice) * 100)
                        : String.format("(-$%.2f)", discountValue);
    
                System.out.printf("%d. %s (Price: $%.2f %s, Quantity: %d)\n",
                        i + 1, item.getName(), discountedPrice, discountDisplay, item.getQuantity());
            } else {
                System.out.printf("%d. %s (Price: $%.2f, Quantity: %d)\n",
                        i + 1, item.getName(), originalPrice, item.getQuantity());
            }
        }
    }
    
    /**
     * Handles coupon logic for the sale.
     */
    private void handleCouponLogic(double totalCost, String couponCode) {
        double discount = 0.0;
    
        if (couponCode != null && !couponCode.isEmpty()) {
            discount = cashierController.previewCoupon(couponCode);
            if (discount > 0) {
                if (cashierController.isPercentageCoupon(couponCode)) {
                    System.out.printf("Coupon applied: %.2f%% off%n", discount);
                    discount = totalCost * (discount / 100);
                } else {
                    System.out.printf("Coupon applied: $%.2f off%n", discount);
                }
                System.out.printf("Total after coupon: $%.2f%n", totalCost - discount);
            } else {
                System.out.println("Previously applied coupon is invalid. Proceeding without a discount.");
                appliedCouponCode = ""; // Reset invalid coupon code
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
                        System.out.printf("Coupon applied: %.2f%% off%n", discount);
                        discount = totalCost * (discount / 100);
                    } else {
                        System.out.printf("Coupon applied: $%.2f off%n", discount);
                    }
                    totalCost -= discount;
                    System.out.printf("Total after coupon: $%.2f%n", totalCost);
                    appliedCouponCode = couponCode; // Save the applied coupon
                } else {
                    System.out.println("Invalid coupon. Proceeding without applying a discount.");
                    appliedCouponCode = ""; // Reset invalid coupon code
                }
            }
        }
    }
    /**
     * Handles the return of items by a customer.
     */
    private void handleReturn() {
        Customer customer = getCustomerDetails();
        Map<Item, Integer> itemsToReturn = new HashMap<>();

        while (true) {
            System.out.print("Enter item name (or type 'done' to finish): ");
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
            if (!receiptRepository.hasReceiptForReturn(customer.getCustomerId(), itemName, quantity)) {
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
        clearConsole();
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
     * Applies a coupon code to the current sale.
     */
    private void applyCouponMenu() {
        clearConsole();
        while (true) {
            System.out.println("\nCoupon Menu:");
            System.out.println("1. Enter Coupon Code");
            System.out.println("2. Back to Cashier Menu");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    System.out.print("\nEnter the coupon code: ");
                    String couponCode = scanner.nextLine().trim();
                    double discount = cashierController.previewCoupon(couponCode);

                    if (discount > 0) {
                        appliedCouponCode = couponCode; // Set the applied coupon
                        System.out.println("Coupon is valid. Discount: " +
                                (cashierController.isPercentageCoupon(couponCode) ? discount + "%" : "$" + discount));
                        return; // Exit the menu after applying a coupon
                    } else {
                        System.out.println("Invalid coupon. Please check the code and try again.");
                    }
                    break;
                case "2":
                    return; // Return to Cashier Menu
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
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
            System.out.printf("%d. %s (Price: $%.2f, Quantity: %d)%n",
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
}