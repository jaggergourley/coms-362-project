package com.sportinggoods.model;

import java.util.ArrayList;

public class RegionalManger extends Employee {

    protected int id;
    protected String name;
    private boolean placeOrder;
    private boolean addingItems;
    private boolean makeSchedule;
    private Schedule workSchedule;
    private int numStores;
    private ArrayList<Store> stores;

    public RegionalManger(int id, String name, boolean placeOrder,
    boolean addingItems, boolean makeschedual, Schedule workSchedule){
        this.id = id;
        this.name = name;
        this.placeOrder = placeOrder;
        this.addingItems = addingItems;
        this.makeSchedule = makeschedual;
        this.workSchedule = workSchedule;
        this.numStores = 0;
        this.stores = new ArrayList<Store>();
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

    public ArrayList<Store> getStores() {
        return stores;
    }

    public void setStores(ArrayList<Store> stores) {
        this.stores = stores;
    }

    public void addStore(String address){
        int id = getNumStores();
        id = id + 1;
        Store s = new Store(id, address);
        stores.add(s);
        setNumStores(id);
    }

    public void removeStore(String address){
        for(Store temp : stores){ //removes store
            if(temp.getAddress() == address){
                stores.remove(temp);
            }
        }

        int count = 1;
        for(Store temp : stores){ // resets ids 
            temp.setID(count);
            count++;
        }
    }

    public void updateStoreAddress(String newAddress){
        for(Store s : stores){
            if(s.getAddress() == newAddress){
                s.setAddress(newAddress);
                break;
            }
        }
    }

    public void printStores(){
        for(Store s : stores){
            System.out.println("Store " + s.getID() + " is at location: " + s.getAddress());
        }
    }
    
}
