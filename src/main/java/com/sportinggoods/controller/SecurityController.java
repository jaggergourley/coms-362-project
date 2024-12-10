package com.sportinggoods.controller;

import com.sportinggoods.model.SecurityIncident;
import com.sportinggoods.repository.SecurityRepository;
import java.util.List;
import java.util.UUID;

public class SecurityController {
    private SecurityRepository repository;

    public SecurityController(SecurityRepository repository) {
        this.repository = repository;
    }

    public void reportIncident(String type, String description, String location) {
        String incidentID = UUID.randomUUID().toString();
        SecurityIncident incident = new SecurityIncident(incidentID, type, description, location, "Pending");
        repository.addIncident(incident);
    }

    public List<SecurityIncident> getPendingIncidents() {
        return repository.getAllIncidents().stream()
                .filter(incident -> "Pending".equalsIgnoreCase(incident.getStatus()))
                .toList();
    }

    public void updateIncidentStatus(String incidentID, String status, String comments) {
        List<SecurityIncident> incidents = repository.getAllIncidents();
        for (SecurityIncident incident : incidents) {
            if (incident.getIncidentID().equals(incidentID)) {
                incident.setStatus(status);
                incident.setComments(comments);
            }
        }
        repository.updateIncidents(incidents);
    }
}