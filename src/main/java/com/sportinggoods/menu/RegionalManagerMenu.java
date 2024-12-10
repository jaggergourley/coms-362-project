package com.sportinggoods.menu;

import com.sportinggoods.model.Employee;
import com.sportinggoods.model.EmployeeList;
import com.sportinggoods.model.Inventory;
import com.sportinggoods.model.Item;
import com.sportinggoods.model.Schedule;
import com.sportinggoods.model.Store;
import com.sportinggoods.util.InitializationManager;

import java.util.Scanner;

/**
 * Regional Manager Menu - Allows adding, deleting, and selecting stores.
 */
public class RegionalManagerMenu extends BaseMenu {

    private int currentStoreId;

    public RegionalManagerMenu(InitializationManager initManager, Scanner scanner) {
        super(initManager, scanner);
    }

    @Override
    protected void registerCommands() {
        invoker.register("1", this::viewStores);
        invoker.register("2", this::addStore);
        invoker.register("3", this::deleteStore);
        invoker.register("4", this::selectStore);
    }

    @Override
    protected void printMenuOptions() {
        clearConsole();
        System.out.println("\n=== Regional Manager Menu ===");
        System.out.println("1. View Stores");
        System.out.println("2. Add Store");
        System.out.println("3. Delete Store");
        System.out.println("4. Select Store");
        System.out.println("5. Exit");
    }

    @Override
    protected boolean isExitChoice(String choice) {
        return choice.equals("5");
    }

    @Override
    protected void handleExit() {
        System.out.println("Exiting system. Goodbye!");
        initManager.shutdown();
        System.exit(0);
    }

    /**
     * Displays the list of stores.
     */
    private void viewStores() {
        clearConsole();
        var stores = initManager.getRegionalManager().getStoreList(); // Use getRegionalManager
        if (stores.getStores().isEmpty()) {
            System.out.println("No stores available.");
        } else {
            for (Store store : stores.getStores()) {
                System.out.println("Store ID: " + store.getID() + ", Address: " + store.getAddress());
            }
        }
        pause();
    }

    /**
     * Adds a new store.
     */
    private void addStore() {
        clearConsole();
        initManager.getRegionalManager().getStoreList().printStores();
        System.out.print("Enter the new store address: ");
        String address = scanner.nextLine().trim();
        System.out.print("Enter the new store id: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume the newline left-over from nextInt()

        // Create the store
        Store s = new Store(id, address);

        // Add items to the store
        while (true) {
            clearConsole();
            Inventory i = s.getStoreInventory();
            System.out.println("Add an item to the store (or type 'exit' to finish): ");
            System.out.print("Enter item name: ");
            String itemName = scanner.nextLine().trim();
            if (itemName.equalsIgnoreCase("exit"))
                break;

            System.out.print("Enter item quantity: ");
            int quantity = scanner.nextInt();
            System.out.print("Enter item price: ");
            double price = scanner.nextDouble();
            System.out.print("Enter item department: ");
            String department = scanner.nextLine();

            Item temp = new Item(itemName, price, department, quantity, id);
            i.addItem(temp);

            scanner.nextLine(); // Consume the newline
        }

        // Add employees to the store
        while (true) {
            clearConsole();
            EmployeeList temp = s.getEmployees();
            System.out.println("Add an employee to the store (or type 'exit' to finish): ");
            System.out.print("Enter employee Name or exit: ");
            String employeeName = scanner.nextLine();
            if (employeeName.equalsIgnoreCase("exit"))
                break;

            System.out.print("Enter employee ID: ");
            int employeeId = scanner.nextInt();
            Employee e = new Employee(employeeName, employeeId, new Schedule(), id);
            temp.addEmployee(e);

        }

        // Add the store to the store list
        initManager.getRegionalManager().getStoreList().addStore(s);
        System.out.println("Store added successfully.");
        pause();
    }

    /**
     * Deletes an existing store.
     */
    private void deleteStore() {
        clearConsole();
        var list = initManager.getRegionalManager().getStoreList();
        list.printStores();
        System.out.print("Enter the store address to delete: ");
        String address = scanner.nextLine().trim();
        System.out.print("Enter the store id: ");
        int id = scanner.nextInt();
        Store s = new Store(id, address);

        EmployeeList temp = s.getEmployees();
        for (int i = 0; i < temp.getEmployees().size(); i++) {
            temp.getEmployees().remove(i);
        }

        Inventory inventory = s.getStoreInventory();
        for (int i = 0; i < inventory.getItems().size(); i++) {
            inventory.getItems().get(i).setQuantity(0);
        }

        list.removeStore(s);
        System.out.println("Store deleted successfully.");
        pause();
    }

    /**
     * Selects a store to enter its menu system.
     */
    private void selectStore() {
        clearConsole();
        System.out.print("Enter the Store ID to select: ");
        String storeIdInput = scanner.nextLine().trim();
        try {
            int storeId = Integer.parseInt(storeIdInput);
            var stores = initManager.getRegionalManager().getStoreList().getStores();
            Store selectedStore = stores.stream()
                    .filter(store -> store.getID() == storeId)
                    .findFirst()
                    .orElse(null);

            if (selectedStore == null) {
                System.out.println("Invalid Store ID. Please try again.");
                pause();
                return;
            }

            // Store context is set; load lower menus
            System.out.println("Entering store: " + selectedStore.getAddress());
            currentStoreId = storeId;

            // Pass the store context to lower menus
            new MainMenu(initManager, currentStoreId).display();
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a numeric Store ID.");
            pause();
        }
    }

    /**
     * Pauses for user input.
     */
    private void pause() {
        System.out.println("Press Enter to continue...");
        initManager.getScanner().nextLine();
    }
}
