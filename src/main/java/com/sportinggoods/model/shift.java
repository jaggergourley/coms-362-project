package com.sportinggoods.model;

public class shift {

    private int date;
    private String startTime;
    private String endTime;

    // Constructor
    public shift(int date, String startTime, String endTime){
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public shift(){
        // read from csv
    }

    public void setdate(int date){
        this.date = date;
    }

    public int getDate(){
        return date;
    }

    public void setStartTime(String startTime){
        this.startTime = startTime;
    }

    public String getStartTime(){
        return startTime;
    }

    public void setEndTime(String endTime){
        this.endTime = endTime;
    }

    public String getEndTime(){
        return endTime;
    }

    public String toCSV() {
        return date + "," + startTime + "," + endTime;
    }

    public static shift fromCSV(String csvLine) {
        String[] tokens = csvLine.split(",");
        if (tokens.length != 3) {
            return null;  // Invalid format
        }
        int date = Integer.parseInt(tokens[0]);
        String startTime = tokens[1];
        String startEnd = tokens[2];
        return new shift(date, startTime, startEnd);
    }
    
}
