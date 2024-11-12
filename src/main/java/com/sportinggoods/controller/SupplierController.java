package com.sportinggoods.controller;

import com.sportinggoods.model.Supplier;
import com.sportinggoods.model.SupplierOrder;
import com.sportinggoods.repository.SupplierOrderRepository;
import com.sportinggoods.repository.SupplierRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class SupplierController {
    private SupplierRepository supplierRepo;
    private SupplierOrderRepository orderRepo;

    public SupplierController(SupplierRepository supplierRepo, SupplierOrderRepository orderRepo) {
        this.supplierRepo = supplierRepo;
        this.orderRepo = orderRepo;
    }

    /**
     * Handles the coordinateSuppliers use case.
     *
     * @param supplierId         The ID of the supplier to update.
     * @param relationshipStatus The new relationship status.
     * @param followUpAction     The planned follow-up action.
     * @return True if update is successful, false otherwise.
     */
    public boolean coordinateSuppliers(String supplierId, String relationshipStatus, String followUpAction) {
        Supplier supplier = supplierRepo.getSupplierById(supplierId);
        if (supplier == null) {
            System.err.println("Supplier ID does not exist.");
            return false;
        }
        supplier.setRelationshipStatus(relationshipStatus);
        supplier.setFollowUpAction(followUpAction);
        return supplierRepo.updateSupplier(supplier);
    }

    /**
     * Handles the placeSupplierOrder use case.
     *
     * @param supplierId     The ID of the supplier to place an order with.
     * @param productDetails Details of the products to order.
     * @param quantity       Quantity of the products.
     * @param totalPrice     Total price of the order.
     * @return True if the order is placed successfully, false otherwise.
     */
    public boolean placeSupplierOrder(String supplierId, String productDetails, int quantity, double totalPrice) {
        Supplier supplier = supplierRepo.getSupplierById(supplierId);
        if (supplier == null) {
            System.err.println("Supplier ID does not exist.");
            return false;
        }

        // Generate a unique order ID
        String orderId = UUID.randomUUID().toString();

        // Create a new SupplierOrder object
        SupplierOrder order = new SupplierOrder(
                orderId,
                supplierId,
                productDetails,
                quantity,
                totalPrice,
                LocalDate.now(),
                "Pending" // Initial status
        );

        // Add the order to the repository
        return orderRepo.addSupplierOrder(order);
    }

    /**
     * Retrieves a supplier by their ID.
     *
     * @param supplierId The ID of the supplier.
     * @return The Supplier object if found, null otherwise.
     */
    public Supplier getSupplierById(String supplierId) {
        return supplierRepo.getSupplierById(supplierId);
    }

    /**
     * Adds a new supplier to the system.
     *
     * @param supplier The Supplier object to add.
     * @return True if added successfully, false if supplier ID already exists.
     */
    public boolean addSupplier(Supplier supplier) {
        return supplierRepo.addSupplier(supplier);
    }

    /**
     * Retrieves all suppliers.
     *
     * @return List of all suppliers.
     */
    public List<Supplier> getAllSuppliers() {
        return supplierRepo.getAllSuppliers();
    }

    /**
     * Retrieves all supplier orders.
     *
     * @return List of all supplier orders.
     */
    public List<SupplierOrder> getAllSupplierOrders() {
        return orderRepo.getAllSupplierOrders();
    }

    /**
     * Updates the status of a supplier order.
     *
     * @param orderId The ID of the order to update.
     * @param status  The new status.
     * @return True if updated successfully, false otherwise.
     */
    public boolean updateOrderStatus(String orderId, String status) {
        return orderRepo.updateOrderStatus(orderId, status);
    }
}
