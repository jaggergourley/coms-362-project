package com.sportinggoods.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StoreList {
    private ArrayList<Store> stores = new ArrayList<>();
    private int storeCount;
    private static final String FILE_PATH = "data/stores.csv";

    public StoreList() {
        stores = loadStoresFromFile();
        storeCount = stores.size();
    }

    public int getStoreCount(){
        return storeCount;
    }

    public void setStoreCount(int newCount){
        storeCount = newCount;
    }

    public ArrayList<Store> getStores() {
        return stores;
    }



    public Store getStore(int storeId) {
        for (Store store : stores) {
            if (store.getID() == storeId) {
                return store;
            }
        }
        return null; // Store not found
    }

    public void addStore(Store store) {
        if (!stores.contains(store)) {
            stores.add(store);
        }

        try {
            saveStoresToFile(); // Save updated list to file
        } catch (Exception e) {
            System.err.println("Error saving stores to file: " + e.getMessage());
        }
    }

    public void removeStore(Store store) {
        for(int i = 0; i < storeCount; i++){
            Store s = stores.get(i);
            if(s.getAddress().matches(store.getAddress()) && s.getID() == store.getID()){
                stores.remove(i);
                storeCount--;
            }
        }
        try {
            saveStoresToFile(); // Save updated list to file
        } catch (Exception e) {
            System.err.println("Error saving stores to file: " + e.getMessage());
        }
    }

    private ArrayList<Store> loadStoresFromFile() {
        ArrayList<Store> loadedStores = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                Store store = Store.fromCSV(line);
                if (store != null) {
                    loadedStores.add(store);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading stores from file: " + e.getMessage());
        }
        return loadedStores;
    }

    public void saveStoresToFile() {
        Map<Integer, Store> storeMap = new HashMap<>();

        // Add current stores to the map (avoid duplicates)
        for (Store store : stores) {
            storeMap.putIfAbsent(store.getID(), store);
        }

        // Write merged data back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write("id,address\n"); // Write CSV header
            for (Store store : storeMap.values()) {
                writer.write(store.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving stores to file: " + e.getMessage());
        }
    }

    public void printStores(){
        for(int i = 0; i < stores.size(); i++){
            Store s = stores.get(i);
            System.out.println(s.getID() + " - " + s.getAddress());
        }
    }
}
