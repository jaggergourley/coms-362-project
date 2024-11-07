package src.main.java.com.sportinggoods.repository;

import src.main.java.com.sportinggoods.model.ShippingOrder;
import src.main.java.com.sportinggoods.util.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class ShippingOrderRepository {
    private final String filePath = "data/shippingOrders.csv";

    public ShippingOrderRepository() {
        // Initialize the shippingOrders.csv file with headers if it doesn't exist
        FileUtils.initializeFile(filePath, "orderId,customerFirstName,customerLastName,items,totalPrice,shippingAddress,customerEmail,customerPhoneNumber,orderDate,status");
    }

    /**
     * Adds a new shipping order to the repository.
     *
     * @param order The ShippingOrder object to add.
     * @return True if added successfully, false otherwise.
     */
    public boolean addShippingOrder(ShippingOrder order) {
        return FileUtils.appendToFile(filePath, order.toCSV());
    }

    /**
     * Updates the status of an existing shipping order.
     *
     * @param orderId The ID of the order to update.
     * @param status  The new status.
     * @return True if updated successfully, false if order not found.
     */
    public boolean updateOrderStatus(String orderId, String status) {
        List<String> lines = FileUtils.readAllLines(filePath);
        List<String> updatedLines = new ArrayList<>();
        boolean found = false;

        for (String line : lines) {
            ShippingOrder order = ShippingOrder.fromCSV(line);
            if (order != null) {
                if (order.getOrderId().equals(orderId)) {
                    order.setStatus(status);
                    updatedLines.add(order.toCSV());
                    found = true;
                } else {
                    updatedLines.add(line);
                }
            }
        }

        if (found) {
            return FileUtils.writeAllLines(filePath, updatedLines);
        }
        return false;
    }

    /**
     * Retrieves a shipping order by its ID.
     *
     * @param orderId The ID of the order.
     * @return The ShippingOrder object if found, null otherwise.
     */
    public ShippingOrder getOrderById(String orderId) {
        List<String> lines = FileUtils.readAllLines(filePath);
        for (String line : lines) {
            ShippingOrder order = ShippingOrder.fromCSV(line);
            if (order != null && order.getOrderId().equals(orderId)) {
                return order;
            }
        }
        return null;
    }

    /**
     * Retrieves all shipping orders from the repository.
     *
     * @return A list of all ShippingOrder objects.
     */
    public List<ShippingOrder> getAllShippingOrders() {
        List<ShippingOrder> orders = new ArrayList<>();
        List<String> lines = FileUtils.readAllLines(filePath);
        for (String line : lines) {
            ShippingOrder order = ShippingOrder.fromCSV(line);
            if (order != null) {
                orders.add(order);
            }
        }
        return orders;
    }
}
