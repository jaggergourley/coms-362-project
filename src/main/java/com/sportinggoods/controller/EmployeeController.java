package com.sportinggoods.controller;

import com.sportinggoods.model.Employee;
import com.sportinggoods.repository.EmployeeRepository;

import java.util.List;

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

    public boolean updateEmployeePosition(int employeeId, String newPosition, String department) {
        Employee employee = employeeRepo.getEmployeeById(employeeId);
        if (employee == null) {
            System.err.println("Employee ID does not exist.");
            return false;
        }

        employee.setPosition(newPosition);
        employee.setDepartment(department);
        return employeeRepo.updateEmployee(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepo.getAllEmployees();
    }

    public boolean removeEmployee(int employeeId) {
        return employeeRepo.removeEmployee(employeeId);
    }
}
