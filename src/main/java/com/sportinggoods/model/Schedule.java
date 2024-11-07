package src.main.java.com.sportinggoods.model;

import java.util.ArrayList;

public class Schedule {
    ArrayList<shift> workSchedual = new ArrayList<shift>();

    public Schedule() {
        workSchedual = loadShiftsFromFile();
    }

    private ArrayList<shift> loadShiftsFromFile(){ // gets the current list of shifts from csv file
        return null;
    }

    private void addShift(shift s){
        // adds a shift to the schedual
    }
    
    private void deleteShift(shift s){
        // removes a shift from the schedual
    }
         

}
