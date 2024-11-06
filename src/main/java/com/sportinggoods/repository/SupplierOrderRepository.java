package com.sportinggoods.repository;

import com.sportinggoods.model.SupplierOrder;
import com.sportinggoods.util.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class SupplierOrderRepository {
    private final String filePath = "data/supplierOrders.csv";

    public SupplierOrderRepository() {
        // Initialize the supplierOrders.csv file with headers if it doesn't exist
        FileUtils.initializeFile(filePath, "orderId,supplierId,productDetails,quantity,totalPrice,orderDate,status");
    }

    /**
     * Adds a new supplier order to the repository.
     *
     * @param order The SupplierOrder object to add.
     * @return True if added successfully, false otherwise.
     */
    public boolean addSupplierOrder(SupplierOrder order) {
        return FileUtils.appendToFile(filePath, order.toCSV());
    }

    /**
     * Updates the status of an existing supplier order.
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
            SupplierOrder order = SupplierOrder.fromCSV(line);
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
     * Retrieves a supplier order by its ID.
     *
     * @param orderId The ID of the order.
     * @return The SupplierOrder object if found, null otherwise.
     */
    public SupplierOrder getOrderById(String orderId) {
        List<String> lines = FileUtils.readAllLines(filePath);
        for (String line : lines) {
            SupplierOrder order = SupplierOrder.fromCSV(line);
            if (order != null && order.getOrderId().equals(orderId)) {
                return order;
            }
        }
        return null;
    }

    /**
     * Retrieves all supplier orders from the repository.
     *
     * @return A list of all SupplierOrder objects.
     */
    public List<SupplierOrder> getAllSupplierOrders() {
        List<SupplierOrder> orders = new ArrayList<>();
        List<String> lines = FileUtils.readAllLines(filePath);
        for (String line : lines) {
            SupplierOrder order = SupplierOrder.fromCSV(line);
            if (order != null) {
                orders.add(order);
            }
        }
        return orders;
    }
}
