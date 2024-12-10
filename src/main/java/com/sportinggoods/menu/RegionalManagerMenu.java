package com.sportinggoods.menu;

import com.sportinggoods.model.Inventory;
import com.sportinggoods.model.Item;
import com.sportinggoods.model.Store;
import com.sportinggoods.model.Utility;
import com.sportinggoods.util.InitializationManager;

import java.time.LocalDateTime;
import java.util.List;
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
        Store s = new Store(id, address);
        initManager.getRegionalManager().getStoreList().addStore(s);
        initializeBaseInventory(id);
        initManager.getUtilityRepo().addUtility(new Utility("U001", id, "HVAC", "Active", 750.5, LocalDateTime.now(), "09:00-21:00"));
        initManager.getUtilityRepo().addUtility(new Utility("U002", id, "Lighting", "Active", 450.2, LocalDateTime.now(), "09:00-21:00"));
        initManager.getUtilityRepo().addUtility(new Utility("U003", id, "Water", "Active", 150.0, LocalDateTime.now(), "24/7"));
        System.out.println("Store added successfully.");
        pause();
    }

    private void initializeBaseInventory(int storeId) {
        Inventory inventory = new Inventory(storeId); // Create an inventory for the new store

        // Define the base inventory items
        List<Item> baseItems = List.of(
                new Item("Tennis Racket", 100.0, "Sports", 10, storeId),
                new Item("Baseball Glove", 45.0, "Sports", 10, storeId),
                new Item("Jump Rope", 10.0, "Fitness", 10, storeId),
                new Item("Resistance Band", 15.0, "Fitness", 10, storeId),
                new Item("Yoga Mat", 30.0, "Fitness", 10, storeId),
                new Item("Dumbbells", 60.0, "Fitness", 10, storeId),
                new Item("Hiking Backpack", 80.0, "Outdoor", 10, storeId),
                new Item("Golf Club", 150.0, "Sports", 10, storeId),
                new Item("Basketball", 25.0, "Sports", 10, storeId),
                new Item("Soccer Ball", 20.0, "Sports", 10, storeId)
        );

        // Add base items to the inventory
        for (Item item : baseItems) {
            inventory.addItem(item);
        }

        System.out.println("Base inventory initialized for Store ID: " + storeId);
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
