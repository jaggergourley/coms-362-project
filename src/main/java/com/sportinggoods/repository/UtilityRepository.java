package com.sportinggoods.repository;

import com.sportinggoods.model.Utility;
import com.sportinggoods.util.FileUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UtilityRepository {
    private final String filePath = "data/utilities.csv";

    public UtilityRepository() {
        // Initialize the utilities.csv file with headers if it doesn't exist
        FileUtils.initializeFile(filePath, "utilityId,storeId,name,status,energyConsumption,lastUpdated,schedule");
    }

    /**
     * Adds a new utility to the repository.
     *
     * @param utility The Utility object to add.
     * @return True if added successfully, false otherwise.
     */
    public boolean addUtility(Utility utility) {
        List<Utility> utilities = getAllUtilities();
        utilities.add(utility);
        return saveUtilitiesToFile(utilities);
    }

    /**
     * Updates an existing utility in the repository.
     *
     * @param utility The Utility object with updated details.
     * @return True if updated successfully, false if utility not found.
     */
    public boolean saveUtility(Utility utility) {
        List<Utility> utilities = getAllUtilities();
        for (int i = 0; i < utilities.size(); i++) {
            if (utilities.get(i).getUtilityId().equals(utility.getUtilityId())) {
                utilities.set(i, utility); // Update the utility
                return saveUtilitiesToFile(utilities);
            }
        }
        return false; // Utility not found
    }

    /**
     * Updates the status of an existing utility.
     *
     * @param utilityId The ID of the utility to update.
     * @param status    The new status (e.g., Active, Inactive, Outage).
     * @return True if updated successfully, false if utility not found.
     */
    public boolean updateUtilityStatus(String utilityId, String status) {
        Utility utility = getUtilityById(utilityId);
        if (utility != null) {
            utility.setStatus(status);
            return saveUtility(utility);
        }
        return false;
    }

    /**
     * Updates the energy consumption of an existing utility.
     *
     * @param utilityId         The ID of the utility to update.
     * @param energyConsumption The new energy consumption value.
     * @return True if updated successfully, false if utility not found.
     */
    public boolean updateEnergyConsumption(String utilityId, double energyConsumption) {
        Utility utility = getUtilityById(utilityId);
        if (utility != null) {
            utility.setEnergyConsumption(energyConsumption);
            return saveUtility(utility);
        }
        return false;
    }

    /**
     * Updates the schedule of an existing utility.
     *
     * @param utilityId The ID of the utility to update.
     * @param schedule  The new schedule to set.
     * @return True if updated successfully, false if utility not found.
     */
    public boolean updateUtilitySchedule(String utilityId, String schedule) {
        Utility utility = getUtilityById(utilityId);
        if (utility != null) {
            utility.setSchedule(schedule);
            return saveUtility(utility);
        }
        return false;
    }

    /**
     * Applies a seasonal preset to all utilities.
     *
     * @param season The season to apply (e.g., Winter, Summer).
     */
    public void applySeasonalPreset(String season) {
        List<Utility> utilities = getAllUtilities();
        for (Utility utility : utilities) {
            if ("Winter".equalsIgnoreCase(season)) {
                utility.setEnergyConsumption(800); // Example Winter preset
            } else if ("Summer".equalsIgnoreCase(season)) {
                utility.setEnergyConsumption(600); // Example Summer preset
            }
            utility.setStatus("Active"); // Ensure utilities are active during the season
        }
        saveUtilitiesToFile(utilities);
    }

    /**
     * Retrieves a utility by its ID.
     *
     * @param utilityId The ID of the utility.
     * @return The Utility object if found, null otherwise.
     */
    public Utility getUtilityById(String utilityId) {
        List<Utility> utilities = getAllUtilities();
        for (Utility utility : utilities) {
            if (utility.getUtilityId().equals(utilityId)) {
                return utility;
            }
        }
        return null;
    }

    /**
     * Retrieves all utilities associated with a specific store.
     *
     * @param storeId The ID of the store.
     * @return A list of Utility objects for the specified store.
     */
    public List<Utility> getUtilitiesByStoreId(int storeId) {
        List<Utility> utilities = getAllUtilities();
        List<Utility> storeUtilities = new ArrayList<>();
        for (Utility utility : utilities) {
            if (utility.getStoreId() == (storeId)) {
                storeUtilities.add(utility);
            }
        }
        return storeUtilities;
    }

    /**
     * Retrieves all utilities from the repository.
     *
     * @return A list of all Utility objects.
     */
    public List<Utility> getAllUtilities() {
        List<Utility> utilities = new ArrayList<>();
        List<String> lines = FileUtils.readAllLines(filePath);
        for (String line : lines) {
            Utility utility = Utility.fromCSV(line);
            if (utility != null) {
                utilities.add(utility);
            }
        }
        return utilities;
    }

    /**
     * Deletes a utility by its ID.
     *
     * @param utilityId The ID of the utility to delete.
     * @return True if deleted successfully, false if utility not found.
     */
    public boolean deleteUtility(String utilityId) {
        List<Utility> utilities = getAllUtilities();
        boolean found = utilities.removeIf(utility -> utility.getUtilityId().equals(utilityId));
        return found && saveUtilitiesToFile(utilities);
    }

    /**
     * Saves the list of utilities to the file, overwriting the existing content.
     *
     * @param utilities List of Utility objects to save to the file.
     * @return True if saved successfully, false otherwise.
     */
    private boolean saveUtilitiesToFile(List<Utility> utilities) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write the CSV header
            writer.write("utilityId,storeId,name,status,energyConsumption,lastUpdated,schedule");
            writer.newLine();

            // Write each utility to the file
            for (Utility utility : utilities) {
                writer.write(utility.toCSV());
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error saving utilities to file: " + e.getMessage());
            return false;
        }
    }
}
