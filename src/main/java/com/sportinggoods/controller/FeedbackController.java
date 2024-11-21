package com.sportinggoods.controller;

import com.sportinggoods.repository.FeedbackRepository;

public class FeedbackController {
    private final FeedbackRepository feedbackRepo;

    public FeedbackController(FeedbackRepository feedbackRepo) {
        this.feedbackRepo = feedbackRepo;
    }

    // HandleCustomerFeedback: Handles feedback from customers and escalates if needed
    public void handleCustomerFeedback(int customerId, String feedback, boolean escalate) {
        System.out.println("Received feedback from Customer " + customerId + ": " + feedback);

        // Step 4: Record feedback
        feedbackRepo.logFeedback(customerId, feedback);

        if (escalate) {
            // Escalate feedback to management
            System.out.println("Escalating feedback to management for further investigation.");
            feedbackRepo.escalateFeedback(customerId, feedback);
        } else {
            System.out.println("Acknowledging feedback without escalation.");
        }

        // Follow-up with customer
        if (escalate) {
            System.out.println("Ensuring customer follow-up for escalated feedback.");
            feedbackRepo.recordFollowUp(customerId, feedback);
        }
    }
}