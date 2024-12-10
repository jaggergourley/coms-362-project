package com.sportinggoods.repository;

import com.sportinggoods.model.SecurityIncident;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SecurityRepository {
    private static final String FILE_PATH = "data/security_incidents.csv";

    public SecurityRepository() {
        initializeFile();
    }

    private void initializeFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
                writer.write("incidentID,type,description,location,timestamp,status,comments\n"); // CSV header
            } catch (IOException e) {
                System.err.println("Error initializing security incidents file: " + e.getMessage());
            }
        }
    }

    public List<SecurityIncident> getAllIncidents() {
        List<SecurityIncident> incidents = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                incidents.add(SecurityIncident.fromCSV(line));
            }
        } catch (IOException e) {
            System.err.println("Error reading security incidents: " + e.getMessage());
        }
        return incidents;
    }

    public void addIncident(SecurityIncident incident) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(incident.toCSV());
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing security incident: " + e.getMessage());
        }
    }

    public void updateIncidents(List<SecurityIncident> incidents) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write("incidentID,type,description,location,timestamp,status,comments\n"); // CSV header
            for (SecurityIncident incident : incidents) {
                writer.write(incident.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error updating security incidents: " + e.getMessage());
        }
    }
}