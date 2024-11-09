package com.sportinggoods.model;

public class shift {

    private String day;
    private double startTime;
    private double endTime;

    // Constructor
    public shift(String day, double startTime, double endTime){
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public shift(){
        // read from csv
    }

    public void setDay(String day){
        this.day = day;
    }

    public String getDay(){
        return day;
    }

    public void setStartTime(double startTime){
        this.startTime = startTime;
    }

    public double getStartTime(){
        return startTime;
    }

    public void setEndTime(double endTime){
        this.endTime = endTime;
    }

    public double getEndTime(){
        return endTime;
    }
    
}
