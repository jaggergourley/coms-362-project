package com.sportinggoods.model;

public class financialManger {
    String name;
    Finance f;

    public financialManger(String name){
        this.name = name;
        // f is pulled from csv
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

    public void addLog(String log){
        f.getTransactionLog().add(log);
    }

    public void removeLog(int logNum){
        f.getTransactionLog().remove(logNum);
    }

    public void updateLog(String newLog, int logNum){
        f.getTransactionLog().set(logNum, newLog);
    }
}
