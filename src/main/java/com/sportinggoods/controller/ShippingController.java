package com.sportinggoods.controller;

import com.sportinggoods.model.Inventory;
import com.sportinggoods.model.Item;
import com.sportinggoods.model.ShippingOrder;
import com.sportinggoods.repository.ShippingOrderRepository;

import java.time.LocalDate;
import java.util.*;



public class ShippingController {

    private ShippingOrderRepository orderRepo;

    public ShippingController(ShippingOrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    /**
     * Creates a new shipping order
     * @param customerFirstName
     * @param customerLastName
     * @param items
     * @param totalPrice
     * @param shippingAddress
     * @param customerEmail
     * @param customerPhoneNumber
     * @return True when added to the ShippingOrderRespository
     */
    public boolean handleShippingOrder(String customerFirstName, String customerLastName, Map<Item, Integer> items,
                                       double totalPrice, String shippingAddress, String customerEmail, String customerPhoneNumber){

        String orderId = UUID.randomUUID().toString();

        LocalDate date =  LocalDate.now();


        ShippingOrder order = new ShippingOrder(
                orderId,
                customerFirstName,
                customerLastName,
                items,
                totalPrice,
                shippingAddress,
                customerEmail,
                customerPhoneNumber,
                date,
                "Confirmed"
        );

        return orderRepo.addShippingOrder(order);
    }

    /**
     * Ensures shipping order is available in a stores inventory
     * @param order
     * @param i
     * @return True if Shipping Order Repository is updated with the correct status
     */
    public boolean processShippingOrder(ShippingOrder order, Inventory i){

        boolean available = false;
        Map<Item, Integer> shippedItems = new HashMap<>();
        Map<Item, Integer> unfinishedShippingOrder = new HashMap<>();

        for(Map.Entry<Item, Integer> entry : order.getItems().entrySet()){
            Item item = entry.getKey();
            int quantity = entry.getValue();


            if(i.checkAvailability(item.getName(), quantity)){
                i.updateQuantity(item.getName(), -quantity);
                shippedItems.put(item, quantity);
                System.out.println("Shipped " + item.getName() + ", Items Left: " + i.getItem(item.getName()).getQuantity());
            }
            else{
                int amountSendable = i.getItem(item.getName()).getQuantity();
                i.updateQuantity(item.getName(), -i.getItem(item.getName()).getQuantity());
                unfinishedShippingOrder.put(item, quantity - amountSendable);
                shippedItems.put(item, amountSendable);
                System.out.println(item.getName() + " only shipped " + amountSendable + "/" + quantity + ". The rest will be sent in another order from another store");

            }
        }


        if(shippedItems.isEmpty()){
            return false;
        }


        for(Map.Entry<Item, Integer> entry : order.getItems().entrySet()){
            for(Map.Entry<Item, Integer> shippedEntry : order.getItems().entrySet()){
                if(entry.getKey().equals(shippedEntry.getKey()) && entry.getValue() == shippedEntry.getValue()){
                    return orderRepo.updateOrderStatus(order.getOrderId(), "Shipped");
                }
                else{

                    double newPrice = 0;



                    for(Map.Entry<Item, Integer> price : unfinishedShippingOrder.entrySet()) {
                        Item item = entry.getKey();

                        newPrice += Math.round(item.getPrice() * item.getQuantity() * 100.0) / 100.0;  //might need to change
                    }
                    handleShippingOrder(order.getCustomerFirstName(), order.getCustomerLastName(), unfinishedShippingOrder, newPrice, order.getShippingAddress(), order.getCustomerEmail(), order.getCustomerPhoneNumber());
                    return orderRepo.updateOrderStatus(order.getOrderId(), "Partially Shipped") && orderRepo.updateOrderQuantity(order.getOrderId(), shippedItems) && orderRepo.updateOrderPrice(order.getOrderId(), shippedItems);
                }
            }
        }


        return true;
    }

}