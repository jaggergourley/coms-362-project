package src.main.java.com.sportinggoods.model;

import src.main.java.com.sportinggoods.model.Item;

import java.time.LocalDate;
import java.util.Map;

public class ShippingOrder {
    private String orderId;
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
    public ShippingOrder(String orderId, String customerFirstName, String customerLastName, Map<Item, Integer> items,
                         double totalPrice, String shippingAddress, String customerEmail, String customerPhoneNumber,
                         LocalDate orderDate, String status) {
        this.orderId = orderId;
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
        return orderId + "," + customerFirstName + "," + customerLastName + "," + itemsToCSV() + "," + totalPrice +
                "," + shippingAddress + "," + customerEmail + "," + customerPhoneNumber + "," + orderDate + "," + status;
    }

    // Helper method to convert items map to CSV format
    private String itemsToCSV() {
        StringBuilder itemsCSV = new StringBuilder();
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            itemsCSV.append(entry.getKey()).append(":").append(entry.getValue()).append(";");
        }
        return itemsCSV.toString();
    }

    // Create ShippingOrder from CSV
    public static ShippingOrder fromCSV(String csvLine) {
        String[] tokens = csvLine.split(",");
        if (tokens.length != 10) {
            return null;
        }
        Map<Item, Integer> items = parseItems(tokens[3]);
        return new ShippingOrder(
                tokens[0],
                tokens[1],
                tokens[2],
                items,
                Double.parseDouble(tokens[4]),
                tokens[5],
                tokens[6],
                tokens[7],
                LocalDate.parse(tokens[8]),
                tokens[9]
        );
    }

    // Helper method to parse items from CSV format
    private static Map<Item, Integer> parseItems(String itemsCSV) {
        Map<Item, Integer> items = new java.util.HashMap<>();
        String[] itemEntries = itemsCSV.split(";"); // Each entry represents an item

        for (String entry : itemEntries) {
            String[] itemData = entry.split(":");
            if (itemData.length == 4) { // Ensure we have all required data (name, price, department, quantity)
                String name = itemData[0];
                double price = Double.parseDouble(itemData[1]);
                String department = itemData[2];
                int quantity = Integer.parseInt(itemData[3]);

                Item item = new Item(name, price, department, quantity);
                items.put(item, quantity); // Add the item and its quantity to the map
            }
        }
        return items;
    }

}

