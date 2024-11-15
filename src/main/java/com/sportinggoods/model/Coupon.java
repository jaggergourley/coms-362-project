package com.sportinggoods.model;

import java.time.LocalDate;

public class Coupon {
    private String code;
    private String discountType; // "PERCENTAGE" or "FIXED"
    private double discountValue;
    private LocalDate expirationDate;

    public Coupon(String code, String discountType, double discountValue, LocalDate expirationDate) {
        this.code = code;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.expirationDate = expirationDate;
    }

    public String getCode() {
        return code;
    }

    public String getDiscountType() {
        return discountType;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(expirationDate);
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%.2f,%s", code, discountType, discountValue, expirationDate);
    }

    public static Coupon fromCSV(String csvLine) {
        String[] parts = csvLine.split(",", -1);
        return new Coupon(parts[0], parts[1], Double.parseDouble(parts[2]), LocalDate.parse(parts[3]));
    }
}