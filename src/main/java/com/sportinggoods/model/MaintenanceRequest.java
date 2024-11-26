package com.sportinggoods.model;

import java.time.LocalDateTime;
import java.time.Duration;

public class MaintenanceRequest {
    private String requestId;
    private String location;
    private String issueType;
    private String urgency; // Emergency, High Priority, Medium Priority, Low Priority
    private long timeRemaining; // Time in seconds
    private String status; // Open, In Progress, Resolved
    private LocalDateTime lastUpdated;

    public MaintenanceRequest(String requestId, String location, String issueType, String urgency, long timeRemaining, String status, LocalDateTime lastUpdated) {
        this.requestId = requestId;
        this.location = location;
        this.issueType = issueType;
        this.urgency = urgency;
        this.timeRemaining = timeRemaining;
        this.status = status;
        this.lastUpdated = lastUpdated;
    }

    public MaintenanceRequest() {}

    // Getters and Setters
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public long getTimeRemaining() {
        return timeRemaining;
    }

    public void setTimeRemaining(long timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    // Decrease timeRemaining based on elapsed time
    public void updateTimeRemaining() {
        LocalDateTime now = LocalDateTime.now();
        long elapsedSeconds = Duration.between(lastUpdated, now).getSeconds();
        timeRemaining = Math.max(0, timeRemaining - elapsedSeconds);
        lastUpdated = now;
    }

    public String toCSV() {
        return requestId + "," + location + "," + issueType + "," + urgency + "," + timeRemaining + "," + status + "," + lastUpdated;
    }

    public static MaintenanceRequest fromCSV(String csvLine) {
        String[] tokens = csvLine.split(",");
        if (tokens.length != 7) {
            throw new IllegalArgumentException("Invalid CSV format: " + csvLine);
        }
        return new MaintenanceRequest(
                tokens[0],
                tokens[1],
                tokens[2],
                tokens[3],
                Long.parseLong(tokens[4]),
                tokens[5],
                LocalDateTime.parse(tokens[6])
        );
    }
}