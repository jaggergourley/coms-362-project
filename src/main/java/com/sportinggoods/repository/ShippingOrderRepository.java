package com.sportinggoods.repository;

import com.sportinggoods.model.Item;
import com.sportinggoods.model.ShippingOrder;
import com.sportinggoods.util.FileUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShippingOrderRepository {
    private final String filePath = "data/shippingOrder.csv";

    public ShippingOrderRepository() {
        FileUtils.initializeFile(filePath, "orderId,storeId,customerFirstName,customerLastName,items,totalPrice,shippingAddress,customerEmail,customerPhoneNumber,orderDate,status");
    }

    /**
     * Adds a new shipping order to the repository.
     *
     * @param order The ShippingOrder object to add.
     * @return True if added successfully, false otherwise.
     */
    public boolean addShippingOrder(ShippingOrder order) {
        List<ShippingOrder> orders = getAllShippingOrders();
        orders.add(order);
        return saveOrdersToFile(orders);
    }

    /**
     * Updates the status of an existing shipping order.
     *
     * @param orderId The ID of the order to update.
     * @param status  The new status.
     * @return True if updated successfully, false if order not found.
     */
    public boolean updateOrderStatus(String orderId, String status) {
        List<ShippingOrder> orders = getAllShippingOrders();
        boolean found = false;

        for (ShippingOrder order : orders) {
            if (order.getOrderId().equals(orderId)) {
                order.setStatus(status);
                found = true;
                break;
            }
        }

        if (found) {
            return saveOrdersToFile(orders);
        }
        return false;
    }

    /**
     * Updates the quantity of an existing shipping order.
     *
     * @param orderId The ID of the order to update.
     * @return True if updated successfully, false if order not found.
     */
    public boolean updateOrderQuantity(String orderId, Map<Item, Integer> items) {
        List<ShippingOrder> orders = getAllShippingOrders();

        ShippingOrder order = getOrderById(orderId);

        order.setItems(items);

        return saveOrdersToFile(orders);
    }

    /**
     * Updates the price of an existing shipping order.
     *
     * @param orderId The ID of the order to update.
     * @return True if updated successfully, false if order not found.
     */
    public boolean updateOrderPrice(String orderId, Map<Item, Integer> items) {
        List<ShippingOrder> orders = getAllShippingOrders();

        ShippingOrder order = getOrderById(orderId);

        double newPrice = 0;

        for(Map.Entry<Item, Integer> entry : order.getItems().entrySet()) {
            Item item = entry.getKey();

            newPrice += Math.round(item.getPrice() * item.getQuantity() * 100.0) / 100.0;  //might need to change
        }

        order.setTotalPrice(newPrice);

        return saveOrdersToFile(orders);
    }

    /**
     * Retrieves a shipping order by its ID.
     *
     * @param orderId The ID of the order.
     * @return The ShippingOrder object if found, null otherwise.
     */
    public ShippingOrder getOrderById(String orderId) {
        List<ShippingOrder> orders = getAllShippingOrders();
        for (ShippingOrder order : orders) {
            if (order.getOrderId().equals(orderId)) {
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

    public List<ShippingOrder> getAllShippingOrdersByStoreId(int storeId) {
        List<ShippingOrder> orders = new ArrayList<>();
        List<String> lines = FileUtils.readAllLines(filePath);
        for (String line : lines) {
            ShippingOrder order = ShippingOrder.fromCSV(line);
            if (order != null && order.getStoreId() == storeId) {
                orders.add(order);
            }
        }
        return orders;
    }


    /**
     * Deletes a shipping order by its ID.
     *
     * @param orderId The ID of the order to delete.
     * @return True if deleted successfully, false if order not found.
     */
    public boolean deleteOrder(String orderId) {
        List<ShippingOrder> orders = getAllShippingOrders();
        boolean found = orders.removeIf(order -> order.getOrderId().equals(orderId));
        return found && saveOrdersToFile(orders);
    }

    /**
     * Saves the list of orders to the file, overwriting the existing content.
     *
     * @param orders List of ShippingOrder objects to save to the file.
     * @return True if saved successfully, false otherwise.
     */
    public boolean saveOrdersToFile(List<ShippingOrder> orders) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write the CSV header
            writer.write("orderId,storeId,customerFirstName,customerLastName,items,totalPrice,shippingAddress,customerEmail,customerPhoneNumber,orderDate,status");
            writer.newLine();

            // Write each order to the file
            for (ShippingOrder order : orders) {
                writer.write(order.toCSV());
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error saving orders to file: " + e.getMessage());
            return false;
        }
    }
}
