package com.sportinggoods.repository;

import com.sportinggoods.model.Receipt;
import com.sportinggoods.util.FileUtils;

public class ReceiptRepository {
    private final String filePath = "data/receipts.csv";

    public ReceiptRepository() {
        FileUtils.initializeFile(filePath, "receiptId,customerId,itemName,quantity,totalCost,date");
    }

    public boolean logReceipt(Receipt receipt) {
        return FileUtils.appendToFile(filePath, receipt.toCSV());
    }
}
