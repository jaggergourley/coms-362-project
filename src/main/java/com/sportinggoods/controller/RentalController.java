package com.sportinggoods.controller;

import com.sportinggoods.repository.RentalRepository;


public class RentalController {
    private final RentalRepository rentalRepo;

    public RentalController(RentalRepository rentalRepo) {
        this.rentalRepo = rentalRepo;
    }

    public String handleRental(String itemId) {
        if (rentalRepo.isItemAvailable(itemId)) {
            rentalRepo.rentItem(itemId);
            return "Item rented successfully: " + itemId;
        } else {
            return "Item is not available for rental.";
        }
    }

    public String handleReturn(String itemId, boolean isDamaged) {
        rentalRepo.returnItem(itemId, isDamaged);
        return isDamaged ? "Item returned with damage. Additional charges may apply." : "Item returned successfully.";
    }
}
