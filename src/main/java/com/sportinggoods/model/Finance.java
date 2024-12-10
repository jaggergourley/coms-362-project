package com.sportinggoods.model;

import java.util.ArrayList;

public class Finance { //should only have 1 stored in csv
    int revenue;
    int costs;
    int budgetCelling;
    int revenueFloor;
    ArrayList<String> transactionLog;

    public Finance(){
        revenue = 0;
        costs = 0;
        budgetCelling = 50000;
        revenueFloor = 20000;
        transactionLog = new ArrayList<>(); // pull from csv
    }

    public int getRevenue() {
        return revenue;
    }
    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }
    public int getBudgetCelling() {
        return budgetCelling;
    }
    public void setBudgetCelling(int budgetCelling) {
        this.budgetCelling = budgetCelling;
    }
    public int getRevenueFloor() {
        return revenueFloor;
    }
    public void setRevenueFloor(int revenueFloor) {
        this.revenueFloor = revenueFloor;
    }
    public ArrayList<String> getTransactionLog() {
        return transactionLog;
    }

    public int getCosts(){
        return costs;
    }

    public void setCosts(int newCosts){
        costs = newCosts;
    }


    public void setTransactionLog(ArrayList<String> transactionLog) {
        this.transactionLog = transactionLog;
    }

    public void addTransactionLog(String log){
        transactionLog.add(log);
    }

    public void removeTransactionLog(int logNum){
        transactionLog.remove(logNum);
    }
}
