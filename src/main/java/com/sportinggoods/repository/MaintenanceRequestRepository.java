package com.sportinggoods.repository;

import com.sportinggoods.model.MaintenanceRequest;
import com.sportinggoods.util.FileUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MaintenanceRequestRepository {
    private final String filePath = "data/maintenanceRequests.csv";

    public MaintenanceRequestRepository() {
        FileUtils.initializeFile(filePath, "requestId,storeId,location,issueType,urgency,timeRemaining,status,lastUpdated");
    }

    public boolean addRequest(MaintenanceRequest request) {
        List<MaintenanceRequest> requests = getAllRequests();
        requests.add(request);
        return saveRequestsToFile(requests);
    }

    public List<MaintenanceRequest> getAllRequests() {
        List<MaintenanceRequest> requests = new ArrayList<>();
        List<String> lines = FileUtils.readAllLines(filePath);
        for (String line : lines) {
            try {
                MaintenanceRequest request = MaintenanceRequest.fromCSV(line);
                request.updateTimeRemaining();
                requests.add(request);
            } catch (IllegalArgumentException e) {
                System.err.println("Error reading request: " + e.getMessage());
            }
        }
        return requests;
    }

    public List<MaintenanceRequest> getAllRequestsByStoreId(int storeId) {
        return getAllRequests().stream()
                .filter(request -> request.getStoreId() == storeId)
                .collect(Collectors.toList());
    }

    public List<MaintenanceRequest> getAllRequestsSortedByStoreId() {
        return getAllRequests().stream()
                .sorted(Comparator.comparingInt(MaintenanceRequest::getStoreId))
                .collect(Collectors.toList());
    }

    public boolean saveRequestsToFile(List<MaintenanceRequest> requests) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("requestId,storeId,location,issueType,urgency,timeRemaining,status,lastUpdated");
            writer.newLine();
            for (MaintenanceRequest request : requests) {
                request.updateTimeRemaining();
                writer.write(request.toCSV());
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error saving requests to file: " + e.getMessage());
            return false;
        }
    }
}
