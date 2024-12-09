package com.sportinggoods.controller;

import com.sportinggoods.model.Feedback;
import com.sportinggoods.repository.FeedbackRepository;

import java.util.List;

public class FeedbackController {
    private final FeedbackRepository feedbackRepo;

    public FeedbackController(FeedbackRepository feedbackRepo) {
        this.feedbackRepo = feedbackRepo;
    }

    public void handleCustomerFeedback(int customerId, String feedback, boolean escalate) {

        String status = escalate ? "Needs Escalation" : "Pending";
        feedbackRepo.logFeedback(customerId, feedback, status);
    }

    public List<Feedback> getAllFeedback() {
        return feedbackRepo.getAllFeedback();
    }

    public boolean escalateFeedback(int customerId, String feedbackId) {
        return feedbackRepo.escalateFeedback(feedbackId);
    }

    public boolean respondToFeedback(String feedbackId, String response) {
        return feedbackRepo.recordResponse(feedbackId, response);
    }
}