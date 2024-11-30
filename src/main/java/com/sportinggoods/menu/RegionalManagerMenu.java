package com.sportinggoods.menu;

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
        var stores = initManager.getRegionalManager().getStores(); // Use getRegionalManager
        if (stores.isEmpty()) {
            System.out.println("No stores available.");
        } else {
            for (Store store : stores) {
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
        System.out.print("Enter the new store address: ");
        String address = scanner.nextLine().trim();
        initManager.getRegionalManager().addStore(address);
        System.out.println("Store added successfully.");
        pause();
    }

    /**
     * Deletes an existing store.
     */
    private void deleteStore() {
        clearConsole();
        System.out.print("Enter the store address to delete: ");
        String address = scanner.nextLine().trim();
        initManager.getRegionalManager().removeStore(address);
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
            var stores = initManager.getRegionalManager().getStores();
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
