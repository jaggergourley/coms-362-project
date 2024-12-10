package com.sportinggoods.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EmployeeList {
    private ArrayList<Employee> employees = new ArrayList<>();
    private static final String FILE_PATH = "data/employee.csv";


    public EmployeeList(int storeID){
        employees = loadStoreEmployeesFromFile(storeID);
    }

    public ArrayList<Employee> getEmployees(){
        return employees;
    }

    public void setEmployees(ArrayList<Employee> list){
        employees = list;
    }

    public Employee getEmployee(String employeeName, int storeID){
        Employee result = null;
        for(Employee e : employees){
            if(e.getName().equals(employeeName) && e.getStoreId() == storeID){
                result = e;
            }
        }
        return result;
    }

    public void addEmployee(Employee employee) {
        if(employees.contains(employee) != true){
            employees.add(employee);
        }
        
        try {
            saveEmployeesToFile(); // Save updated list to file
        } catch (Exception e) {
            System.err.println("Error saving items to file: " + e.getMessage());
        }
    }

    public void removeEmployee(String name, int id){
        for(int i = 0; i < employees.size(); i++){
            Employee e = employees.get(i);
            if(e.getName().equals(name) && e.getId() == id){
                employees.remove(i);
            }
        }
        try {
            saveEmployeesToFile(); // Save updated list to file
        } catch (Exception e) {
            System.err.println("Error saving items to file: " + e.getMessage());
        }
    }

    private ArrayList<Employee> loadStoreEmployeesFromFile(int storeID) {
        ArrayList<Employee> loadedEmployees = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                Employee employee = Employee.fromCSV(line);
                if (employee != null && employee.getStoreId() == storeID) {
                    loadedEmployees.add(employee);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading inventory from file: " + e.getMessage());
        }
        return loadedEmployees;
    }
            
            
    public void saveEmployeesToFile() {
        Map<String, Employee> employeeMap = new HashMap<>();

        // Add current store's items to the map (merge quantities)
        for (Employee e : employees) {
            if(employeeMap.containsKey(e.getName()) == false){
                employeeMap.put(e.getName(), e);
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Skip empty lines
                String[] parts = line.split(",");
                if (parts.length < 3) { // Validate structure
                    System.err.println("Error parsing inventory line: " + line);
                    continue;
                }
                
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                int storeID = Integer.parseInt(parts[2]);
                int temp = employees.get(0).getStoreId();

                if(employeeMap.get(name) == null && temp != storeID){
                    employeeMap.put(name, new Employee(name, id, null, storeID));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading inventory file: " + e.getMessage());
        }


        // Write merged inventory back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write("id,name,storeID\n"); // Write CSV header
            for (Employee e : employeeMap.values()) {
                writer.write(e.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving inventory to file: " + e.getMessage());
        }
    }
        
    public void printEmployeeList(){
        for(Employee e : employees){
            System.out.println(e.getName() + " - " + e.getId());
        }
    }
}
