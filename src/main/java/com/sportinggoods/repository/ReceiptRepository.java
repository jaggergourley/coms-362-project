package com.sportinggoods.repository;

import com.sportinggoods.model.Receipt;
import com.sportinggoods.util.FileUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReceiptRepository {
    private final String filePath = "data/receipts.csv";

    public ReceiptRepository() {
        // Initialize the CSV file with the header if it doesn't exist
        FileUtils.initializeFile(filePath, "receiptId,customerId,itemsDetails,totalCost,date");
    }

    /**
     * Logs a receipt by assigning a unique receiptId and appending it to the CSV file.
     *
     * @param receipt The receipt to log.
     * @return true if logging was successful, false otherwise.
     */
    public boolean logReceipt(Receipt receipt) {
        // Assign receiptId
        int newReceiptId = getNextReceiptId();
        receipt.setReceiptId(newReceiptId);
        return FileUtils.appendToFile(filePath, receipt.toCSV());
    }

    /**
     * Determines the next receiptId by reading the existing receipts.
     *
     * @return The next receiptId.
     */
    private int getNextReceiptId() {
        int maxId = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Skip header
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 2); // Only parse receiptId
                if (parts.length > 0) {
                    try {
                        int id = Integer.parseInt(parts[0].trim());
                        if (id > maxId) {
                            maxId = id;
                        }
                    } catch (NumberFormatException e) {
                        // Skip invalid lines
                        System.out.println("Skipping line due to invalid receiptId: " + line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return maxId + 1;
    }

    /**
     * Checks if a customer has a valid receipt for returning a specified quantity of an item.
     *
     * @param customerId The ID of the customer.
     * @param itemName   The name of the item.
     * @param quantity   The quantity to return.
     * @return true if a valid receipt exists, false otherwise.
     */
    public boolean hasReceiptForReturn(int customerId, String itemName, int quantity) {
        Pattern pattern = Pattern.compile(Pattern.quote(itemName) + ":\\s*(\\d+)\\s*x", Pattern.CASE_INSENSITIVE);

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Skip the header line
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                // Split the line, ignoring commas within quotes
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                if (parts.length < 5) {
                    // Malformed line
                    continue;
                }

                int existingCustomerId;
                try {
                    existingCustomerId = Integer.parseInt(parts[1].trim());
                } catch (NumberFormatException e) {
                    // Invalid customerId, skip this line
                    continue;
                }

                double transactionTotal;
                try {
                    transactionTotal = Double.parseDouble(parts[3].trim());
                } catch (NumberFormatException e) {
                    // Invalid transactionTotal, skip this line
                    continue;
                }

                // Only consider purchase receipts (positive totalCost)
                if (transactionTotal < 0) {
                    continue; // Skip return receipts
                }

                // Verify customer ID
                if (existingCustomerId == customerId) {
                    String itemsDetails = parts[2].replace("\"", "").trim(); // Remove quotes around itemsDetails

                    Matcher matcher = pattern.matcher(itemsDetails);
                    while (matcher.find()) {
                        int existingQuantity = Integer.parseInt(matcher.group(1).trim());

                        if (existingQuantity >= quantity) {
                            return true;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}