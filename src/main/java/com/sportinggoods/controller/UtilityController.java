package com.sportinggoods.controller;

import com.sportinggoods.model.Utility;
import com.sportinggoods.repository.UtilityRepository;

import java.util.List;

public class UtilityController {
    private final UtilityRepository utilityRepository;

    public UtilityController(UtilityRepository repo) {
        this.utilityRepository = repo;
    }

    /**
     * Adds a new utility to the system.
     *
     * @param utilityId         The ID of the utility.
     * @param storeId           The store ID associated with the utility.
     * @param name              The name of the utility.
     * @param status            The current status (Active, Inactive, Outage).
     * @param energyConsumption The energy consumption in kWh.
     * @param lastUpdated       The last updated timestamp.
     * @param schedule          The operating schedule of the utility.
     * @return True if the utility was added successfully, false otherwise.
     */
    public boolean addUtility(String utilityId, int storeId, String name, String status, double energyConsumption, String lastUpdated, String schedule) {
        Utility utility = new Utility(utilityId, storeId, name, status, energyConsumption, lastUpdated, schedule);
        return utilityRepository.addUtility(utility);
    }

    /**
     * Updates the status of an existing utility.
     *
     * @param utilityId The ID of the utility to update.
     * @param status    The new status (Active, Inactive, Outage).
     * @return True if the status was updated successfully, false otherwise.
     */
    public boolean updateUtilityStatus(String utilityId, String status) {
        Utility utility = utilityRepository.getUtilityById(utilityId);
        if (utility == null) {
            return false;
        }

        utility.setStatus(status);
        return utilityRepository.saveUtility(utility);
    }

    /**
     * Updates the energy consumption of a utility.
     *
     * @param utilityId         The ID of the utility to update.
     * @param energyConsumption The new energy consumption value in kWh.
     * @return True if the energy consumption was updated successfully, false otherwise.
     */
    public boolean updateEnergyConsumption(String utilityId, double energyConsumption) {
        Utility utility = utilityRepository.getUtilityById(utilityId);
        if (utility == null) {
            return false; // Utility not found
        }

        utility.setEnergyConsumption(energyConsumption);
        return utilityRepository.saveUtility(utility);
    }

    /**
     * Updates the schedule of an existing utility.
     *
     * @param utilityId The ID of the utility to update.
     * @param schedule  The new schedule.
     * @return True if the schedule was updated successfully, false otherwise.
     */
    public boolean updateSchedule(String utilityId, String schedule) {
        Utility utility = utilityRepository.getUtilityById(utilityId);
        if (utility == null) {
            return false; // Utility not found
        }

        utility.setSchedule(schedule);
        return utilityRepository.saveUtility(utility);
    }

    /**
     * Applies seasonal presets to all utilities.
     *
     * @param season The season to apply (e.g., Winter, Summer).
     */
    public void applySeasonalPreset(String season) {
        utilityRepository.applySeasonalPreset(season);
    }

    /**
     * Retrieves a utility by its ID.
     *
     * @param utilityId The ID of the utility to retrieve.
     * @return The Utility object if found, null otherwise.
     */
    public Utility getUtilityById(String utilityId) {
        return utilityRepository.getUtilityById(utilityId);
    }

    /**
     * Retrieves all utilities for a specific store.
     *
     * @param storeId The store ID for which to retrieve utilities.
     * @return A list of Utility objects for the specified store.
     */
    public List<Utility> getUtilitiesByStoreId(int storeId) {
        return utilityRepository.getUtilitiesByStoreId(storeId);
    }

    /**
     * Retrieves all utilities in the system.
     *
     * @return A list of all Utility objects.
     */
    public List<Utility> getAllUtilities() {
        return utilityRepository.getAllUtilities();
    }

    /**
     * Runs a status check and returns all utilities with abnormalities or high energy usage.
     *
     * @return A list of utilities with abnormalities or high energy usage.
     */
    public List<Utility> runStatusCheck() {
        return utilityRepository.getAllUtilities().stream()
                .filter(utility -> "Outage".equalsIgnoreCase(utility.getStatus()) || utility.getEnergyConsumption() > 1000)
                .toList();
    }

    /**
     * Deletes a utility by its ID.
     *
     * @param utilityId The ID of the utility to delete.
     * @return True if the utility was deleted successfully, false otherwise.
     */
    public boolean deleteUtility(String utilityId) {
        return utilityRepository.deleteUtility(utilityId);
    }

    /**
     * Schedules maintenance for a specific utility.
     *
     * @param utilityId The ID of the utility to schedule maintenance for.
     * @param date      The maintenance date.
     * @return True if the scheduling was successful, false otherwise.
     */
    public boolean scheduleMaintenance(String utilityId, String date) {
        Utility utility = utilityRepository.getUtilityById(utilityId);
        if (utility == null) {
            return false; // Utility not found
        }

        // Simulate maintenance scheduling
        utility.setStatus("Maintenance Scheduled");
        System.out.println("Maintenance scheduled for " + utility.getName() + " on " + date);
        return utilityRepository.saveUtility(utility);
    }

    /**
     * Handles outage notifications for a utility.
     *
     * @param utilityId The ID of the utility experiencing an outage.
     * @return True if the outage was recorded successfully, false otherwise.
     */
    public boolean handleOutage(String utilityId) {
        Utility utility = utilityRepository.getUtilityById(utilityId);
        if (utility != null) {
            System.out.println("Outage reported for utility: " + utility.getName());
            utility.setStatus("Outage");
            return utilityRepository.saveUtility(utility);
        }
        return false;
    }
}
