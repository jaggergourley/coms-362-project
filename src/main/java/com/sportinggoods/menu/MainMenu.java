package com.sportinggoods.menu;

import com.sportinggoods.commands.MenuInvoker;
import com.sportinggoods.util.InitializationManager;

/**
 * Represents the main menu of the Sporting Goods Management System,
 * allowing users to select their role.
 */
public class MainMenu {
    private MenuInvoker invoker;
    private InitializationManager initManager;

    public MainMenu(InitializationManager initManager) {
        this.invoker = new MenuInvoker();
        this.initManager = initManager;
        registerCommands();
    }

    /**
     * Registers the main menu commands with their corresponding actions.
     */
    private void registerCommands() {
        invoker.register("1", this::managerMenu);
        invoker.register("2", this::cashierMenu);
        invoker.register("3", this::employeeMenu);
        invoker.register("4", this::customerMenu);
        invoker.register("5", this::exitSystem);
    }

    /**
     * Displays the main menu and handles user input.
     */
    public void display() {
        while (true) {
            System.out.println("\n=== Sporting Goods Management System ===");
            System.out.println("Please select your role:");
            System.out.println("1. Manager");
            System.out.println("2. Cashier");
            System.out.println("3. Employee");
            System.out.println("4. Customer");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            String choice = initManager.getScanner().nextLine().trim();
            invoker.executeCommand(choice);
        }
    }

    /**
     * Launches the Manager menu.
     */
    private void managerMenu() {
        ManagerMenu managerMenu = new ManagerMenu(initManager);
        managerMenu.display();
    }

    /**
     * Launches the Cashier menu.
     */
    private void cashierMenu() {
        CashierMenu cashierMenu = new CashierMenu(initManager);
        cashierMenu.display();
    }

    /**
     * Launches the Employee menu.
     */
    private void employeeMenu() {
        EmployeeMenu employeeMenu = new EmployeeMenu(initManager);
        employeeMenu.display();
    }

    /**
     * Launches the Customer menu.
     */
    private void customerMenu() {
        CustomerMenu customerMenu = new CustomerMenu(initManager);
        customerMenu.display();
    }

    /**
     * Exits the system gracefully.
     */
    private void exitSystem() {
        System.out.println("Exiting the system. Goodbye!");
        initManager.shutdown(); // Close Scanner
        System.exit(0);
    }
}
