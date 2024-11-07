package src.main.java.com.sportinggoods.model;

public class Employee{
    protected int id;
    protected String name;
    private boolean placeOrder;
    private boolean addingItems;
    private boolean makeSchedule;

    public Employee(){}

    //Constructor
    public Employee(String name, int id) {
        this.name = name;
        this.id = id;
    }

    //getters and setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName(){
        return name;
    }

    //Functions that all objects that are employees have
    public Boolean canPlaceOrder() {
        //Insert logic here

        return placeOrder;
    }

    public Boolean canAddItems() {
        //insert logic here

        return addingItems;
    }

    public Boolean canMakeSchedule() {
        // insert logic here

        return makeSchedule;
    }


}
