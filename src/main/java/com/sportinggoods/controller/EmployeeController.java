package com.sportinggoods.controller;

import com.sportinggoods.model.Employee;
import com.sportinggoods.repository.EmployeeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeeController {
    private EmployeeRepository employeeRepo;

    public EmployeeController(EmployeeRepository employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    public boolean addEmployee(String name, int id, int storeID, String position, String department) {
        Employee employee = new Employee(name, id, null, storeID);
        employee.setPosition(position);
        employee.setDepartment(department);
        return employeeRepo.addEmployee(employee);
    }

    public boolean updateEmployeePosition(int storeId) {
        // Expanded predefined lists for positions and departments
        List<String> validPositions = List.of(
                "Cashier", "Manager", "Stocker", "HR", "Maintenance", "Sales Associate",
                "Department Specialist", "Inventory Coordinator", "Visual Merchandiser",
                "Technician", "Delivery Personnel", "Warehouse Associate"
        );

        List<String> validDepartments = List.of(
                "Sports", "Fitness", "Outdoor", "Apparel", "Customer Service",
                "Equipment", "Footwear", "Accessories", "Maintenance"
        );

        // Step 1: Get employees for the specified store
        List<Employee> employees = getEmployeesByStoreId(storeId);
        if (employees.isEmpty()) {
            System.out.println("No employees found for this store.");
            return false;
        }

        System.out.println("Employee List for Store ID " + storeId + ":");
        for (Employee employee : employees) {
            System.out.println("ID: " + employee.getId() + ", Name: " + employee.getName() +
                    ", Position: " + employee.getPosition() + ", Department: " + employee.getDepartment());
        }

        // Step 2: Allow user to select an employee or exit
        System.out.print("\nEnter the Employee ID to update (or type 'exit' to go back): ");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim();

        if (input.equalsIgnoreCase("exit")) {
            System.out.println("Exiting employee update.");
            return false;
        }

        int employeeId;
        try {
            employeeId = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format. Please enter a numeric ID.");
            return false;
        }

        Employee employee = employees.stream()
                .filter(e -> e.getId() == employeeId)
                .findFirst()
                .orElse(null);

        if (employee == null) {
            System.out.println("No employee found with the given ID in this store.");
            return false;
        }

        // Step 3: Show valid positions and departments
        System.out.println("\nValid Positions:");
        for (String position : validPositions) {
            System.out.println("- " + position);
        }

        System.out.println("\nValid Departments:");
        for (String department : validDepartments) {
            System.out.println("- " + department);
        }

        // Step 4: Input new position details
        System.out.print("\nEnter the new position: ");
        String newPosition = scanner.nextLine().trim();
        if (!validPositions.contains(newPosition)) {
            System.out.println("Invalid position. Please choose from the valid positions listed above.");
            return false;
        }

        System.out.print("Enter the new department: ");
        String newDepartment = scanner.nextLine().trim();
        if (!validDepartments.contains(newDepartment)) {
            System.out.println("Invalid department. Please choose from the valid departments listed above.");
            return false;
        }

        // Step 5: Update employee position and department
        employee.setPosition(newPosition);
        employee.setDepartment(newDepartment);
        boolean success = employeeRepo.updateEmployee(employee);

        if (success) {
            System.out.println("Employee position and department updated successfully!");
        } else {
            System.out.println("Failed to update the position and department. Please try again later.");
        }
        return success;
    }



    public List<Employee> getAllEmployees() {
        return employeeRepo.getAllEmployees();
    }

    public List<Employee> getEmployeesByStoreId(int storeId) {
        List<Employee> allEmployees = employeeRepo.getAllEmployees();
        List<Employee> filteredEmployees = new ArrayList<>();
        for (Employee employee : allEmployees) {
            if (employee.getStoreId() == storeId) {
                filteredEmployees.add(employee);
            }
        }
        return filteredEmployees;
    }

    public boolean removeEmployee(int employeeId) {
        return employeeRepo.removeEmployee(employeeId);
    }
}
