package com.sportinggoods.model;

import com.sportinggoods.repository.ReceiptRepository;
import java.time.LocalDate;

public class Cashier extends Employee {
    //Constructor from Employee
    public Cashier(String name, int id, Schedule workSchedule, int storeID) {
        super(name, id, workSchedule, storeID);
    }

    // Converts a Cashier object to a CSV string
    public String toCSV() {
        return id + "," + name;
    }

    // Creates a Cashier object from a CSV string
    public static Cashier fromCSV(String csvLine) {
        String[] tokens = csvLine.split(",");
        if (tokens.length != 2) {
            return null;  // Invalid format
        }
        int id = Integer.parseInt(tokens[0]);
        String name = tokens[1];
        return new Cashier(name, id, null, 1);
    }

    @Override
    public String toString() {
        return "Cashier: " + name + " (ID: " + id + ")";
    }

}