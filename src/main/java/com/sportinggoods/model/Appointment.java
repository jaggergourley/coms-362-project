package com.sportinggoods.model;

import java.time.LocalDateTime;

public class Appointment {
    private String appointmentId;
    private int storeId;
    private String customerName;
    private String phoneNumber;
    private String itemName;
    private String issue;
    private LocalDateTime appointmentTime;
    private String status; // Scheduled, Completed, Canceled, Service Delayed

    public Appointment(String appointmentId, int storeId, String customerName, String phoneNumber,
                       String itemName, String issue, LocalDateTime appointmentTime, String status) {
        this.appointmentId = appointmentId;
        this.storeId = storeId;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.itemName = itemName;
        this.issue = issue;
        this.appointmentTime = appointmentTime;
        this.status = status;
    }
    public Appointment() {}


    // Getters and Setters
    public String getAppointmentId() { return appointmentId; }
    public int getStoreId() { return storeId; }
    public String getCustomerName() { return customerName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getItemName() { return itemName; }
    public String getIssue() { return issue; }
    public LocalDateTime getAppointmentTime() { return appointmentTime; }
    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    // To CSV string
    public String toCSV() {
        return String.join(",",
                appointmentId, String.valueOf(storeId), customerName, phoneNumber, itemName,
                issue, appointmentTime.toString(), status);
    }

    // From CSV string
    public static Appointment fromCSV(String csvLine) {
        String[] tokens = csvLine.split(",");
        return new Appointment(
                tokens[0], Integer.parseInt(tokens[1]), tokens[2], tokens[3], tokens[4],
                tokens[5], LocalDateTime.parse(tokens[6]), tokens[7]
        );
    }
}
