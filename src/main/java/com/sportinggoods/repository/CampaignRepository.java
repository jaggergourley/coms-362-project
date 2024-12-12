package com.sportinggoods.repository;

import com.sportinggoods.model.Campaign;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CampaignRepository {
    private static final String CAMPAIGN_FILE_PATH = "data/campaigns.csv";
    private List<Campaign> campaigns = new ArrayList<>();

    public CampaignRepository() {
        loadCampaignsFromFile();
    }

    public List<Campaign> getAllCampaigns() {
        return campaigns;
    }

    public void addCampaign(Campaign campaign) {
        campaigns.add(campaign);
        saveCampaignsToFile();
    }

    public void endCampaign(String title) {
        for (Campaign campaign : campaigns) {
            if (campaign.getTitle().equalsIgnoreCase(title)) {
                campaign.setStatus("Ended");
                saveCampaignsToFile();
                return;
            }
        }
        throw new IllegalArgumentException("Campaign not found: " + title);
    }

    private void saveCampaignsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMPAIGN_FILE_PATH))) {
            // Write header
            writer.write("Title,StartDate,EndDate,Type,Value,DiscountType,CustomerMessage,Status\n");

            // Write campaign data
            for (Campaign campaign : campaigns) {
                writer.write(String.format("%s,%s,%s,%s,%.2f,%s,%s,%s\n",
                    campaign.getTitle(),
                    campaign.getStartDate(),
                    campaign.getEndDate(),
                    campaign.getType(),
                    campaign.getValue(),
                    campaign.getDiscountType(), // Added DiscountType
                    campaign.getCustomerMessage().replace(",", ";"), // Prevent breaking CSV
                    campaign.getStatus()
                ));
            }
        } catch (IOException e) {
            System.err.println("Error saving campaigns to file: " + e.getMessage());
        }
    }

    private void loadCampaignsFromFile() {
        File file = new File(CAMPAIGN_FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length != 8) continue;

                Campaign campaign = new Campaign(
                    parts[0],                        // Title
                    LocalDate.parse(parts[1]),       // StartDate
                    LocalDate.parse(parts[2]),       // EndDate
                    parts[3],                        // Type
                    Double.parseDouble(parts[4]),    // Value
                    parts[5],                        // DiscountType
                    parts[6],                        // CustomerMessage
                    parts[7]                         // Status
                );
                campaigns.add(campaign);
            }
        } catch (IOException e) {
            System.err.println("Error loading campaigns from file: " + e.getMessage());
        }
    }
}