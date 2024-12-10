package com.sportinggoods.controller;

import com.sportinggoods.model.Employee;
import com.sportinggoods.model.EmployeeTraining;
import com.sportinggoods.model.TrainingProgram;
import com.sportinggoods.repository.EmployeeTrainingRepository;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class EmployeeTrainingController {
    private EmployeeTrainingRepository employeeTrainingRepo;
    private TrainingProgramController trainingProgramController;
    private EmployeeController employeeController;
    private Scanner scanner;

    public EmployeeTrainingController(EmployeeTrainingRepository employeeTrainingRepo,
                                      TrainingProgramController trainingProgramController,
                                      EmployeeController employeeController,
                                      Scanner scanner) {
        this.employeeTrainingRepo = employeeTrainingRepo;
        this.trainingProgramController = trainingProgramController;
        this.employeeController = employeeController;
        this.scanner = scanner;
    }

    // Assign a training program to an employee
    public void assignTrainingToEmployee(int storeId) {
        System.out.println("\nAssign Training to Employee");

        // List employees in the current store
        List<Employee> employees = employeeController.getEmployeesByStoreId(storeId);
        if (employees.isEmpty()) {
            System.out.println("No employees found for this store.");
            return;
        }

        System.out.println("\nEmployees in Store " + storeId + ":");
        employees.forEach(employee -> System.out.println("ID: " + employee.getId() + ", Name: " + employee.getName()));

        // Get employee selection
        System.out.print("\nEnter the Employee ID to assign: ");
        int employeeId = Integer.parseInt(scanner.nextLine());

        // Verify employee exists in this store
        Employee selectedEmployee = employees.stream()
                .filter(employee -> employee.getId() == employeeId)
                .findFirst()
                .orElse(null);

        if (selectedEmployee == null) {
            System.out.println("Invalid Employee ID. Please select a valid employee.");
            return;
        }

        // List available training programs
        List<TrainingProgram> programs = trainingProgramController.getAllTrainingPrograms();
        if (programs.isEmpty()) {
            System.out.println("No training programs available.");
            return;
        }

        System.out.println("\nAvailable Training Programs:");
        programs.forEach(program -> System.out.println("ID: " + program.getProgramId() +
                ", Title: " + program.getTitle() +
                ", Capacity: " + program.getCapacity()));

        // Get program selection
        System.out.print("\nEnter the ID of the training program: ");
        int programId = Integer.parseInt(scanner.nextLine());

        TrainingProgram selectedProgram = trainingProgramController.getTrainingProgramById(programId);
        if (selectedProgram == null) {
            System.out.println("Invalid program ID.");
            return;
        }

        // Check program capacity
        long assignedCount = getAllEmployeeTrainingsByProgramId(programId).size();
        if (assignedCount >= selectedProgram.getCapacity()) {
            System.out.println("The selected training program is full.");
            return;
        }

        System.out.print("Enter the assignment deadline (YYYY-MM-DD): ");
        String deadline = scanner.nextLine();

        // Create assignment
        int assignmentId = employeeTrainingRepo.getAllEmployeeTrainings().size() + 1;
        EmployeeTraining training = new EmployeeTraining(assignmentId, employeeId, programId,
                "Assigned", deadline, storeId);

        boolean success = employeeTrainingRepo.addEmployeeTraining(training);
        if (success) {
            System.out.println("Training program assigned successfully.");
        } else {
            System.out.println("Failed to assign training program.");
        }
    }

    // View training assignments by store
    public void viewTrainingAssignmentsByStore(int storeId) {
        System.out.println("\nTraining Assignments for Store " + storeId);
        List<EmployeeTraining> assignments = getAllEmployeeTrainingsByStore(storeId);
        if (assignments.isEmpty()) {
            System.out.println("No training assignments found for this store.");
            return;
        }

        assignments.forEach(training -> System.out.println("Assignment ID: " + training.getAssignmentId() +
                ", Employee ID: " + training.getEmployeeId() +
                ", Program ID: " + training.getProgramId() +
                ", Status: " + training.getStatus() +
                ", Deadline: " + training.getDeadline()));
    }

    // Update training assignment status
    public void updateTrainingStatus() {
        System.out.print("\nEnter the Assignment ID to update: ");
        int assignmentId = Integer.parseInt(scanner.nextLine());

        EmployeeTraining training = employeeTrainingRepo.getEmployeeTrainingById(assignmentId);
        if (training == null) {
            System.out.println("Assignment not found.");
            return;
        }

        System.out.println("Current Status: " + training.getStatus());
        System.out.print("Enter new status (Assigned/In Progress/Completed): ");
        String status = scanner.nextLine();

        training.setStatus(status);
        boolean success = employeeTrainingRepo.updateEmployeeTraining(training);
        if (success) {
            System.out.println("Training assignment updated successfully.");
        } else {
            System.out.println("Failed to update training assignment.");
        }
    }

    // Remove a training assignment
    public void removeTrainingAssignment() {
        System.out.print("\nEnter the Assignment ID to remove: ");
        int assignmentId = Integer.parseInt(scanner.nextLine());

        boolean success = employeeTrainingRepo.removeEmployeeTraining(assignmentId);
        if (success) {
            System.out.println("Training assignment removed successfully.");
        } else {
            System.out.println("Failed to remove training assignment.");
        }
    }

    // Get all training assignments for a store
    public List<EmployeeTraining> getAllEmployeeTrainingsByStore(int storeId) {
        return employeeTrainingRepo.getAllEmployeeTrainings()
                .stream()
                .filter(training -> training.getStoreId() == storeId)
                .collect(Collectors.toList());
    }

    // Get all training assignments for a program
    public List<EmployeeTraining> getAllEmployeeTrainingsByProgramId(int programId) {
        return employeeTrainingRepo.getAllEmployeeTrainings()
                .stream()
                .filter(training -> training.getProgramId() == programId)
                .collect(Collectors.toList());
    }

    // Allow employees to self-assign to a training program
    public void assignTrainingToEmployeeForEmployee(int storeId, int employeeId) {
        System.out.println("\nJoin Training Program");

        // List available training programs
        List<TrainingProgram> programs = trainingProgramController.getAllTrainingPrograms();
        if (programs.isEmpty()) {
            System.out.println("No training programs available.");
            return;
        }

        System.out.println("\nAvailable Training Programs:");
        programs.forEach(program -> System.out.println("ID: " + program.getProgramId() +
                ", Title: " + program.getTitle() +
                ", Capacity: " + program.getCapacity()));

        // Get program selection
        System.out.print("\nEnter the ID of the training program: ");
        int programId = Integer.parseInt(scanner.nextLine());

        TrainingProgram selectedProgram = trainingProgramController.getTrainingProgramById(programId);
        if (selectedProgram == null) {
            System.out.println("Invalid program ID.");
            return;
        }

        // Check program capacity
        long assignedCount = getAllEmployeeTrainingsByProgramId(programId).size();
        if (assignedCount >= selectedProgram.getCapacity()) {
            System.out.println("The selected training program is full.");
            return;
        }

        System.out.print("Enter the assignment deadline (YYYY-MM-DD): ");
        String deadline = scanner.nextLine();

        // Create assignment
        int assignmentId = employeeTrainingRepo.getAllEmployeeTrainings().size() + 1;
        EmployeeTraining training = new EmployeeTraining(assignmentId, employeeId, programId,
                "Assigned", deadline, storeId);

        boolean success = employeeTrainingRepo.addEmployeeTraining(training);
        if (success) {
            System.out.println("You have successfully joined the training program.");
        } else {
            System.out.println("Failed to join the training program.");
        }
    }

    // View training assignments for a specific employee
    public void viewTrainingAssignmentsByEmployee(int employeeId) {
        System.out.println("\nYour Training Assignments:");
        List<EmployeeTraining> assignments = employeeTrainingRepo.getAllEmployeeTrainings()
                .stream()
                .filter(training -> training.getEmployeeId() == employeeId)
                .collect(Collectors.toList());

        if (assignments.isEmpty()) {
            System.out.println("You have no training assignments.");
            return;
        }

        assignments.forEach(training -> System.out.println("Assignment ID: " + training.getAssignmentId() +
                ", Program ID: " + training.getProgramId() +
                ", Status: " + training.getStatus() +
                ", Deadline: " + training.getDeadline()));
    }
}
