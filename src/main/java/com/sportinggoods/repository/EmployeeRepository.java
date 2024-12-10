package com.sportinggoods.repository;

import com.sportinggoods.model.Employee;
import com.sportinggoods.util.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class EmployeeRepository {
    private final String filePath = "data/employees.csv";
    private final String header = "id,name,storeID,position,department";

    public EmployeeRepository() {
        FileUtils.initializeFile(filePath, header);
    }

    public boolean addEmployee(Employee employee) {
        if (getEmployeeById(employee.getId()) != null) {
            return false; // Employee ID already exists
        }
        return FileUtils.appendToFile(filePath, employee.toCSV());
    }

    public boolean updateEmployee(Employee updatedEmployee) {
        List<String> lines = FileUtils.readAllLines(filePath);
        List<String> updatedLines = new ArrayList<>();
        boolean found = false;

        updatedLines.add(header);

        for (String line : lines) {
            Employee employee = Employee.fromCSV(line);
            if (employee != null) {
                if (employee.getId() == updatedEmployee.getId()) {
                    updatedLines.add(updatedEmployee.toCSV());
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

    public Employee getEmployeeById(int employeeId) {
        List<String> lines = FileUtils.readAllLines(filePath);
        for (String line : lines) {
            Employee employee = Employee.fromCSV(line);
            if (employee != null && employee.getId() == employeeId) {
                return employee;
            }
        }
        return null;
    }

    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        List<String> lines = FileUtils.readAllLines(filePath);
        for (String line : lines) {
            Employee employee = Employee.fromCSV(line);
            if (employee != null) {
                employees.add(employee);
            }
        }
        return employees;
    }

    public boolean removeEmployee(int employeeId) {
        List<String> lines = FileUtils.readAllLines(filePath);
        List<String> updatedLines = new ArrayList<>();
        boolean found = false;

        updatedLines.add(header);

        for (String line : lines) {
            Employee employee = Employee.fromCSV(line);
            if (employee != null) {
                if (employee.getId() == employeeId) {
                    found = true; // Skip this employee to remove them
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
