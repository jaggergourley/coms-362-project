package com.sportinggoods.model;

import com.sportinggoods.repository.DiscountRepository;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Manages inventory
public class Inventory {
    //private Map<String, Item> items = new HashMap<>();
    private ArrayList<Item> items = new ArrayList<>();
    private static final String FILE_PATH = "data/inventory.csv";
    private static final String LOW_STOCK_FILE = "data/lowStock.csv";
    private static final int LOW_STOCK_THRESHOLD = 5;
    private static final int RESTOCK_LEVEL = 10;

    //Constructor
    public Inventory(int storeID) {
        items = loadStoreItemsFromFile(storeID);
    }

    public ArrayList<Item> getItems(){
        return items;
    }

    //Getter
    public Item getItem(String itemName) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(itemName)) { // Case-insensitive comparison
                return item;
            }
        }
        return null; // Return null if the item is not found
    }

    /**
     * Updates current inventory with the change of item inputted.
     * @param itemName
     * @param quantityChange
     */
    // Updates the quantity of an item in the inventory
    /**
     public void updateQuantity(String itemName, int quantityChange) {
         Item item = items.get(itemName);
         if (item != null) {
             item.setQuantity(item.getQuantity() + quantityChange);
             saveItemsToFile(); // Save the updated inventory
         }
     } */

    /**
     * Updates the quantity of an item in the inventory. THIS IS FOR ARRAY LIST, solving merge conflict
     * If the item is not found, it does nothing.
     *
     * @param itemName       The name of the item to update.
     * @param quantityChange The change in quantity (can be positive or negative).
     */
    public void updateQuantity(String itemName, int quantityChange) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                item.setQuantity(item.getQuantity() + quantityChange);
                saveItemsToFile(); // Save the updated inventory
                return;
            }
        }
        System.out.println("Item not found: " + itemName);
    }



    /**
     * adds a item to the inventory by either updating
     * quantity or creating a new entry.
     * @param item
     */
    // public void addItem(Item item){
    //     Item temp = items.get(item.getName());
    //     if(temp != null){ //item is in the inventory
    //         int newQuantity = temp.getQuantity() + item.getQuantity();
    //         temp.setQuantity(newQuantity);
    //         saveItemsToFile();
    //     }
    //     else{ // item is not in the inventory
    //         items.put(item.getName(), item);
    //         saveItemsToFile();
    //     }
    // }

    public void addItem(Item item) {
        boolean itemFound = false;
        // Check if the item already exists in the list
        for (Item temp : items) {
            if (temp.getName().equalsIgnoreCase(item.getName()) && temp.getStoreID() == item.getStoreID()) {
                // Item is in the inventory, update quantity
                int newQuantity = temp.getQuantity() + item.getQuantity();
                temp.setQuantity(newQuantity);
                itemFound = true;
                break;
            }
        }
        if (!itemFound) {
            // Item is not in the inventory, add it
            items.add(item);
        }
        // Save updated list to file
        saveItemsToFile();
    }

    // public void addItems(ArrayList<Item> itemList){
    //     for(int i = 0; i < itemList.size(); i++){
    //         Item temp1 = itemList.get(i);
    //         Item temp2 = items.get(temp1.getName());
    //         if(temp2 != null){ //item exists in inventory
    //             int newQuantity = temp2.getQuantity() + temp1.getQuantity();
    //             temp2.setQuantity(newQuantity);
    //             saveItemsToFile();
    //         }
    //         else{
    //             items.put(temp1.getName(), temp1);
    //             saveItemsToFile();
    //         }
    //     }
    // }

    public void addItems(ArrayList<Item> newItems) {
        for (Item newItem : newItems) {
            boolean itemFound = false;

            // Check if the item already exists in the inventory
            for (Item existingItem : items) {
                if (existingItem.getName().equalsIgnoreCase(newItem.getName())) {
                    // Item is in the inventory, update quantity
                    int newQuantity = existingItem.getQuantity() + newItem.getQuantity();
                    existingItem.setQuantity(newQuantity);
                    itemFound = true;
                    break;
                }
            }

            if (!itemFound) {
                // Item is not in the inventory, add it
                items.add(newItem);
            }
        }

        // Save updated list to file
        saveItemsToFile();
    }

    // public List<Item> getAllItems() {
    //     return new ArrayList<>(items.values());
    // }

    public List<Item> getItemsByDepartment(String department) {
        List<Item> itemsInDepartment = new ArrayList<>();
        for (Item item : items) {
            if (item.getDepartment().equalsIgnoreCase(department)) {
                itemsInDepartment.add(item);
            }
        }
        return itemsInDepartment;
    }

    /**
     * remove the item from the list by decrementing
     * by given items qunatity
     * @param item
     */
    public void deleteItem(Item item){
        Item temp = getItem(item.getName());
        if(temp != null){ // item is in the inventory
            temp.setQuantity(temp.getQuantity() - item.getQuantity());
            if(temp.getQuantity() < 0){
                temp.setQuantity(0);
            }
            saveItemsToFile();
        }
        else{
            System.out.println("Item to remove not found");
        }
    }

    public void deleteItems(ArrayList<Item> itemList){
        for(int i = 0; i < itemList.size(); i++){
            Item temp1 = itemList.get(i);
            Item temp2 = getItem(temp1.getName());
            if(temp2 != null){ //item exists in inventory
                int newQuantity = temp2.getQuantity() - temp1.getQuantity();
                temp2.setQuantity(newQuantity);
                saveItemsToFile();
            }
            else{
                System.out.println("Item: " + temp1.getName() + " is not in the inventory");
            }
        }
    }

    public double getEffectivePrice(String itemName, DiscountRepository discountRepository) {
        Item item = getItem(itemName);
        if (item == null) {
            throw new IllegalArgumentException("Item not found: " + itemName);
        }
    
        // Check if there is an active discount for this item
        List<Discount> activeDiscounts = discountRepository.getDiscounts();
        for (Discount discount : activeDiscounts) {
            if (discount.getTarget().equalsIgnoreCase(itemName)) {
                double originalPrice = item.getPrice();
                return discount.getType().equalsIgnoreCase("PERCENTAGE") 
                       ? originalPrice * (1 - discount.getValue() / 100) 
                       : originalPrice - discount.getValue();
            }
        }
    
        // No active discount; return the original price
        return item.getPrice();
    }


    public void swapStore(String itemName, int newStoreID){
        Item temp = getItem(itemName);
        if(temp != null){
            temp.setStoreID(newStoreID);
            saveItemsToFile();
        }
        else{
            System.out.println("Item is not in the inventory");
        }
    }


    // Checks if an item is available in the required quantity
    public boolean checkAvailability(String itemName, int quantity) {
        Item item = getItem(itemName);
        return item != null && item.getQuantity() >= quantity;
    }


    // Loads items from a CSV file into the inventory map
    private ArrayList<Item> loadStoreItemsFromFile(int storeID) {
        ArrayList<Item> loadedItems = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                Item item = Item.fromCSV(line);
                if (item != null && item.getStoreID() == storeID) {
                    loadedItems.add(item);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading inventory from file: " + e.getMessage());
        }
        return loadedItems;
    }

    // Saves the current state of inventory to a CSV file
    // public void saveItemsToFile() {
    //     try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
    //         writer.write("name,price,department,quantity\n"); // Write CSV header
    //         for (Item item : items) {
    //             writer.write(item.toCSV());
    //             writer.newLine();
    //         }
    //     } catch (IOException e) {
    //         System.out.println("Error saving inventory to file: " + e.getMessage());
    //     }
    // }

    public void saveItemsToFile() {
    Map<String, Item> inventoryMap = new HashMap<>();

    // Read existing data from the file
    try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
        String line = reader.readLine(); // Skip header
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            String name = parts[0];
            double price = Double.parseDouble(parts[1]);
            String department = parts[2];
            int quantity = Integer.parseInt(parts[3]);
            int storeID = Integer.parseInt(parts[4]);

            inventoryMap.put(name, new Item(name, price, department, quantity, storeID));
        }
    } catch (IOException e) {
        System.out.println("Error reading inventory file: " + e.getMessage());
    }

    // Add current store's items to the map (merge quantities)
    for (Item item : items) {
        inventoryMap.merge(item.getName(), item, (existing, newItem) -> {
            existing.setQuantity(existing.getQuantity() + newItem.getQuantity());
            return existing;
        });
    }

    // Write merged inventory back to the file
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
        writer.write("name,price,department,quantity\n"); // Write CSV header
        for (Item item : inventoryMap.values()) {
            writer.write(item.toCSV());
            writer.newLine();
        }
    } catch (IOException e) {
        System.out.println("Error saving inventory to file: " + e.getMessage());
    }
}

    public void printInventory(){
        System.out.println("Inventory:");
        for(Item item : items){
            System.out.println(item);
        }
        System.out.println();
    }

    // ==========================
    // Low Stock Management
    // ==========================

    /**
     * Generates a low stock request by identifying items with stock below a predefined threshold.
     * Writes the low-stock items to 'lowStock.csv'.
     */
    public void generateLowStockRequest() {
        List<Item> lowStockItems = new ArrayList<>();

        // Identify items with stock below the threshold
        for (Item item : items) {
            if (item.getQuantity() < LOW_STOCK_THRESHOLD) {
                lowStockItems.add(item);
            }
        }

        if (lowStockItems.isEmpty()) {
            System.out.println("All items are sufficiently stocked. No low stock request generated.");
            return;
        }

        // Write low stock items to 'lowStock.csv'
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOW_STOCK_FILE))) {
            writer.write("name,price,department,quantity,storeID\n");
            for (Item item : lowStockItems) {
                writer.write(item.toCSV());
                writer.newLine();
            }
            System.out.println("Low stock request generated successfully. See 'lowStock.csv' for details.");
        } catch (IOException e) {
            System.out.println("Error writing low stock request to file: " + e.getMessage());
        }
    }

    /**
     * Restocks items by reading from 'lowStock.csv' and updating their quantities to a predefined level.
     * Sets the stock level of each low-stock item to RESTOCK_LEVEL (e.g., 10).
     * After restocking, the 'lowStock.csv' file is cleared to indicate processing.
     */
    public void restockDepartmentItems() {
        File lowStockFile = new File(LOW_STOCK_FILE);
        if (!lowStockFile.exists()) {
            System.out.println("'lowStock.csv' does not exist. No items to restock.");
            return;
        }

        List<Item> lowStockItems = new ArrayList<>();

        // Read low stock items from 'lowStock.csv'
        try (BufferedReader reader = new BufferedReader(new FileReader(lowStockFile))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                Item item = Item.fromCSV(line);
                if (item != null) {
                    lowStockItems.add(item);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading low stock request from file: " + e.getMessage());
            return;
        }

        if (lowStockItems.isEmpty()) {
            System.out.println("No low stock items found in 'lowStock.csv'.");
            return;
        }

        // Update inventory to restock items to RESTOCK_LEVEL
        for (Item lowStockItem : lowStockItems) {
            Item inventoryItem = getItem(lowStockItem.getName());
            if (inventoryItem != null) {
                inventoryItem.setQuantity(RESTOCK_LEVEL);
                System.out.println("Restocked '" + inventoryItem.getName() + "' to quantity " + RESTOCK_LEVEL + ".");
            } else {
                System.out.println("Item '" + lowStockItem.getName() + "' not found in inventory. Skipping restock.");
            }
        }

        // Save updated inventory to file
        saveItemsToFile();
        System.out.println("Inventory updated successfully.");

        // Clear the 'lowStock.csv' file after restocking
        if (lowStockFile.delete()) {
            System.out.println("'lowStock.csv' has been cleared after restocking.");
        } else {
            System.out.println("Failed to clear 'lowStock.csv'. Please check manually.");
        }
    }
}