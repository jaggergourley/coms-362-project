package com.sportinggoods.controller;

import com.sportinggoods.model.EmployeeTraining;
import com.sportinggoods.repository.EmployeeTrainingRepository;

import java.util.List;
import java.util.Optional;

public class EmployeeTrainingController {
    private EmployeeTrainingRepository employeeTrainingRepo;

    public EmployeeTrainingController(EmployeeTrainingRepository employeeTrainingRepo) {
        this.employeeTrainingRepo = employeeTrainingRepo;
    }

    /**
     * Assigns a training program to an employee.
     *
     * @param assignmentId Unique ID for the training assignment.
     * @param employeeId   ID of the employee.
     * @param programId    ID of the training program.
     * @param status       Status of the assignment (e.g., "Assigned").
     * @param deadline     Deadline for the training program.
     * @param storeId      ID of the store the employee belongs to.
     * @return True if the training is assigned successfully, false otherwise.
     */
    public boolean assignTrainingToEmployee(int assignmentId, int employeeId, int programId, String status, String deadline, int storeId) {
        EmployeeTraining training = new EmployeeTraining(assignmentId, employeeId, programId, status, deadline, storeId);

        // Check if the training program is already full
        long assignedCount = getAllEmployeeTrainings().stream()
                .filter(t -> t.getProgramId() == programId)
                .count();

        // Optional logic to check training program capacity
        // Assume we have access to a method getTrainingProgramCapacity(programId)
        int capacity = getTrainingProgramCapacity(programId); // Mock this logic for now
        if (assignedCount >= capacity) {
            System.out.println("Training program is full. Cannot assign more employees.");
            return false;
        }

        return employeeTrainingRepo.addEmployeeTraining(training);
    }

    /**
     * Updates the status of an employee's training assignment.
     *
     * @param assignmentId Unique ID for the training assignment.
     * @param status       New status of the training (e.g., "In Progress").
     * @return True if the status is updated successfully, false otherwise.
     */
    public boolean updateTrainingStatus(int assignmentId, String status) {
        EmployeeTraining training = employeeTrainingRepo.getEmployeeTrainingById(assignmentId);
        if (training != null) {
            training.setStatus(status);
            return employeeTrainingRepo.updateEmployeeTraining(training);
        }
        return false;
    }

    /**
     * Retrieves an employee's training assignment by its ID.
     *
     * @param assignmentId The ID of the assignment.
     * @return The EmployeeTraining object if found, null otherwise.
     */
    public EmployeeTraining getEmployeeTrainingById(int assignmentId) {
        return employeeTrainingRepo.getEmployeeTrainingById(assignmentId);
    }

    /**
     * Retrieves all training assignments.
     *
     * @return A list of all training assignments.
     */
    public List<EmployeeTraining> getAllEmployeeTrainings() {
        return employeeTrainingRepo.getAllEmployeeTrainings();
    }

    /**
     * Removes a training assignment.
     *
     * @param assignmentId The ID of the assignment to remove.
     * @return True if the assignment is removed successfully, false otherwise.
     */
    public boolean removeTrainingAssignment(int assignmentId) {
        return employeeTrainingRepo.removeEmployeeTraining(assignmentId);
    }

    /**
     * Mock function to get the capacity of a training program.
     *
     * @param programId The ID of the training program.
     * @return The capacity of the training program.
     */
    private int getTrainingProgramCapacity(int programId) {
        // This should be replaced with actual logic to get program capacity
        // Example: TrainingProgramController.getTrainingProgramById(programId).getCapacity();
        return 10; // Default capacity for demonstration
    }
}
