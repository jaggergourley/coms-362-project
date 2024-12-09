package com.sportinggoods.model;

import java.time.LocalDate;

public class Feedback {
    private String feedbackId;
    private int customerId;
    private String content;
    private LocalDate date;
    private String status; // "Escalated", "Resolved", or other responses

    public Feedback(String feedbackId, int customerId, String content, String status, LocalDate date) {
        this.feedbackId = feedbackId;
        this.customerId = customerId;
        this.content = content;
        this.date = date;
        this.status = status; // Default to no response
    }

    public String getFeedbackId() {
        return feedbackId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getContent() {
        return content;
    }


    public LocalDate getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String response) {
        this.status = response;
    }

    public boolean isEscalated() {
        return "Escalated".equalsIgnoreCase(status);
    }

    @Override
    public String toString() {
        return "Feedback ID: " + feedbackId
                + ", Customer ID: " + customerId
                + ", Content: " + content
                + ", Date: " + date
                + ", Status: " + (status == null || status.isEmpty() ? "No Status" : status);
    }
}

