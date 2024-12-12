package com.sportinggoods.controller;

import com.sportinggoods.model.Campaign;
import com.sportinggoods.repository.CampaignRepository;
import java.time.LocalDate;
import java.util.List;

public class CampaignController {
    private CampaignRepository campaignRepository;

    public CampaignController(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    public String createCampaign(String title, LocalDate startDate, LocalDate endDate, String type, double value, String discountType, String customerMessage) {
        if (startDate.isAfter(endDate)) {
            return "Start date must be before end date.";
        }
        Campaign campaign = new Campaign(title, startDate, endDate, type, value, discountType, customerMessage, "Active");
        campaignRepository.addCampaign(campaign);
        notifyManager(campaign);
        notifySubscribers(campaign);
        return "Campaign created successfully.";
    }

    public List<Campaign> viewAllCampaigns() {
        return campaignRepository.getAllCampaigns();
    }

    public String endCampaign(String title) {
        try {
            campaignRepository.endCampaign(title);
            return "Campaign ended successfully.";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    private void notifyManager(Campaign campaign) {
        // Placeholder for sending notifications to the manager
        System.out.println("Manager notified: Request to create " + campaign.getType() +
                " with value " + campaign.getValue());
    }

    private void notifySubscribers(Campaign campaign) {
        // Placeholder for sending notifications to subscribers
        System.out.println("Subscribers notified: " + campaign.getCustomerMessage());
    }
}