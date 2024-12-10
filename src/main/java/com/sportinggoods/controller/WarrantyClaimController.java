package com.sportinggoods.controller;
import com.sportinggoods.repository.WarrantyClaimRepository;


public class WarrantyClaimController {
    private final WarrantyClaimRepository warrantyRepo;

    public WarrantyClaimController(WarrantyClaimRepository warrantyRepo) {
        this.warrantyRepo = warrantyRepo;
    }

    public String handleWarrantyClaim(String productId, String proofOfPurchase) {
        if (!proofOfPurchase.isEmpty()) {
            if (warrantyRepo.isWarrantyValid(productId)) {
                String warrantyDetails = warrantyRepo.getWarrantyDetails(productId);
                warrantyRepo.logClaim(productId, "Warranty claim processed successfully");
                return "Warranty claim processed: " + warrantyDetails;
            } else {
                return "Warranty expired or not found for Product ID: " + productId;
            }
        } else {
            return "Proof of purchase is required.";
        }
    }
}
