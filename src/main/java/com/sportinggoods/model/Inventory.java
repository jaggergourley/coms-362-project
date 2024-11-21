package com.sportinggoods.model;

import com.sportinggoods.repository.DiscountRepository;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Manages inventory
public class Inventory {
    private Map<String, Item> items = new HashMap<>();
    private static final String FILE_PATH = "data/inventory.csv";

    //Constructor
    public Inventory() {
        items = loadItemsFromFile();
    }

    public Map<String, Item> getItems(){
        return items;
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

    /**
     * adds a item to the inventory by either updating
     * quantity or creating a new entry.
     * @param item
     */
    public void addItem(Item item){
        Item temp = items.get(item.getName());
        if(temp != null){ //item is in the inventory
            int newQuantity = temp.getQuantity() + item.getQuantity();
            temp.setQuantity(newQuantity);
            saveItemsToFile();
        }
        else{ // item is not in the inventory
            items.put(item.getName(), item);
            saveItemsToFile();
        }
    }

    public void addItems(ArrayList<Item> itemList){
        for(int i = 0; i < itemList.size(); i++){
            Item temp1 = itemList.get(i);
            Item temp2 = items.get(temp1.getName());
            if(temp2 != null){ //item exists in inventory
                int newQuantity = temp2.getQuantity() + temp1.getQuantity();
                temp2.setQuantity(newQuantity);
                saveItemsToFile();
            }
            else{
                items.put(temp1.getName(), temp1);
                saveItemsToFile();
            }
        }
    }

    public List<Item> getAllItems() {
        return new ArrayList<>(items.values());
    }

    public List<Item> getItemsByDepartment(String department) {
        List<Item> itemsInDepartment = new ArrayList<>();
        for (Item item : items.values()) {
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
        Item temp = items.get(item.getName());
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
            Item temp2 = items.get(temp1.getName());
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
        Item temp = items.get(itemName);
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

    public void printInventory(){
        System.out.println("Inventory:");
        for(Item item : items.values()){
            System.out.println(item);
        }
        System.out.println();
    }
}