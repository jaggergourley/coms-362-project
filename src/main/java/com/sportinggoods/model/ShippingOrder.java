package com.sportinggoods.model;

import com.sportinggoods.model.Item;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ShippingOrder {
    private String orderId;
    private int storeId;
    private String customerFirstName;
    private String customerLastName;
    private Map<Item, Integer> items;
    private double totalPrice;
    private String shippingAddress;
    private String customerEmail;
    private String customerPhoneNumber;
    private LocalDate orderDate;
    private String status; // Confirmed, Shipped, Partially Shipped

    // Constructors
    public ShippingOrder(String orderId, int storeId, String customerFirstName, String customerLastName, Map<Item, Integer> items,
                         double totalPrice, String shippingAddress, String customerEmail, String customerPhoneNumber,
                         LocalDate orderDate, String status) {
        this.orderId = orderId;
        this.storeId = storeId;
        this.customerFirstName = customerFirstName;
        this.customerLastName = customerLastName;
        this.items = items;
        this.totalPrice = totalPrice;
        this.shippingAddress = shippingAddress;
        this.customerEmail = customerEmail;
        this.customerPhoneNumber = customerPhoneNumber;
        this.orderDate = orderDate;
        this.status = status;
    }

    public ShippingOrder() {}

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }
    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    public Map<Item, Integer> getItems() {
        return items;
    }

    public void setItems(Map<Item, Integer> items) {
        this.items = items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // toCSV method for CSV representation
    public String toCSV() {
        return orderId + "|" + storeId + "|" + customerFirstName + "|" + customerLastName + "|" + itemsToCSV() + "|" + totalPrice +
                "|" + shippingAddress + "|" + customerEmail + "|" + customerPhoneNumber + "|" + orderDate + "|" + status;
    }

    // Helper method to convert items map to CSV format
    private String itemsToCSV() {
        StringBuilder itemsCSV = new StringBuilder();
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();

            itemsCSV.append(item.getName())
                    .append(",")
                    .append(item.getPrice())
                    .append(",")
                    .append(item.getDepartment())
                    .append(",")
                    .append(quantity)
                    .append(";");
        }
        return itemsCSV.toString();
    }

    // Create ShippingOrder from CSV
    public static ShippingOrder fromCSV(String csvLine) {
        try {
            // Split the line into tokens by "|"
            String[] tokens = csvLine.split("\\|", -1);

            // Ensure we have exactly 11 tokens
            if (tokens.length != 11) {
                System.out.println("Error: Expected 11 tokens, got " + tokens.length);
                return null;
            }

            // Parse items using the updated parseItems method
            Map<Item, Integer> items = parseItems(tokens[4]);
            if (items == null || items.isEmpty()) {
                System.out.println("Error: Items could not be parsed.");
                return null;
            }

            // Parse totalPrice
            double totalPrice;
            try {
                totalPrice = Double.parseDouble(tokens[5]);
            } catch (NumberFormatException e) {
                System.out.println("Error parsing totalPrice: " + e.getMessage());
                return null;
            }

            // Parse order date
            LocalDate orderDate;
            try {
                orderDate = LocalDate.parse(tokens[9]);
            } catch (Exception e) {
                System.out.println("Error parsing orderDate: " + e.getMessage());
                return null;
            }

            // Construct and return the ShippingOrder object
            return new ShippingOrder(
                    tokens[0],                // orderId
                    Integer.parseInt(tokens[1]), // storeId
                    tokens[2],                // customerFirstName
                    tokens[3],                // customerLastName
                    items,                    // items
                    totalPrice,               // totalPrice
                    tokens[6],                // shippingAddress
                    tokens[7],                // customerEmail
                    tokens[8],                // customerPhoneNumber
                    orderDate,                // orderDate
                    tokens[10]                // status
            );

        } catch (Exception e) {
            System.out.println("An error occurred while parsing CSV: " + e.getMessage());
            return null;
        }
    }



    // Helper method to parse items from CSV format
    private static Map<Item, Integer> parseItems(String itemsCSV) {
        Map<Item, Integer> items = new HashMap<>();
        String[] itemEntries = itemsCSV.split(";"); // Each entry represents an item

        for (String entry : itemEntries) {
            if (entry.isEmpty()) continue; // Skip empty entries

            // Split each item entry into name, price, department, and quantity
            String[] itemData = entry.split(",");
            if (itemData.length != 4) {
                System.out.println("Error parsing item entry: " + entry);
                continue;
            }

            // Parse item details
            String name = itemData[0].trim();
            double price;
            String department = itemData[2].trim();
            int quantity;

            try {
                price = Double.parseDouble(itemData[1].trim());
                quantity = Integer.parseInt(itemData[3].trim());
            } catch (NumberFormatException e) {
                System.out.println("Error parsing item details: " + e.getMessage());
                continue;

            }

            // Create the Item object with parsed details
            Item item = new Item(name, price, department, 0, 0); // Use 0 for inventory quantity and storeID by default
            items.put(item, quantity);
        }
        return items;
    }


}
