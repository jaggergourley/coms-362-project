package src.main.java.com.sportinggoods.controller;

import src.main.java.com.sportinggoods.model.GiftCard;
import src.main.java.com.sportinggoods.repository.GiftCardRepository;

import java.util.Optional;
import java.util.UUID;

public class GiftCardController {
    private GiftCardRepository giftCardRepository;
    private static final double AUTHORIZATION_THRESHOLD = 500.0; // Example threshold

    public GiftCardController(GiftCardRepository giftCardRepository) {
        this.giftCardRepository = giftCardRepository;
    }

    // Sell a new gift card
    public String sellGiftCard(double amount) {
        if (amount <= 0) {
            return "Error: Invalid gift card amount.";
        }

        if (amount > AUTHORIZATION_THRESHOLD && !requestAuthorization()) {
            return "Error: Authorization required for high-value gift cards.";
        }

        String code = UUID.randomUUID().toString(); // Generate unique gift card code
        GiftCard newGiftCard = new GiftCard(code, amount);
        giftCardRepository.save(newGiftCard);

        return "Gift card sold successfully. Code: " + newGiftCard.getCode();
    }

    // Redeem an existing gift card
    public String redeemGiftCard(String code, double amount) {
        Optional<GiftCard> giftCardOpt = giftCardRepository.findByCode(code);

        // Alternate Flow A1: Gift Card Not Found
        if (giftCardOpt.isEmpty()) {
            return "Error: Gift card not found.";
        }

        GiftCard giftCard = giftCardOpt.get();

        // Alternate Flow A2: Invalid Amount
        String redemptionResult = giftCard.redeem(amount);
        if (redemptionResult.startsWith("Error")) {
            return redemptionResult;
        }

        // Log transaction and update gift card in repository
        giftCardRepository.save(giftCard);
        return redemptionResult;
    }

    private boolean requestAuthorization() {
        // Placeholder for requesting authorization
        return true; // Always granted
    }
}