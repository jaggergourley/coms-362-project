package com.sportinggoods.repository;

import com.sportinggoods.model.Utility;
import com.sportinggoods.util.FileUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class UtilityRepository {
    private final String filePath = "data/utilities.csv";

    public UtilityRepository() {
        FileUtils.initializeFile(filePath, "utilityId,storeId,name,status,energyConsumption,lastUpdated,schedule");
    }

    public boolean addUtility(Utility utility) {
        List<Utility> utilities = getAllUtilities();
        utilities.add(utility);
        return saveUtilitiesToFile(utilities);
    }

    public boolean updateUtilityField(String utilityId, int storeId, Consumer<Utility> updateFunction) {
        List<Utility> utilities = getAllUtilities();
        for (Utility utility : utilities) {
            if (utility.getUtilityId().equals(utilityId) && utility.getStoreId() == storeId) {
                updateFunction.accept(utility);
                return saveUtilitiesToFile(utilities);
            }
        }
        return false;
    }

    public boolean updateUtilityStatus(String utilityId, int storeId, String status) {
        return updateUtilityField(utilityId, storeId, utility -> utility.setStatus(status));
    }

    public boolean updateEnergyConsumption(String utilityId, int storeId, double energyConsumption) {
        return updateUtilityField(utilityId, storeId, utility -> utility.setEnergyConsumption(energyConsumption));
    }

    public boolean updateUtilitySchedule(String utilityId, int storeId, String schedule) {
        return updateUtilityField(utilityId, storeId, utility -> utility.setSchedule(schedule));
    }

    public Utility getUtilityById(String utilityId) {
        return getAllUtilities().stream()
                .filter(utility -> utility.getUtilityId().equals(utilityId))
                .findFirst()
                .orElse(null);
    }

    public Utility getUtilityByIdAndStoreId(String utilityId, int storeId) {
        return getAllUtilities().stream()
                .filter(utility -> utility.getUtilityId().equals(utilityId) && utility.getStoreId() == storeId)
                .findFirst()
                .orElse(null);
    }

    public List<Utility> getUtilitiesByStoreId(int storeId) {
        return getAllUtilities().stream()
                .filter(utility -> utility.getStoreId() == storeId)
                .toList();
    }

    public List<Utility> getAllUtilities() {
        List<Utility> utilities = new ArrayList<>();
        List<String> lines = FileUtils.readAllLines(filePath);
        for (String line : lines) {
            if (!line.trim().isEmpty() && !line.startsWith("utilityId")) {
                utilities.add(Utility.fromCSV(line));
            }
        }
        return utilities;
    }

    public boolean deleteUtility(String utilityId) {
        List<Utility> utilities = getAllUtilities();
        boolean found = utilities.removeIf(utility -> utility.getUtilityId().equals(utilityId));
        return found && saveUtilitiesToFile(utilities);
    }

    public void applySeasonalPreset(String season) {
        List<Utility> utilities = getAllUtilities();
        utilities.forEach(utility -> {
            if ("Winter".equalsIgnoreCase(season)) {
                utility.setEnergyConsumption(800);
            } else if ("Summer".equalsIgnoreCase(season)) {
                utility.setEnergyConsumption(600);
            }
            utility.setStatus("Active");
        });
        saveUtilitiesToFile(utilities);
    }

    private boolean saveUtilitiesToFile(List<Utility> utilities) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("utilityId,storeId,name,status,energyConsumption,lastUpdated,schedule");
            writer.newLine();
            for (Utility utility : utilities) {
                writer.write(utility.toCSV());
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error saving utilities to file: " + e.getMessage());
            return false;
        }
    }
}
