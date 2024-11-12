package src.main.java.com.sportinggoods.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Schedule {
   private ArrayList<shift> workSchedual = new ArrayList<shift>();
   private static final String FILE_PATH = "data/WorkSchedual.csv";

    public Schedule() {
        workSchedual = loadShiftsFromFile();
    }

    public ArrayList<shift> loadShiftsFromFile(){ // gets the current list of shifts from csv file
        ArrayList<shift> loadedItems = new ArrayList<shift>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                shift item = shift.fromCSV(line);
                if (item != null) {
                    loadedItems.add(item);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading inventory from file: " + e.getMessage());
        }
        return loadedItems;
    }

    public void addShift(shift s){
        workSchedual.add(s);
        saveShiftsToFile();
        System.out.println("shift has been added");
    }
    
    public void deleteShift(shift s){
        for(int i = 0; i < workSchedual.size(); i++){
            shift temp = workSchedual.get(i);
            if(temp.getDate() == s.getDate() && temp.getStartTime().matches(s.getStartTime()) == true && temp.getEndTime().matches(s.getEndTime())){
                workSchedual.remove(i);
                saveShiftsToFile();
                System.out.println("Shift removed");
                break;
            }
        }
    }

    public void saveShiftsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write("date,startTime,endTime\n"); // Write CSV header
            for (shift shift : workSchedual) {
                writer.write(shift.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving inventory to file: " + e.getMessage());
        }
    }

    public void printWorkSchedule(){
        for(int i = 0; i < workSchedual.size(); i++){
            shift temp = workSchedual.get(i);
            System.out.println(temp.getDate() + "-" + temp.getStartTime() + " " + temp.getEndTime());
        }
    }

    
}
