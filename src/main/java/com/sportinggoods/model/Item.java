package src.main.java.com.sportinggoods.model;

public class Item {
    private String name;
    private double price;
    private String department;
    private int quantity;

    // Constructor
    public Item(String name, double price, String department, int quantity) {
        this.name = name;
        this.price = price;
        this.department = department;
        this.quantity = quantity;
    }

    //Getters and Setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Converts an Item object to a CSV string
    public String toCSV() {
        return name + "," + price + "," + department + "," + quantity;
    }

    // Creates an Item object from a CSV string
    public static Item fromCSV(String csvLine) {
        String[] tokens = csvLine.split(",");
        if (tokens.length != 4) {
            return null;  // Invalid format
        }
        String name = tokens[0];
        double price = Double.parseDouble(tokens[1]);
        String department = tokens[2];
        int quantity = Integer.parseInt(tokens[3]);
        return new Item(name, price, department, quantity);
    }

    @Override
    public String toString() {
        return name + " - $" + price + ", " + department + " Dept., Quantity: " + quantity;
    }
}
