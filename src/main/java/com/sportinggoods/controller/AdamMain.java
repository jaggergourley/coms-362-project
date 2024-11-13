package com.sportinggoods.controller;

import com.sportinggoods.model.Inventory;
import com.sportinggoods.model.Item;
import com.sportinggoods.model.Shipper;
import com.sportinggoods.model.ShippingOrder;
import com.sportinggoods.repository.ShippingOrderRepository;
import com.sportinggoods.repository.SupplierOrderRepository;
import com.sportinggoods.repository.SupplierRepository;

import java.io.File;
import java.util.*;

public class AdamMain {

    private static Scanner scanner = new Scanner(System.in);
    private static ShippingOrderRepository shippingRepo;
    private static ShippingController shippingController;

    private static Inventory inventory;

    public static void main(String[] args) {
        shippingRepo = new ShippingOrderRepository();
        shippingController = new ShippingController(shippingRepo);

        fillInventory();

        getInventory();

        //makeShippingOrderInput();

        processAndSendShipment();
    }

    public static void fillInventory(){
        inventory = new Inventory();

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

    public static void getInventory(){

        inventory.printInventory();

    }


    public static void makeShippingOrderInput() {
        Scanner scanner = new Scanner(System.in);

        // Get customer details
        System.out.print("Enter customer first name: ");
        String customerFirstName = scanner.nextLine();

        System.out.print("Enter customer last name: ");
        String customerLastName = scanner.nextLine();

        System.out.print("Enter shipping address: ");
        String shippingAddress = scanner.nextLine();

        System.out.print("Enter customer email: ");
        String customerEmail = scanner.nextLine();

        System.out.print("Enter customer phone number: ");
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
            int orderQuantity = Integer.parseInt(scanner.nextLine().trim());


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
        System.out.println("Order created: " + orderCreated);

        // Retrieve and display the order details
        if (orderCreated) {

            System.out.println("Order Details:");
            System.out.println("Customer Name: " + customerFirstName + " " + customerLastName);
            System.out.println("Customer Address: " + shippingAddress);
            System.out.println("Customer Email: " + customerEmail);
            System.out.println("Customer Phone Number: " + customerPhoneNumber);
            System.out.println("Items Ordered:");

            for (Item orderedItem : items.keySet()) {
                int quantity = orderedItem.getQuantity();
                System.out.println("Item: " + orderedItem.getName() + ", Quantity: " + quantity);
            }

            // Format the total price to two decimal places
            System.out.printf("Total Price: %.2f\n", totalPrice);
            System.out.println("Order Status: Confirmed");
        }

        scanner.close();
    }


    public static void makeShippingOrder(){

        Map<Item, Integer> items = new HashMap<>();
        items.put(inventory.getItem("Tennis Racket"), 100);
        items.put(inventory.getItem("Tennis Ball"), 1);


        boolean orderCreated = shippingController.handleShippingOrder(
                "Randy", "Doe", items, 149.97, "123 Sport St", "john.doe@example.com", "555-1234"
        );
        System.out.println("Order created: " + orderCreated);

        //List<ShippingOrder> orders = shippingRepo.getAllShippingOrders();

//        System.out.println(orders.get(0).getItems());
//
//        for (Item item : items.keySet()) {
//            System.out.println("Single item: " + item + ", Quantity: " + items.get(item));
//            break; // Break after printing the first item (if you only want one)
//        }

    }


    public static void processAndSendShipment(){

        List<ShippingOrder> orders = shippingRepo.getAllShippingOrders();

        List<ShippingOrder> confirmedOrders = orders.stream()
                .filter(order -> "Confirmed".equalsIgnoreCase(order.getStatus()))
                .toList();

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
                int orderNumber = 0;
                try {
                    orderNumber = Integer.parseInt(scanner.nextLine().trim()) - 1;
                    if (orderNumber < 0 || orderNumber >= confirmedOrders.size()) {
                        System.out.println("Invalid selection. Please enter a number within the list range.");
                    } else {
                        ShippingOrder selectedOrder = confirmedOrders.get(orderNumber);
                        System.out.println("You selected Order ID: " + selectedOrder.getOrderId());

                        Shipper shipper = new Shipper("Ben Jackson", 1, true, null, shippingController);
                        shipper.shipOrder(selectedOrder, inventory);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }

        }
        scanner.close();


    }


}
