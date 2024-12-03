package com.sportinggoods.repository;

import com.sportinggoods.model.Feedback;
import com.sportinggoods.util.FileUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FeedbackRepository {
    private static final String FILE_PATH = "data/feedback.csv";
    private static final String HEADER = "feedbackId,customerId,content,response,date";

    public FeedbackRepository() {
        FileUtils.initializeFile(FILE_PATH, HEADER);
    }

    /**
     * Logs feedback provided by the customer.
     *
     * @param customerId The ID of the customer.
     * @param content    The content of the feedback.
     */
    public void logFeedback(int customerId, String content, String status) {
        String feedbackId = "FB" + (getAllFeedback().size() + 1);
        String date = LocalDate.now().toString();
        String feedbackEntry = String.join(",", feedbackId, String.valueOf(customerId), content, status, date);
        if (FileUtils.appendToFile(FILE_PATH, feedbackEntry)) {
            System.out.println("Feedback logged: " + feedbackEntry);
        } else {
            System.err.println("Failed to log feedback.");
        }
    }

    /**
     * Escalates feedback for further investigation.
     *
     * @param feedbackId The ID of the feedback to escalate.
     * @return True if successfully escalated, false otherwise.
     */
    public boolean escalateFeedback(String feedbackId) {
        List<Feedback> feedbackList = getAllFeedback();
        Optional<Feedback> feedbackOptional = feedbackList.stream()
                .filter(f -> f.getFeedbackId().equals(feedbackId))
                .findFirst();

        if (feedbackOptional.isPresent()) {
            Feedback feedback = feedbackOptional.get();
            feedback.setStatus("Escalated");
            saveAllFeedback(feedbackList); // Persist the changes to the file
            return true;
        }
        return false;
    }

    /**
     * Records a response to feedback.
     *
     * @param feedbackId The ID of the feedback.
     * @param response   The response to the feedback.
     * @return True if successfully recorded, false otherwise.
     */
    public boolean recordResponse(String feedbackId, String response) {
        List<Feedback> feedbackList = getAllFeedback();
        Optional<Feedback> feedbackOptional = feedbackList.stream()
                .filter(f -> f.getFeedbackId().equals(feedbackId))
                .findFirst();

        if (feedbackOptional.isPresent()) {
            Feedback feedback = feedbackOptional.get();
            feedback.setStatus(response);
            saveAllFeedback(feedbackList);
            System.out.println("Updated feedback: " + feedback);// Persist the changes to the file
            return true;
        }
        System.err.println("Feedback ID not found: " + feedbackId);
        return false;
    }

    /**
     * Retrieves all feedback from the CSV file.
     *
     * @return A list of Feedback objects.
     */
    public List<Feedback> getAllFeedback() {
        List<Feedback> feedbackList = new ArrayList<>();
        List<String> lines = FileUtils.readAllLines(FILE_PATH);

        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 5) {
                Feedback feedback = new Feedback(
                        parts[0], // feedbackId
                        Integer.parseInt(parts[1]), // customerId
                        parts[2], // content
                        parts[3], // // response ("Escalated", "Resolved", etc.)
                        LocalDate.parse(parts[4]) // date
                );
                feedbackList.add(feedback);
            }
        }
        return feedbackList;
    }

    /**
     * Saves all feedback back to the CSV file.
     *
     * @param feedbackList The list of feedback to save.
     */
    private void saveAllFeedback(List<Feedback> feedbackList) {
        List<String> lines = new ArrayList<>();
        lines.add(HEADER); // Add header
        for (Feedback feedback : feedbackList) {
            lines.add(String.join(",",
                    feedback.getFeedbackId(),
                    String.valueOf(feedback.getCustomerId()),
                    feedback.getContent(),
                    feedback.getStatus() != null ? feedback.getStatus() : "",
                    feedback.getDate().toString()
            ));
        }
        FileUtils.writeAllLines(FILE_PATH, lines);
    }
}
