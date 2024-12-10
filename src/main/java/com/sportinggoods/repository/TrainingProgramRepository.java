package com.sportinggoods.repository;

import com.sportinggoods.model.TrainingProgram;
import com.sportinggoods.util.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class TrainingProgramRepository {
    private final String filePath = "data/trainingPrograms.csv";
    private final String header = "programId,title,description,capacity";

    public TrainingProgramRepository() {
        // Initialize the CSV file with headers if it doesn't exist
        FileUtils.initializeFile(filePath, header);
    }

    public boolean addTrainingProgram(TrainingProgram program) {
        if (getTrainingProgramById(program.getProgramId()) != null) {
            return false; // Program ID already exists
        }
        return FileUtils.appendToFile(filePath, program.toCSV());
    }

    public boolean updateTrainingProgram(TrainingProgram updatedProgram) {
        List<String> lines = FileUtils.readAllLines(filePath);
        List<String> updatedLines = new ArrayList<>();
        boolean found = false;

        updatedLines.add(header); // Add header back

        for (String line : lines) {
            TrainingProgram program = TrainingProgram.fromCSV(line);
            if (program != null) {
                if (program.getProgramId() == updatedProgram.getProgramId()) {
                    updatedLines.add(updatedProgram.toCSV());
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

    public TrainingProgram getTrainingProgramById(int programId) {
        List<String> lines = FileUtils.readAllLines(filePath);
        for (String line : lines) {
            TrainingProgram program = TrainingProgram.fromCSV(line);
            if (program != null && program.getProgramId() == programId) {
                return program;
            }
        }
        return null;
    }

    public List<TrainingProgram> getAllTrainingPrograms() {
        List<TrainingProgram> programs = new ArrayList<>();
        List<String> lines = FileUtils.readAllLines(filePath);
        for (String line : lines) {
            TrainingProgram program = TrainingProgram.fromCSV(line);
            if (program != null) {
                programs.add(program);
            }
        }
        return programs;
    }

    public boolean removeTrainingProgram(int programId) {
        List<String> lines = FileUtils.readAllLines(filePath);
        List<String> updatedLines = new ArrayList<>();
        boolean found = false;

        updatedLines.add(header); // Add header back

        for (String line : lines) {
            TrainingProgram program = TrainingProgram.fromCSV(line);
            if (program != null) {
                if (program.getProgramId() == programId) {
                    found = true; // Skip this program to remove it
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
