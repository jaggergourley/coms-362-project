package com.sportinggoods.model;

public class Discount {
    private String target;
    private double value;
    private String type; // PERCENTAGE or FIXED

    public Discount(String target, double value, String type) {
        this.target = target;
        this.value = value;
        this.type = type;
    }

    public String getTarget() {
        return target;
    }

    public double getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public String toCSV() {
        return target + "," + value + "," + type;
    }

    public static Discount fromCSV(String csv) {
        String[] parts = csv.split(",");
        if (parts.length != 3) return null;
        String target = parts[0];
        double value = Double.parseDouble(parts[1]);
        String type = parts[2];
        return new Discount(target, value, type);
    }

    @Override
    public String toString() {
        return "Target: " + target + ", Value: " + value + ", Type: " + type;
    }
}