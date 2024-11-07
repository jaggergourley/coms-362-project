package src.main.java.com.sportinggoods.controller;

import src.main.java.com.sportinggoods.model.Inventory;
import src.main.java.com.sportinggoods.model.Item;
import com.sportinggoods.model.ShippingOrder;
import src.main.java.com.sportinggoods.repository.ShippingOrderRepository;

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

        for(Map.Entry<Item, Integer> entry : order.getItems().entrySet()){
            Item item = entry.getKey();
            int quantity = entry.getValue();
            if(i.checkAvailability(item.getName(), quantity)){
                i.updateQuantity(item.getName(), item.getQuantity() - quantity);
                shippedItems.put(item, quantity);
            }
            else{
                i.updateQuantity(item.getName(), 0);
                int amountSendable = item.getQuantity();
                shippedItems.put(item, amountSendable);

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
                    return orderRepo.updateOrderStatus(order.getOrderId(), "Partially Shipped");
                }
            }
        }


        return true;
    }

}
