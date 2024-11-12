package com.sportinggoods.controller;

import com.sportinggoods.model.Receipt;
import com.sportinggoods.repository.ReceiptRepository;

public class ReceiptController {
    private ReceiptRepository receiptRepo;

    public ReceiptController(ReceiptRepository receiptRepo) {
        this.receiptRepo = receiptRepo;
    }

    public void logReceipt(Receipt receipt) {
        receiptRepo.logReceipt(receipt);
    }
}
