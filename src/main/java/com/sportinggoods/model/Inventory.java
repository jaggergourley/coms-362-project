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
                int newQuantity = item.getQuantity() + quantityChange;
                if (newQuantity < 0) {
                    System.out.println("Error: Insufficient stock for " + itemName);
                    return; // Prevent negative inventory
                }
                item.setQuantity(newQuantity);
                System.out.println("Updated " + itemName + " quantity to " + newQuantity);
                saveItemsToFile(); // Save only after updating the specific item
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
        for (Item temp : items) {
            if (temp.getName().equalsIgnoreCase(item.getName()) && temp.getStoreID() == item.getStoreID()) {
                temp.setQuantity(temp.getQuantity() + item.getQuantity());
                itemFound = true;
                break;
            }
        }
        if (!itemFound) {
            items.add(item);
        }
        try {
            saveItemsToFile(); // Save updated list to file
        } catch (Exception e) {
            System.err.println("Error saving items to file: " + e.getMessage());
        }
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
        // Load the existing inventory from the file
        Map<String, Item> inventoryMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line = reader.readLine(); // Skip the header
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Skip empty lines
                Item item = Item.fromCSV(line);
                if (item != null) {
                    inventoryMap.put(item.getName() + ":" + item.getStoreID(), item); // Use unique key
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading inventory file: " + e.getMessage());
        }

        // Update the inventoryMap with the current items
        for (Item item : items) {
            String key = item.getName() + ":" + item.getStoreID();
            inventoryMap.put(key, item); // Replace or add the updated item
        }

        // Write the updated inventory back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write("name,price,department,quantity,storeID\n"); // Write header
            for (Item item : inventoryMap.values()) {
                writer.write(item.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving inventory to file: " + e.getMessage());
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
     * Writes the low-stock items to 'lowStock.csv' with the storeID.
     *
     * @param storeID The ID of the store generating the low stock request.
     */
    public void generateLowStockRequest(int storeID) {
        List<Item> lowStockItems = new ArrayList<>();

        // Identify items with stock below the threshold for the given storeID
        for (Item item : items) {
            if (item.getQuantity() <= LOW_STOCK_THRESHOLD && item.getStoreID() == storeID) {
                lowStockItems.add(item);
            }
        }

        if (lowStockItems.isEmpty()) {
            System.out.println("All items are sufficiently stocked. No low stock request generated for Store ID: " + storeID);
            return;
        }

        // Write low stock items to 'lowStock.csv' with storeID
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOW_STOCK_FILE, true))) { // Append mode
            // Check if the file is empty to write headers
            File file = new File(LOW_STOCK_FILE);
            if (file.length() == 0) {
                writer.write("storeID,name,price,department,quantity\n");
            }
            for (Item item : lowStockItems) {
                writer.write(storeID + "," + item.toCSV());
                writer.newLine();
            }
            System.out.println("Low stock request generated successfully for Store ID: " + storeID + ". See 'lowStock.csv' for details.");
        } catch (IOException e) {
            System.out.println("Error writing low stock request to file: " + e.getMessage());
        }
    }

    /**
     * Restocks items by reading from 'lowStock.csv' and updating their quantities to a predefined level.
     * Processes only items matching the provided storeID.
     * After restocking, removes the processed items from 'lowStock.csv'.
     *
     * @param storeID The ID of the store to restock items for.
     */
    public void restockDepartmentItems(int storeID) {
        File lowStockFileObj = new File(LOW_STOCK_FILE);
        if (!lowStockFileObj.exists()) {
            System.out.println("'lowStock.csv' does not exist. No items to restock.");
            return;
        }

        List<Item> lowStockItems = new ArrayList<>();
        List<String> remainingEntries = new ArrayList<>();

        // Read low stock items from 'lowStock.csv'
        try (BufferedReader reader = new BufferedReader(new FileReader(lowStockFileObj))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip header
                    continue;
                }
                if (line.trim().isEmpty()) continue; // Skip empty lines
                String[] parts = line.split(",");
                if (parts.length < 5) { // Validate structure
                    System.err.println("Error parsing low stock line: " + line);
                    continue;
                }

                int fileStoreID = Integer.parseInt(parts[0]);
                if (fileStoreID == storeID) {
                    // Parse item details
                    String name = parts[1];
                    double price = Double.parseDouble(parts[2]);
                    String department = parts[3];
                    int quantity = Integer.parseInt(parts[4]);

                    lowStockItems.add(new Item(name, price, department, quantity, storeID));
                } else {
                    remainingEntries.add(line); // Keep entries for other stores
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading low stock requests: " + e.getMessage());
            return;
        }

        if (lowStockItems.isEmpty()) {
            System.out.println("No low stock items found for Store ID: " + storeID);
            return;
        }

        // Restock items
        for (Item lowStockItem : lowStockItems) {
            Item inventoryItem = getItem(lowStockItem.getName());
            if (inventoryItem != null && inventoryItem.getStoreID() == storeID) {
                inventoryItem.setQuantity(RESTOCK_LEVEL);
                System.out.println("Restocked '" + inventoryItem.getName() + "' to quantity " + RESTOCK_LEVEL + ".");
            } else {
                System.out.println("Item '" + lowStockItem.getName() + "' not found in inventory for Store ID: " + storeID + ". Skipping restock.");
            }
        }

        // Save updated inventory to file
        saveItemsToFile();
        System.out.println("Inventory updated successfully for Store ID: " + storeID + ".");

        // Rewrite 'lowStock.csv' with remaining entries
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOW_STOCK_FILE))) {
            writer.write("storeID,name,price,department,quantity\n"); // Write header
            for (String entry : remainingEntries) {
                writer.write(entry);
                writer.newLine();
            }
            System.out.println("'lowStock.csv' has been updated after restocking.");
        } catch (IOException e) {
            System.out.println("Error updating 'lowStock.csv': " + e.getMessage());
        }
    }
}