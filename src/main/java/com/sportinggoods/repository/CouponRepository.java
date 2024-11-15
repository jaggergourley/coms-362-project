package com.sportinggoods.repository;

import com.sportinggoods.model.Coupon;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class CouponRepository {
    private final String filePath = "data/coupons.csv";
    private Map<String, Coupon> coupons;

    public CouponRepository() {
        File file = new File(filePath);
        if (!file.exists()) {
            initializeCouponFile();
        }
        this.coupons = loadCouponsFromFile();
    }

    /**
     * Initializes the coupon file with a header if it doesn't exist.
     */
    private void initializeCouponFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("code,discountType,discountValue,expirationDate");
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error initializing coupon file: " + e.getMessage());
        }
    }

    /**
     * Loads coupons from the CSV file into memory.
     * @return Map of coupon code to Coupon object.
     */
    private Map<String, Coupon> loadCouponsFromFile() {
        Map<String, Coupon> loadedCoupons = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    Coupon coupon = Coupon.fromCSV(line);
                    loadedCoupons.put(coupon.getCode(), coupon);
                } catch (Exception e) {
                    System.out.println("Error parsing coupon line: " + line + " - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading coupons from file: " + e.getMessage());
        }
        return loadedCoupons;
    }

    /**
     * Saves the current coupons to the CSV file.
     */
    public void saveToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("code,discountType,discountValue,expirationDate");
            writer.newLine();
            for (Coupon coupon : coupons.values()) {
                writer.write(coupon.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving coupons to file: " + e.getMessage());
        }
    }

    /**
     * Retrieves a coupon by its code.
     * @param code The coupon code.
     * @return The Coupon object if found, or null otherwise.
     */
    public Optional<Coupon> findByCode(String code) {
        return Optional.ofNullable(coupons.get(code));
    }

    /**
     * Adds a new coupon to the repository.
     * @param coupon The Coupon object to add.
     */
    public void addCoupon(Coupon coupon) {
        coupons.put(coupon.getCode(), coupon);
        saveToCSV();
    }

    /**
     * Validates if a coupon is still valid based on its expiration date.
     * @param code The coupon code.
     * @return true if valid, false otherwise.
     */
    public boolean isCouponValid(String code) {
        Optional<Coupon> optionalCoupon = findByCode(code);
        return optionalCoupon.isPresent() && !optionalCoupon.get().isExpired();
    }

    /**
     * Removes an expired coupon from the repository.
     * @param code The coupon code.
     * @return true if the coupon was removed, false otherwise.
     */
    public boolean removeCoupon(String code) {
        if (coupons.containsKey(code)) {
            coupons.remove(code);
            saveToCSV();
            return true;
        }
        return false;
    }

    /**
     * Returns all active coupons.
     * @return A list of active coupons.
     */
    public List<Coupon> getActiveCoupons() {
        return coupons.values().stream()
                .filter(coupon -> !coupon.isExpired())
                .collect(Collectors.toList());
    }
}