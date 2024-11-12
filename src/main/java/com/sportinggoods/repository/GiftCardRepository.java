package com.sportinggoods.repository;

import com.sportinggoods.model.GiftCard;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GiftCardRepository {
    private final String filePath = "data/giftCards.csv";
    private List<GiftCard> giftCards;

    public GiftCardRepository() {
        initializeFileWithHeader();
        this.giftCards = loadGiftCardsFromFile();
    }

    private void initializeFileWithHeader() {
        File file = new File(filePath);
        if (!file.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write("code,balance,isActive,transactionHistory");
                writer.newLine();
            } catch (IOException e) {
                System.out.println("Error initializing gift card file: " + e.getMessage());
            }
        }
    }

    // Load gift cards from CSV file
    private List<GiftCard> loadGiftCardsFromFile() {
        List<GiftCard> cards = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 4) {
                    try {
                        String code = parts[0].trim();
                        double balance = Double.parseDouble(parts[1].trim());
                        boolean isActive = parts[2].trim().equalsIgnoreCase("active");
                        List<String> transactionHistory = List.of(parts[3].trim().split(";"));

                        GiftCard giftCard = new GiftCard(code, balance);
                        if (!isActive) {
                            giftCard.redeem(balance);  // Set to inactive if balance is 0
                        }
                        transactionHistory.forEach(giftCard::addTransaction);
                        cards.add(giftCard);
                    } catch (NumberFormatException e) {
                        System.out.println("Skipping invalid entry: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading gift cards from file: " + e.getMessage());
        }
        return cards;
    }

    // Find a gift card by code
    public Optional<GiftCard> findByCode(String code) {
        return giftCards.stream()
                .filter(giftCard -> giftCard.getCode().equalsIgnoreCase(code))
                .findFirst();
    }

    // Save or update a gift card in the list
    public void save(GiftCard giftCard) {
        giftCards.removeIf(existing -> existing.getCode().equalsIgnoreCase(giftCard.getCode()));
        giftCards.add(giftCard);
        saveToCSV();  // Save changes to CSV file
    }

    // Save all gift cards to the CSV file
    private void saveToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("code,balance,isActive,transactionHistory");
            writer.newLine();
            for (GiftCard giftCard : giftCards) {
                writer.write(giftCard.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving gift cards to file: " + e.getMessage());
        }
    }
}