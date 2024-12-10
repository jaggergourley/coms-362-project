package com.sportinggoods.controller;

import com.sportinggoods.model.TrainingProgram;
import com.sportinggoods.repository.TrainingProgramRepository;

import java.util.List;
import java.util.Scanner;

public class TrainingProgramController {
    private TrainingProgramRepository trainingProgramRepo;
    private Scanner scanner;

    public TrainingProgramController(TrainingProgramRepository trainingProgramRepo, Scanner scanner) {
        this.trainingProgramRepo = trainingProgramRepo;
        this.scanner = scanner;
    }

    public void addTrainingProgram() {
        System.out.print("Enter Program ID: ");
        int programId = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter Program Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Program Description: ");
        String description = scanner.nextLine();
        System.out.print("Enter Program Capacity: ");
        int capacity = Integer.parseInt(scanner.nextLine());

        boolean added = trainingProgramRepo.addTrainingProgram(new TrainingProgram(programId, title, description, capacity));
        if (added) {
            System.out.println("Training program added successfully!");
        } else {
            System.out.println("Failed to add training program. Program ID may already exist.");
        }
    }

    public void viewAllTrainingPrograms() {
        System.out.println("\nTraining Programs:");
        List<TrainingProgram> programs = trainingProgramRepo.getAllTrainingPrograms();
        if (programs.isEmpty()) {
            System.out.println("No training programs found.");
        } else {
            for (TrainingProgram program : programs) {
                System.out.println("ID: " + program.getProgramId() +
                        ", Title: " + program.getTitle() +
                        ", Description: " + program.getDescription() +
                        ", Capacity: " + program.getCapacity());
            }
        }
    }

    public void updateTrainingProgram() {
        System.out.print("Enter Program ID to update: ");
        int programId = Integer.parseInt(scanner.nextLine());

        TrainingProgram existingProgram = trainingProgramRepo.getTrainingProgramById(programId);
        if (existingProgram == null) {
            System.out.println("Training program not found.");
            return;
        }

        System.out.println("Updating Program: " + existingProgram.getTitle());
        System.out.print("Enter new Title (or press Enter to keep the current): ");
        String title = scanner.nextLine();
        if (title.isEmpty()) title = existingProgram.getTitle();

        System.out.print("Enter new Description (or press Enter to keep the current): ");
        String description = scanner.nextLine();
        if (description.isEmpty()) description = existingProgram.getDescription();

        System.out.print("Enter new Capacity (or press Enter to keep the current): ");
        String capacityInput = scanner.nextLine();
        int capacity = capacityInput.isEmpty() ? existingProgram.getCapacity() : Integer.parseInt(capacityInput);

        boolean updated = trainingProgramRepo.updateTrainingProgram(new TrainingProgram(programId, title, description, capacity));
        if (updated) {
            System.out.println("Training program updated successfully!");
        } else {
            System.out.println("Failed to update training program.");
        }
    }

    public void removeTrainingProgram() {
        System.out.print("Enter Program ID to remove: ");
        int programId = Integer.parseInt(scanner.nextLine());

        boolean removed = trainingProgramRepo.removeTrainingProgram(programId);
        if (removed) {
            System.out.println("Training program removed successfully!");
        } else {
            System.out.println("Failed to remove training program. Program ID may not exist.");
        }
    }
}
