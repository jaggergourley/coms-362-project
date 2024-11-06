package com.sportinggoods.repository;

import com.sportinggoods.model.Supplier;
import com.sportinggoods.util.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class SupplierRepository {
    private final String filePath = "data/suppliers.csv";

    public SupplierRepository() {
        // Initialize the suppliers.csv file with headers if it doesn't exist
        FileUtils.initializeFile(filePath, "supplierId,name,contactInfo,relationshipStatus,followUpAction");
    }

    /**
     * Adds a new supplier to the repository.
     *
     * @param supplier The Supplier object to add.
     * @return True if added successfully, false if supplier ID already exists.
     */
    public boolean addSupplier(Supplier supplier) {
        if (getSupplierById(supplier.getSupplierId()) != null) {
            return false; // Supplier ID already exists
        }
        return FileUtils.appendToFile(filePath, supplier.toCSV());
    }

    /**
     * Updates an existing supplier's information.
     *
     * @param updatedSupplier The Supplier object with updated information.
     * @return True if updated successfully, false if supplier not found.
     */
    public boolean updateSupplier(Supplier updatedSupplier) {
        List<String> lines = FileUtils.readAllLines(filePath);
        List<String> updatedLines = new ArrayList<>();
        boolean found = false;

        for (String line : lines) {
            Supplier supplier = Supplier.fromCSV(line);
            if (supplier != null) {
                if (supplier.getSupplierId().equals(updatedSupplier.getSupplierId())) {
                    updatedLines.add(updatedSupplier.toCSV());
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
     * Retrieves a supplier by their ID.
     *
     * @param supplierId The ID of the supplier.
     * @return The Supplier object if found, null otherwise.
     */
    public Supplier getSupplierById(String supplierId) {
        List<String> lines = FileUtils.readAllLines(filePath);
        for (String line : lines) {
            Supplier supplier = Supplier.fromCSV(line);
            if (supplier != null && supplier.getSupplierId().equals(supplierId)) {
                return supplier;
            }
        }
        return null;
    }

    /**
     * Retrieves all suppliers from the repository.
     *
     * @return A list of all Supplier objects.
     */
    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        List<String> lines = FileUtils.readAllLines(filePath);
        for (String line : lines) {
            Supplier supplier = Supplier.fromCSV(line);
            if (supplier != null) {
                suppliers.add(supplier);
            }
        }
        return suppliers;
    }
}
