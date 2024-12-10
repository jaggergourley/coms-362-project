package com.sportinggoods.controller;

import com.sportinggoods.model.MaintenanceRequest;
import com.sportinggoods.repository.MaintenanceRequestRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class MaintenanceRequestController {
    private final MaintenanceRequestRepository repository;

    public MaintenanceRequestController(MaintenanceRequestRepository repository) {
        this.repository = repository;
    }

    public boolean createRequest(int storeId, String location, String issueType, String urgency) {
        long timeRemaining = calculateTimeRemaining(urgency);
        String id = UUID.randomUUID().toString();
        MaintenanceRequest request = new MaintenanceRequest(id, storeId, location, issueType, urgency, timeRemaining, "Open", LocalDateTime.now());
        return repository.addRequest(request);
    }

    public List<MaintenanceRequest> getAllRequests(int storeId) {
        return repository.getAllRequestsByStoreId(storeId);
    }

    private long calculateTimeRemaining(String urgency) {
        return switch (urgency.toLowerCase()) {
            case "emergency" -> 3600; // 1 hour
            case "high priority" -> 86400; // 24 hours
            case "medium priority" -> 259200; // 3 days
            case "low priority" -> 604800; // 1 week
            default -> 0;
        };
    }

    public boolean updateRequestStatus(String requestId, String newStatus) {
        List<MaintenanceRequest> requests = repository.getAllRequests();

        for (MaintenanceRequest request : requests) {
            if (request.getRequestId().equals(requestId)) {
                if(newStatus.equals("Open")){
                    request.updateTimeRemaining();
                }
                else{
                    request.setTimeRemaining(0);
                }
                request.setStatus(newStatus);
                request.setLastUpdated(LocalDateTime.now());
                return repository.saveRequestsToFile(requests);
            }
        }

        System.out.println("Request with ID " + requestId + " not found.");
        return false;
    }
}
