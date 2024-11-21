package com.sportinggoods.model;

import java.util.ArrayList;

public class Store {

    private int ID;

    private String address;

    private ArrayList<Employee> Employees;

    private Inventory storeInventory;


    public Store(int ID, String address){
        this.ID = ID;
        this.address = address;
        Employees = new ArrayList<>();
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


    public ArrayList<Employee> getEmployees() {
        return Employees;
    }


    public void setEmployees(ArrayList<Employee> employees) {
        Employees = employees;
    }


    public Inventory getStoreInventory() {
        return storeInventory;
    }


    public void setStoreInventory(Inventory storeInventory) {
        this.storeInventory = storeInventory;
    }

    public void addEmployee(Employee employee){
        Employees.add(employee);
    }

    public void removeEmployee(Employee employee){
        Employees.remove(employee);
    }

    public void updateEmployee(int id, Employee employee){
        for(Employee temp : Employees){
            if(temp.getId() == id){
                temp.setId(employee.getId());
                temp.setName(employee.getName());
                temp.setWorkSchedule(employee.getWorkSchedule());
            }
        }
    }
    
}
