package com.sportinggoods.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Store {

    private int ID;

    private String address;

    private EmployeeList Employees;

    private Inventory storeInventory;

    private String employeeFile = "data/employees.csv";


    public Store(int ID, String address){
        this.ID = ID;
        this.address = address;
        Employees = new EmployeeList(ID);
        storeInventory = new Inventory(ID);
    }


    public int getID() {
        return ID;
    }


    public void setID(int iD) {
        ID = iD;
    }


    public String getAddress() {
        return address;
    }


    public void setAddress(String address) {
        this.address = address;
    }


    public EmployeeList getEmployees() {
        return Employees;
    }


    public void setEmployees(EmployeeList employees) {
        Employees = employees;
    }


    public Inventory getStoreInventory() {
        return storeInventory;
    }


    public void setStoreInventory(Inventory storeInventory) {
        this.storeInventory = storeInventory;
    }

    public void addEmployee(Employee employee){
        Employees.getEmployees().add(employee);
    }

    public void removeEmployee(Employee employee){
        Employees.getEmployees().remove(employee);
    }

    public void updateEmployee(int id, Employee employee){
        for(Employee temp : Employees.getEmployees()){
            if(temp.getId() == id){
                temp.setId(employee.getId());
                temp.setName(employee.getName());
                temp.setWorkSchedule(employee.getWorkSchedule());
            }
        }
    }

    public ArrayList<Employee> loadEmployeeFromFile() {
        ArrayList<Employee> loadedEmplyees = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(employeeFile))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                Employee employee = Employee.fromCSV(line);
                if (employee != null && employee.getId() == ID) {
                    loadedEmplyees.add(employee);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading inventory from file: " + e.getMessage());
        }
        return loadedEmplyees;
    }


    public String toCSV() {
        return ID + "," + address;
    }

    public static Store fromCSV(String csvLine) {
        String[] tokens = csvLine.split(",");
        if (tokens.length != 2) {
            return null;  // Invalid format
        }
        int id = Integer.parseInt(tokens[0]);
        String address = tokens[1];
      
        return new Store(id, address);
    }
    
}
