package src.main.java.com.sportinggoods.model;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

// Manages inventory
public class Inventory {
    private Map<String, Item> items = new HashMap<>();
    private static final String FILE_PATH = "data/inventory.csv";

    //Constructor
    public Inventory() {
        items = loadItemsFromFile();
    }

    //Getter
    public Item getItem(String itemName) {
        return items.get(itemName);
    }

    /**
     * Updates current inventory with the change of item inputted.
     * @param itemName
     * @param quantityChange
     */
    // Updates the quantity of an item in the inventory
    public void updateQuantity(String itemName, int quantityChange) {
        Item item = items.get(itemName);
        if (item != null) {
            item.setQuantity(item.getQuantity() + quantityChange);
            saveItemsToFile(); // Save the updated inventory
        }
    }

    // Checks if an item is available in the required quantity
    public boolean checkAvailability(String itemName, int quantity) {
        Item item = items.get(itemName);
        return item != null && item.getQuantity() >= quantity;
    }


    // Loads items from a CSV file into the inventory map
    private Map<String, Item> loadItemsFromFile() {
        Map<String, Item> loadedItems = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                Item item = Item.fromCSV(line);
                if (item != null) {
                    loadedItems.put(item.getName(), item);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading inventory from file: " + e.getMessage());
        }
        return loadedItems;
    }

    // Saves the current state of inventory to a CSV file
    public void saveItemsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write("name,price,department,quantity\n"); // Write CSV header
            for (Item item : items.values()) {
                writer.write(item.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving inventory to file: " + e.getMessage());
        }
    }
}
