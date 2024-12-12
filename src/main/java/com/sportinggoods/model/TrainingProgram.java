package com.sportinggoods.model;

public class TrainingProgram {
    private int programId;
    private String title;
    private String description;
    private int capacity;

    // Constructors
    public TrainingProgram() {}

    public TrainingProgram(int programId, String title, String description, int capacity) {
        this.programId = programId;
        this.title = title;
        this.description = description;
        this.capacity = capacity;
    }

    // Getters and Setters
    public int getProgramId() {
        return programId;
    }

    public void setProgramId(int programId) {
        this.programId = programId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    // CSV Serialization
    public String toCSV() {
        return programId + "," + title + "," + description + "," + capacity;
    }

    // CSV Deserialization
    public static TrainingProgram fromCSV(String csvLine) {
        String[] tokens = csvLine.split(",", -1); // Allow empty fields
        if (tokens.length < 4) {
            return null; // Invalid format
        }
        int programId = Integer.parseInt(tokens[0]);
        String title = tokens[1];
        String description = tokens[2];
        int capacity = Integer.parseInt(tokens[3]);
        return new TrainingProgram(programId, title, description, capacity);
    }
}
