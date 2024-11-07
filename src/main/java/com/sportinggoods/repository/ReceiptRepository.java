package src.main.java.com.sportinggoods.repository;

import src.main.java.com.sportinggoods.model.Receipt;
import src.main.java.com.sportinggoods.util.FileUtils;

public class ReceiptRepository {
    private final String filePath = "data/receipts.csv";

    public ReceiptRepository() {
        FileUtils.initializeFile(filePath, "receiptId,customerId,itemName,quantity,totalCost,date");
    }

    public boolean logReceipt(Receipt receipt) {
        return FileUtils.appendToFile(filePath, receipt.toCSV());
    }
}
