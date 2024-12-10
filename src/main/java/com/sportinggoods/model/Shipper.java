package com.sportinggoods.model;

import com.sportinggoods.controller.ShippingController;
import com.sportinggoods.repository.ShippingOrderRepository;

public class Shipper extends Employee {
    private boolean canShipOrders;

    private ShippingController shippingController;

    private ShippingOrderRepository shippingOrderRepository;

    public Shipper() {
        super();
    }

    // Constructor for Shipper with additional canShipOrders permission


    public Shipper(String name, int id, boolean canShipOrders, Schedule workSchedule, ShippingController cont,int storeID) {
        super(name, id, workSchedule, storeID);

        this.canShipOrders = canShipOrders;
        this.shippingController = cont;
    }

    // Getter and Setter for canShipOrders
    public boolean canShipOrders() {
        return canShipOrders;
    }

    public void setCanShipOrders(boolean canShipOrders) {
        this.canShipOrders = canShipOrders;
    }

//    // Override methods from Employee to specify permissions for Shipper
//    @Override
//    public Boolean canPlaceOrder() {
//        return false; // Shippers typically don't place orders
//    }
//
//    @Override
//    public Boolean canAddItems() {
//        return false; // Shippers typically don't add items
//    }
//
//    @Override
//    public Boolean canMakeSchedule() {
//        return false; // Shippers typically don't make schedules
//    }

    // Method specific to Shipper role
    public void shipOrder(ShippingOrder shippingOrder, Inventory inventory) {
        if (canShipOrders) {
            System.out.println("A Shipper " + "is shipping order with ID: " + shippingOrder.getOrderId());
            if(shippingController.processShippingOrder(shippingOrder, inventory, getStoreId())){
                System.out.println(shippingOrder.getStatus());
            }
        } else {
            System.out.println("Shipper " + getName() + " does not have permission to ship orders.");
        }
    }
}