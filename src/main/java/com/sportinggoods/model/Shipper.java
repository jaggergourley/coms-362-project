package src.main.java.com.sportinggoods.model;

public class Shipper extends Employee {
    private boolean canShipOrders;

    public Shipper() {
        super();
    }

    // Constructor for Shipper with additional canShipOrders permission
    public Shipper(String name, int id, boolean canShipOrders) {
        super(name, id);
        this.canShipOrders = canShipOrders;
    }

    // Getter and Setter for canShipOrders
    public boolean canShipOrders() {
        return canShipOrders;
    }

    public void setCanShipOrders(boolean canShipOrders) {
        this.canShipOrders = canShipOrders;
    }

    // Override methods from Employee to specify permissions for Shipper
    @Override
    public Boolean canPlaceOrder() {
        return false; // Shippers typically don't place orders
    }

    @Override
    public Boolean canAddItems() {
        return false; // Shippers typically don't add items
    }

    @Override
    public Boolean canMakeSchedule() {
        return false; // Shippers typically don't make schedules
    }

    // Method specific to Shipper role
    public void shipOrder(int orderId) {
        if (canShipOrders) {
            System.out.println("Shipper " + getName() + " is shipping order with ID: " + orderId);
            // Insert additional shipping logic here
        } else {
            System.out.println("Shipper " + getName() + " does not have permission to ship orders.");
        }
    }
}
