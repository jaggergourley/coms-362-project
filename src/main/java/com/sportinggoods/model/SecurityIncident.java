package com.sportinggoods.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SecurityIncident {
    private String incidentID;
    private String type;
    private String description;
    private String location;
    private String timestamp;
    private String status; // Pending, Resolved, Escalated
    private String comments;

    public SecurityIncident(String incidentID, String type, String description, String location, String status) {
        this.incidentID = incidentID;
        this.type = type;
        this.description = description;
        this.location = location;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.status = status;
        this.comments = "";
    }

    // Getters and Setters
    public String getIncidentID() { return incidentID; }
    public String getType() { return type; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public String getTimestamp() { return timestamp; }
    public String getStatus() { return status; }
    public String getComments() { return comments; }
    public void setStatus(String status) { this.status = status; }
    public void setComments(String comments) { this.comments = comments; }

    // Convert to CSV format
    public String toCSV() {
        return String.join(",", incidentID, type, description, location, timestamp, status, comments);
    }

    // Create from CSV
    public static SecurityIncident fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        return new SecurityIncident(parts[0], parts[1], parts[2], parts[3], parts[5]);
    }

    @Override
    public String toString() {
        return String.format("ID: %s | Type: %s | Location: %s | Status: %s | Timestamp: %s",
                incidentID, type, location, status, timestamp);
    }
}