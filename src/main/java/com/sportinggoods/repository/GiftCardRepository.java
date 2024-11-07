package src.main.java.com.sportinggoods.repository;

import src.main.java.com.sportinggoods.model.GiftCard;

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
    }
}