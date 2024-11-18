package com.sportinggoods.menu;

import com.sportinggoods.commands.MenuInvoker;
import com.sportinggoods.controller.CashierController;
import com.sportinggoods.controller.ShippingController;
import com.sportinggoods.model.Employee;
import com.sportinggoods.model.Schedule;
import com.sportinggoods.model.ShippingOrder;
import com.sportinggoods.repository.ShippingOrderRepository;
import com.sportinggoods.util.InitializationManager;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class EmployeeMenu {
    private Scanner scanner;
    private MenuInvoker employeeInvoker;
    private InitializationManager initManager;

    // Controllers obtained from InitializationManager
    private ShippingController shippingController;
    private CashierController cashierController;

    public EmployeeMenu(InitializationManager initManager) {
        this.scanner = new Scanner(System.in);
        this.employeeInvoker = new MenuInvoker();
        this.initManager = initManager;

        // Initialize controllers from InitializationManager
        this.shippingController = initManager.getShippingController();
        this.cashierController = initManager.getCashierController();

        registerCommands();
    }

    private void registerCommands() {
        employeeInvoker.register("1", this::viewInventory);
        employeeInvoker.register("2", this::processAndSendShipment);
        employeeInvoker.register("3", this::backToMainMenu);
    }

    public void display() {
        while (true) {
            System.out.println("\nEmployee Menu:");
            System.out.println("1. View Inventory");
            System.out.println("2. Process Shipping Orders");
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();
            if (choice.equals("3")) {
                break; // Exit to Main Menu
            }
            employeeInvoker.executeCommand(choice);
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
     * Employee Functionality: View Inventory
     */
    private static void getInventory() {
        System.out.println("\nCurrent Inventory:");
        inventory.printInventory();
    }
}
