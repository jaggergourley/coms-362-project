package com.sportinggoods.repository;

import com.sportinggoods.model.Discount;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscountRepository {
    private List<Discount> discounts = new ArrayList<>();
    private Map<String, Double> originalPrices = new HashMap<>();
    private static final String DISCOUNTS_FILE_PATH = "data/discounts.csv";
    private static final String ORIGINAL_PRICES_FILE_PATH = "data/original_prices.csv";

    public DiscountRepository() {
        loadDiscountsFromFile();
        loadOriginalPricesFromFile();
    }

    public List<Discount> getDiscounts() {
        return discounts;
    }

    public void addDiscount(Discount discount, double originalPrice) {
        discounts.add(discount);
        originalPrices.putIfAbsent(discount.getTarget(), originalPrice); // Avoid overwriting
        saveDiscountsToFile();
        saveOriginalPricesToFile();
    }

    public boolean removeDiscount(String target) {
        boolean removed = discounts.removeIf(d -> d.getTarget().equalsIgnoreCase(target));
        if (removed) {
            originalPrices.remove(target); // Remove associated original price
            saveDiscountsToFile();
            saveOriginalPricesToFile();
        }
        return removed;
    }

    public double restoreOriginalPrice(String target) {
        if (originalPrices.containsKey(target)) {
            return originalPrices.get(target);
        } else {
            throw new IllegalArgumentException("No original price found for target: " + target);
        }
    }

    private void saveDiscountsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DISCOUNTS_FILE_PATH))) {
            writer.write("target,value,type\n"); // CSV Header
            for (Discount discount : discounts) {
                writer.write(discount.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving discounts to file: " + e.getMessage());
        }
    }

    private void loadDiscountsFromFile() {
        File file = new File(DISCOUNTS_FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(DISCOUNTS_FILE_PATH))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                Discount discount = Discount.fromCSV(line);
                if (discount != null) {
                    discounts.add(discount);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading discounts from file: " + e.getMessage());
        }
    }

    public void saveOriginalPricesToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ORIGINAL_PRICES_FILE_PATH))) {
            writer.write("target,originalPrice\n"); // CSV Header
            for (Map.Entry<String, Double> entry : originalPrices.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving original prices to file: " + e.getMessage());
        }
    }

    public void loadOriginalPricesFromFile() {
        File file = new File(ORIGINAL_PRICES_FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String target = parts[0];
                    double originalPrice = Double.parseDouble(parts[1]);
                    originalPrices.put(target, originalPrice);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading original prices from file: " + e.getMessage());
        }
    }

    public Map<String, Double> getOriginalPrices() {
        return originalPrices;
    }
}