package src.main.java.com.sportinggoods.controller;

import src.main.java.com.sportinggoods.model.Receipt;
import src.main.java.com.sportinggoods.repository.ReceiptRepository;

public class ReceiptController {
    private ReceiptRepository receiptRepo;

    public ReceiptController(ReceiptRepository receiptRepo) {
        this.receiptRepo = receiptRepo;
    }

    public void logReceipt(Receipt receipt) {
        receiptRepo.logReceipt(receipt);
    }
}
