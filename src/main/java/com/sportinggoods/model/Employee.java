package com.sportinggoods.model;

public class Employee {
    protected int id;
    protected String name;
    private Schedule workSchedule;
    private String position;
    private String department;
    private int storeID;

    public Employee() {}

    // Constructor
    public Employee(String name, int id, Schedule workSchedule, int storeID) {
        this.name = name;
        this.id = id;
        this.workSchedule = workSchedule;
        this.storeID = storeID;
    }

    // Constructor with position and department
    public Employee(String name, int id, Schedule workSchedule, int storeID, String position, String department) {
        this.name = name;
        this.id = id;
        this.workSchedule = workSchedule;
        this.storeID = storeID;
        this.position = position;
        this.department = department;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Schedule getWorkSchedule() {
        return workSchedule;
    }

    public void setWorkSchedule(Schedule workSchedule) {
        this.workSchedule = workSchedule;
    }

    public int getStoreId() {
        return storeID;
    }

    public void setStoreId(int newID) {
        this.storeID = newID;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String toCSV() {
        return id + "," + name + "," + storeID + "," + position + "," + department;
    }

    public static Employee fromCSV(String csvLine) {
        String[] tokens = csvLine.split(",", -1); // Allow empty tokens for missing values
        if (tokens.length < 5) {
            return null; // Invalid format
        }
        int id = Integer.parseInt(tokens[0]);
        String name = tokens[1];
        int storeID = Integer.parseInt(tokens[2]);
        String position = tokens[3];
        String department = tokens[4];

        Employee employee = new Employee(name, id, null, storeID);
        employee.setPosition(position);
        employee.setDepartment(department);
        return employee;
    }
}
