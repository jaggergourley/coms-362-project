package com.sportinggoods.controller;

import com.sportinggoods.model.Utility;
import com.sportinggoods.repository.UtilityRepository;

import java.time.LocalDateTime;
import java.util.List;

public class UtilityController {
    private final UtilityRepository utilityRepository;

    public UtilityController(UtilityRepository repo) {
        this.utilityRepository = repo;
    }

    public boolean addUtility(String utilityId, int storeId, String name, String status, double energyConsumption, LocalDateTime lastUpdated, String schedule) {
        return utilityRepository.addUtility(new Utility(utilityId, storeId, name, status, energyConsumption, lastUpdated, schedule));
    }

    public boolean updateUtilityStatus(String utilityId, int storeId, String status) {
        return utilityRepository.updateUtilityField(utilityId, storeId, utility -> utility.setStatus(status));
    }

    public boolean updateEnergyConsumption(String utilityId, int storeId, double energyConsumption) {
        return utilityRepository.updateUtilityField(utilityId, storeId, utility -> utility.setEnergyConsumption(energyConsumption));
    }

    public boolean updateSchedule(String utilityId, int storeId, String schedule) {
        return utilityRepository.updateUtilityField(utilityId, storeId, utility -> utility.setSchedule(schedule));
    }

    public void applySeasonalPreset(String season) {
        utilityRepository.applySeasonalPreset(season);
    }

    public Utility getUtilityById(String utilityId) {
        return utilityRepository.getUtilityById(utilityId);
    }

    public List<Utility> getUtilitiesByStoreId(int storeId) {
        return utilityRepository.getUtilitiesByStoreId(storeId);
    }

    public List<Utility> getAllUtilities() {
        return utilityRepository.getAllUtilities();
    }

    public Utility getUtilityByIdAndStoreId(String utilityId, int storeId) {
        return utilityRepository.getUtilityByIdAndStoreId(utilityId, storeId);
    }

    public List<Utility> runStatusCheck() {
        return utilityRepository.getAllUtilities().stream()
                .filter(utility -> "Outage".equalsIgnoreCase(utility.getStatus()) || utility.getEnergyConsumption() > 1000)
                .toList();
    }

    public boolean deleteUtility(String utilityId) {
        return utilityRepository.deleteUtility(utilityId);
    }

    public boolean scheduleMaintenance(String utilityId, int storeId, String date) {
        return utilityRepository.updateUtilityField(utilityId, storeId, utility -> {
            utility.setStatus("Maintenance Scheduled");
            System.out.println("Maintenance scheduled for " + utility.getName() + " on " + date);
        });
    }

    public boolean handleOutage(String utilityId, int storeId) {
        return utilityRepository.updateUtilityField(utilityId, storeId, utility -> {
            System.out.println("Outage reported for utility: " + utility.getName());
            utility.setStatus("Outage");
        });
    }
}
