package com.sportinggoods.model;


public class Employee{
    protected int id;
    protected String name;
    private boolean placeOrder;
    private boolean addingItems;
    private boolean makeSchedule;
    private Schedule workSchedule;


    public Employee(){}

    //Constructor
    public Employee(String name, int id, Schedule workSchedule) {
        this.name = name;
        this.id = id;
        this.workSchedule = workSchedule;
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


}
