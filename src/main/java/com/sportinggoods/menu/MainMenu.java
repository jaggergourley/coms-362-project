package com.sportinggoods.menu;

import com.sportinggoods.util.InitializationManager;

/**
 * Main menu for selecting user roles.
 */
public class MainMenu {
    private InitializationManager initManager;
    private int storeId;

    /**
     * Initializes MainMenu with dependencies.
     *
     * @param initManager Dependency injection manager.
     */
    public MainMenu(InitializationManager initManager, int storeId) {
        this.initManager = initManager;
        this.storeId = storeId;
    }

    /**
     * Displays the main menu and handles input.
     */
    public void display() {
        while (true) {
            clearConsole();
            System.out.println("\n=== Sporting Goods Management System (Store ID: " + storeId + ") ===");
            System.out.println("1. Manager");
            System.out.println("2. Cashier");
            System.out.println("3. Employee");
            System.out.println("4. Customer");
            System.out.println("5. Maintenance Staff");
            System.out.println("6. Back to Regional Manager Menu");
            System.out.print("Enter your choice: ");

            String choice = initManager.getScanner().nextLine().trim();

            switch (choice) {
                case "1":
                    new ManagerMenu(initManager, initManager.getScanner(), storeId).display();
                    break;
                case "2":
                    new CashierMenu(initManager, initManager.getScanner(), storeId).display();
                    break;
                case "3":
                    new EmployeeMenu(initManager, initManager.getScanner(), storeId).display();
                    break;
                case "4":
                    new CustomerMenu(initManager, initManager.getScanner(), storeId).display();
                    break;
                case "5":
                    new MaintenanceStaffMenu(initManager, initManager.getScanner(), storeId).display();
                    break;
                case "6":
                    return; // Exit to Regional Manager Menu
                default:
                    System.out.println("Invalid choice. Please try again.");
                    pause();
            }
        }
    }

    /**
     * Clears the console.
     */
    private void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) System.out.println(); // Fallback
        }
    }

    /**
     * Exits the system.
     */
    private void exitSystem() {
        System.out.println("Exiting the system. Goodbye!");
        initManager.shutdown();
        System.exit(0);
    }

    /**
     * Pauses for user input.
     */
    private void pause() {
        System.out.println("Press Enter to continue...");
        initManager.getScanner().nextLine();
    }
}
