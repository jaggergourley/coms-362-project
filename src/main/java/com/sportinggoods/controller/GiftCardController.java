package com.sportinggoods.controller;

import com.sportinggoods.model.GiftCard;
import com.sportinggoods.repository.GiftCardRepository;
import java.util.Optional;

public class GiftCardController {
    private GiftCardRepository giftCardRepository;
    private static final double HIGH_VALUE_THRESHOLD = 500.00;

    public GiftCardController(GiftCardRepository giftCardRepository) {
        this.giftCardRepository = giftCardRepository;
    }

    // Method to sell a new gift card
    public String sellGiftCard(double initialBalance, String customerInfo) {
        if (initialBalance <= 0) {
            return "Error: Invalid amount. Enter a positive balance.";
        }

        String code = generateUniqueCode();
        GiftCard giftCard = new GiftCard(code, initialBalance);

        // Log the sale transaction with customer information if provided
        giftCard.addTransaction("Gift Card sold to " +
                (customerInfo.isEmpty() ? "anonymous" : customerInfo) +
                " with initial balance: $" + initialBalance);

        // Save the new gift card to the repository
        giftCardRepository.save(giftCard);

        return "Gift Card successfully created with code: " + code + " and initial balance: $" + initialBalance;
    }

    // Method to redeem an existing gift card
    public String redeemGiftCard(String code, double amount) {
        Optional<GiftCard> optionalGiftCard = giftCardRepository.findByCode(code);

        if (optionalGiftCard.isEmpty()) {
            return "Error: Gift Card with code " + code + " not found.";
        }

        GiftCard giftCard = optionalGiftCard.get();
        if (!giftCard.isActive()) {
            return "Error: This gift card has already been fully redeemed or is inactive.";
        }

        if (amount <= 0 || amount > giftCard.getBalance()) {
            return "Error: Invalid amount. Enter an amount within the available balance.";
        }

        // Check if high-value authorization is required
        if (amount > HIGH_VALUE_THRESHOLD) {
            return "Authorization Required: Please submit for manager approval.";
        }

        String redemptionResult = giftCard.redeem(amount);

        // Save the updated gift card to the repository
        giftCardRepository.save(giftCard);

        return redemptionResult;
    }

    // Generate a unique gift card code (simplified for example)
    private String generateUniqueCode() {
        return "GC-" + System.currentTimeMillis(); // Unique code based on timestamp
    }

    // View gift card details by code
    public String viewGiftCardDetails(String code) {
        Optional<GiftCard> optionalGiftCard = giftCardRepository.findByCode(code);
        return optionalGiftCard.map(GiftCard::toString)
                .orElse("Gift Card with code " + code + " not found.");
    }
}