package com.sportinggoods.model;

public class EmployeeTraining {
    private int assignmentId;
    private int employeeId;
    private int programId;
    private String status; // "Assigned", "In Progress", "Completed"
    private String deadline;
    private int storeId;

    // Constructors
    public EmployeeTraining() {}

    public EmployeeTraining(int assignmentId, int employeeId, int programId, String status, String deadline, int storeId) {
        this.assignmentId = assignmentId;
        this.employeeId = employeeId;
        this.programId = programId;
        this.status = status;
        this.deadline = deadline;
        this.storeId = storeId;
    }

    // Getters and Setters
    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getProgramId() {
        return programId;
    }

    public void setProgramId(int programId) {
        this.programId = programId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    // CSV Serialization
    public String toCSV() {
        return assignmentId + "," + employeeId + "," + programId + "," + status + "," + deadline + "," + storeId;
    }

    // CSV Deserialization
    public static EmployeeTraining fromCSV(String csvLine) {
        String[] tokens = csvLine.split(",", -1); // Allow empty fields
        if (tokens.length < 6) {
            return null; // Invalid format
        }
        int assignmentId = Integer.parseInt(tokens[0]);
        int employeeId = Integer.parseInt(tokens[1]);
        int programId = Integer.parseInt(tokens[2]);
        String status = tokens[3];
        String deadline = tokens[4];
        int storeId = Integer.parseInt(tokens[5]);
        return new EmployeeTraining(assignmentId, employeeId, programId, status, deadline, storeId);
    }
}
