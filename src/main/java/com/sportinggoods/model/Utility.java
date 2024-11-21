package com.sportinggoods.model;

public class Utility {
    private String utilityId;
    private int storeId;
    private String name;
    private String status; // Active, Inactive, Outage
    private double energyConsumption; // kWh
    private String lastUpdated; // Timestamp
    private String schedule;

    // Constructors
    public Utility(String utilityId, int storeId, String name, String status, double energyConsumption, String lastUpdated, String schedule) {
        this.utilityId = utilityId;
        this.storeId = storeId;
        this.name = name;
        this.status = status;
        this.energyConsumption = energyConsumption;
        this.lastUpdated = lastUpdated;
        this.schedule = schedule;
    }

    public Utility() {}

    // Getters and Setters
    public String getUtilityId() {
        return utilityId;
    }

    public void setUtilityId(String utilityId) {
        this.utilityId = utilityId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getEnergyConsumption() {
        return energyConsumption;
    }

    public void setEnergyConsumption(double energyConsumption) {
        this.energyConsumption = energyConsumption;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }


    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    @Override
    public String toString() {
        return "Utility ID: " + utilityId +
                ", Store ID: " + storeId +
                ", Name: " + name +
                ", Status: " + status +
                ", Energy Consumption: " + energyConsumption +
                " kWh, Last Updated: " + lastUpdated +
                ", Schedule: " + schedule;
    }

    // toCSV method for CSV representation
    public String toCSV() {
        return utilityId + "," + storeId + "," + name + "," + status + "," + energyConsumption + "," + lastUpdated + "," + schedule;
    }

    // Create Utility from CSV
    public static Utility fromCSV(String csvLine) {
        String[] tokens = csvLine.split(",");
        if (tokens.length != 7) {
            System.err.println("Invalid CSV format: " + csvLine);
            return null;
        }
        try {
            return new Utility(tokens[0], Integer.parseInt(tokens[1]), tokens[2], tokens[3], Double.parseDouble(tokens[4]), tokens[5], tokens[6]);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing CSV line: " + csvLine);
            return null;
        }
    }

}
