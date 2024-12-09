package com.sportinggoods.model;


public class Employee{
    protected int id;
    protected String name;
    private boolean placeOrder;
    private boolean addingItems;
    private boolean makeSchedule;
    private Schedule workSchedule;
    private int storeID;


    public Employee(){}

    //Constructor
    public Employee(String name, int id, Schedule workSchedule, int storeID) {
        this.name = name;
        this.id = id;
        this.workSchedule = workSchedule;
        this.storeID = storeID;
    }

    //getters and setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName(){
        return name;
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


    //Functions that all objects that are employees have
    public Boolean canPlaceOrder() {
        //Insert logic here

        return placeOrder;
    }

    public Boolean canAddItems() {
        //insert logic here

        return addingItems;
    }

    public Boolean canMakeSchedule() {
        // insert logic here

        return makeSchedule;
    }

    public static Employee fromCSV(String csvLine) {
        String[] tokens = csvLine.split(",");
        if (tokens.length != 3) {
            return null;  // Invalid format
        }
        int id = Integer.parseInt(tokens[0]);
        String name = tokens[1];
        int storeID = Integer.parseInt(tokens[2]);
        return new Employee(name, id, null, storeID);
    }

    public String toCSV() {
        return id + "," + name + "," + storeID;
    }


}