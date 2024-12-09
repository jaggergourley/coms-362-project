package com.sportinggoods.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegionalManger extends Employee {

    protected int id;
    protected String name;
    private boolean placeOrder;
    private boolean addingItems;
    private boolean makeSchedule;
    private Schedule workSchedule;
    private int numStores;
    private StoreList storeList;
    



    public RegionalManger(int id, String name, boolean placeOrder,
    boolean addingItems, boolean makeschedual, Schedule workSchedule){
        this.id = id;
        this.name = name;
        this.placeOrder = placeOrder;
        this.addingItems = addingItems;
        this.makeSchedule = makeschedual;
        this.workSchedule = workSchedule;
        this.storeList = new StoreList();
        this.numStores = this.storeList.getStoreCount();
        
        
    }

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

    public boolean isPlaceOrder() {
        return placeOrder;
    }

    public void setPlaceOrder(boolean placeOrder) {
        this.placeOrder = placeOrder;
    }

    public boolean isAddingItems() {
        return addingItems;
    }

    public void setAddingItems(boolean addingItems) {
        this.addingItems = addingItems;
    }

    public boolean isMakeSchedule() {
        return makeSchedule;
    }

    public void setMakeSchedule(boolean makeSchedule) {
        this.makeSchedule = makeSchedule;
    }

    public Schedule getWorkSchedule() {
        return workSchedule;
    }

    public void setWorkSchedule(Schedule workSchedule) {
        this.workSchedule = workSchedule;
    }

    public int getNumStores() {
        return numStores;
    }

    public void setNumStores(int numStores) {
        this.numStores = numStores;
    }

    public StoreList getStoreList(){
        return storeList;
    }
}
