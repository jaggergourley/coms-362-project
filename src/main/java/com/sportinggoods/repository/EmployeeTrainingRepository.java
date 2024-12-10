package com.sportinggoods.repository;

import com.sportinggoods.model.EmployeeTraining;
import com.sportinggoods.util.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class EmployeeTrainingRepository {
    private final String filePath = "data/employeeTrainings.csv";
    private final String header = "assignmentId,employeeId,programId,status,deadline,storeId";

    public EmployeeTrainingRepository() {
        // Initialize the CSV file with headers if it doesn't exist
        FileUtils.initializeFile(filePath, header);
    }

    public boolean addEmployeeTraining(EmployeeTraining training) {
        if (getEmployeeTrainingById(training.getAssignmentId()) != null) {
            return false; // Assignment ID already exists
        }
        return FileUtils.appendToFile(filePath, training.toCSV());
    }

    public boolean updateEmployeeTraining(EmployeeTraining updatedTraining) {
        List<String> lines = FileUtils.readAllLines(filePath);
        List<String> updatedLines = new ArrayList<>();
        boolean found = false;

        updatedLines.add(header); // Add header back

        for (String line : lines) {
            EmployeeTraining training = EmployeeTraining.fromCSV(line);
            if (training != null) {
                if (training.getAssignmentId() == updatedTraining.getAssignmentId()) {
                    updatedLines.add(updatedTraining.toCSV());
                    found = true;
                } else {
                    updatedLines.add(line);
                }
            }
        }

        if (found) {
            return FileUtils.writeAllLines(filePath, updatedLines);
        }
        return false;
    }

    public EmployeeTraining getEmployeeTrainingById(int assignmentId) {
        List<String> lines = FileUtils.readAllLines(filePath);
        for (String line : lines) {
            EmployeeTraining training = EmployeeTraining.fromCSV(line);
            if (training != null && training.getAssignmentId() == assignmentId) {
                return training;
            }
        }
        return null;
    }

    public List<EmployeeTraining> getAllEmployeeTrainings() {
        List<EmployeeTraining> trainings = new ArrayList<>();
        List<String> lines = FileUtils.readAllLines(filePath);
        for (String line : lines) {
            EmployeeTraining training = EmployeeTraining.fromCSV(line);
            if (training != null) {
                trainings.add(training);
            }
        }
        return trainings;
    }

    public boolean removeEmployeeTraining(int assignmentId) {
        List<String> lines = FileUtils.readAllLines(filePath);
        List<String> updatedLines = new ArrayList<>();
        boolean found = false;

        updatedLines.add(header); // Add header back

        for (String line : lines) {
            EmployeeTraining training = EmployeeTraining.fromCSV(line);
            if (training != null) {
                if (training.getAssignmentId() == assignmentId) {
                    found = true; // Skip this training to remove it
                } else {
                    updatedLines.add(line);
                }
            }
        }

        if (found) {
            return FileUtils.writeAllLines(filePath, updatedLines);
        }
        return false;
    }
}
