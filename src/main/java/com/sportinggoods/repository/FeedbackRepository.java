package com.sportinggoods.repository;

import java.util.ArrayList;
import java.util.List;

public class FeedbackRepository {
    private final List<String> feedbackLogs = new ArrayList<>();
    private final List<String> followUpLogs = new ArrayList<>();

    // Log feedback provided by the customer
    public void logFeedback(int customerId, String feedback) {
        feedbackLogs.add("Customer " + customerId + ": " + feedback);
        System.out.println("Feedback logged for Customer " + customerId + ".");
    }

    // Escalate feedback for further investigation
    public void escalateFeedback(int customerId, String feedback) {
        System.out.println("Escalating feedback for Customer " + customerId + ": " + feedback);
        // Here, you'd notify managers or store the escalation for action
        feedbackLogs.add("Escalated: Customer " + customerId + ": " + feedback);
    }

    // Record that follow-up is required
    public void recordFollowUp(int customerId, String feedback) {
        followUpLogs.add("Follow-up required for Customer " + customerId + ": " + feedback);
        System.out.println("Follow-up recorded for Customer " + customerId + ".");
    }

    // View all logged feedback
    public List<String> getFeedbackLogs() {
        return feedbackLogs;
    }

    // View all follow-ups
    public List<String> getFollowUpLogs() {
        return followUpLogs;
    }
}
