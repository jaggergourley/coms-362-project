package com.sportinggoods.model;

import com.sportinggoods.repository.EmployeeRepository;

import java.util.ArrayList;
import java.util.List;

public class EmployeeList {
    private int storeId;
    private List<Employee> employees;

    public EmployeeList(int storeId) {
        this.storeId = storeId;
        this.employees = loadEmployeesByStore(storeId);
    }

    private List<Employee> loadEmployeesByStore(int storeId) {
        EmployeeRepository repository = new EmployeeRepository(); // Singleton or DI could improve this.
        List<Employee> allEmployees = repository.getAllEmployees();
        List<Employee> storeEmployees = new ArrayList<>();
        for (Employee employee : allEmployees) {
            if (employee.getStoreId() == storeId) {
                storeEmployees.add(employee);
            }
        }
        return storeEmployees;
    }

    public List<Employee> getEmployees() {
        return new ArrayList<>(employees);
    }

    public boolean addEmployee(Employee employee) {
        if (employee.getStoreId() != storeId) {
            System.out.println("Employee store ID mismatch. Cannot add to this store.");
            return false;
        }

        EmployeeRepository repository = new EmployeeRepository();
        if (repository.addEmployee(employee)) {
            employees.add(employee);
            return true;
        }
        return false;
    }

    public boolean removeEmployee(int employeeId) {
        Employee toRemove = employees.stream()
                .filter(emp -> emp.getId() == employeeId)
                .findFirst()
                .orElse(null);

        if (toRemove != null) {
            EmployeeRepository repository = new EmployeeRepository();
            if (repository.removeEmployee(employeeId)) {
                employees.remove(toRemove);
                return true;
            }
        }
        return false;
    }

    public void printEmployeeList() {
        if (employees.isEmpty()) {
            System.out.println("No employees found for this store.");
        } else {
            for (Employee e : employees) {
                System.out.println("ID: " + e.getId() + ", Name: " + e.getName()
                        + ", Position: " + e.getPosition() + ", Department: " + e.getDepartment());
            }
        }
    }
}
