package src.main.java.com.sportinggoods.model;

import java.util.ArrayList;

public class Manager extends Employee{

    public Manager(String name, int id, Schedule workSchedule){
        super(name, id, workSchedule);
    }
  
    public void addItemToInventory(Inventory inventory, Item item) {
        inventory.addItem(item);  // Calls addItem on the provided Inventory object
    }

    public void addItemsInBulk(Inventory inventory, ArrayList<Item> items){
        inventory.addItems(items);
    }

    public void deleteItemFromInventory(Inventory inventory, Item item) {
        inventory.deleteItem(item);
    }

    public void deleteItemsInBulk(Inventory inventory, ArrayList<Item> items){
        inventory.deleteItems(items);
    }

    public void swapStore(Inventory inventory, Item item, int newStoreId) {
        inventory.swapStore(item.getName(), newStoreId);
    }

    public void addShiftToEmployee(Employee employee, shift shift) {
        employee.getWorkSchedule().addShift(shift);  // Assuming Employee has getSchedule method
    }

}
