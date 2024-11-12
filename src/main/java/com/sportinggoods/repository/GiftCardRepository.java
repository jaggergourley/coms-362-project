package com.sportinggoods.repository;

import com.sportinggoods.model.GiftCard;
import java.util.List;
import java.util.Optional;

public class GiftCardRepository {
    private List<GiftCard> giftCards;

    public GiftCardRepository(List<GiftCard> giftCards) {
        this.giftCards = giftCards;
    }

    // Find a gift card by code
    public Optional<GiftCard> findByCode(String code) {
        return giftCards.stream()
                        .filter(giftCard -> giftCard.getCode().equalsIgnoreCase(code))
                        .findFirst();
    }

    // Save or update a gift card
    public void save(GiftCard giftCard) {
        // Check if the gift card already exists
        findByCode(giftCard.getCode()).ifPresentOrElse(
            existingGiftCard -> {
                // If it exists, update it by removing and re-adding the updated instance
                giftCards.remove(existingGiftCard);
                giftCards.add(giftCard);
            },
            // If it does not exist, add it as a new gift card
            () -> giftCards.add(giftCard)
        );
    }
}