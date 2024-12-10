package com.sportinggoods.repository;

import java.util.*;

public class WarrantyClaimRepository {
    private Map<String, String> warrantyData; // Key: Product ID, Value: Warranty Details
    private List<String> claimLogs;

    public WarrantyClaimRepository() {
        this.warrantyData = new HashMap<>();
        this.claimLogs = new ArrayList<>();
    }

    public boolean isWarrantyValid(String productId) {
        return warrantyData.containsKey(productId);
    }

    public String getWarrantyDetails(String productId) {
        return warrantyData.get(productId);
    }

    public void logClaim(String productId, String action) {
        claimLogs.add("Product ID: " + productId + ", Action: " + action + ", Date: " + new Date());
    }

    public List<String> getClaimLogs() {
        return new ArrayList<>(claimLogs);
    }
}

