package com.sportinggoods.model;

public class budgetManager {
    String name;
    Finance f;

    public budgetManager(String name) {
        this.name = name;
        // f should be pulled from csv
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Finance getF() {
        return f;
    }

    public void setF(Finance f) {
        this.f = f;
    }

    public void updateCostsBudget(int newCosts){
        f.setCosts(newCosts);
    }

    public void updateBudgetCelling(int newCelling){
        f.setBudgetCelling(newCelling);
    }

    public boolean auditCostsBudget(){
        if(f.getCosts() > f.getBudgetCelling()){
            return false;
        }
        else {
            return true;
        }
    }

    public boolean auditRevenueBudget(){
        if(f.getRevenue() < f.getRevenueFloor()){
            return false;
        }
        else{
            return true;
        }
    }
}
