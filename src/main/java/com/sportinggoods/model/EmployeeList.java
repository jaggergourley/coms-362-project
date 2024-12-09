package com.sportinggoods.model;

import com.sportinggoods.controller.EmployeeController;
import com.sportinggoods.repository.EmployeeRepository;

import java.util.ArrayList;
import java.util.List;

public class EmployeeList {
    private List<Employee> employees;
    private EmployeeController employeeController;

    public EmployeeList(int storeID) {
        this.employeeController = new EmployeeController(new EmployeeRepository());
        this.employees = filterEmployeesByStore(storeID);
    }

    private List<Employee> filterEmployeesByStore(int storeID) {
        List<Employee> allEmployees = employeeController.getAllEmployees();
        List<Employee> filteredEmployees = new ArrayList<>();
        for (Employee e : allEmployees) {
            if (e.getStoreId() == storeID) {
                filteredEmployees.add(e);
            }
        }
        return filteredEmployees;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public Employee getEmployee(String name, int storeID) {
        for (Employee e : employees) {
            if (e.getName().equalsIgnoreCase(name) && e.getStoreId() == storeID) {
                return e;
            }
        }
        return null;
    }

    public void addEmployee(Employee employee) {
        if (!employees.contains(employee)) {
            if (employeeController.addEmployee(
                    employee.getName(),
                    employee.getId(),
                    employee.getStoreId(),
                    employee.getPosition(),
                    employee.getDepartment())) {
                employees.add(employee);
                System.out.println("Employee added successfully.");
            } else {
                System.out.println("Failed to add employee. Duplicate ID or other issue.");
            }
        }
    }

    public void removeEmployee(String name, int id) {
        Employee toRemove = getEmployee(name, id);
        if (toRemove != null) {
            if (employeeController.removeEmployee(id)) {
                employees.remove(toRemove);
                System.out.println("Employee removed successfully.");
            } else {
                System.out.println("Failed to remove employee. Employee may not exist.");
            }
        } else {
            System.out.println("Employee not found in the list.");
        }
    }

    public void printEmployeeList() {
        if (employees.isEmpty()) {
            System.out.println("No employees found for this store.");
        } else {
            for (Employee e : employees) {
                System.out.println(e.getName() + " - ID: " + e.getId() + " - Position: " + e.getPosition());
            }
        }
    }
}
