package com.sportinggoods.util;

import com.sportinggoods.controller.*;
import com.sportinggoods.model.*;
import com.sportinggoods.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Manages the initialization of controllers, repositories, and models.
 * Ensures proper dependency injection and resource management.
 */
public class InitializationManager {
    // Controllers
    private SupplierController supplierController;
    private PricingController pricingController;
    private GiftCardController giftCardController;
    private ShippingController shippingController;
    private CashierController cashierController;
    private RegisterController registerController;
    private ReceiptController receiptController;

    // Repositories
    private SupplierRepository supplierRepo;
    private SupplierOrderRepository orderRepo;
    private ItemRepository itemRepository;
    private GiftCardRepository giftCardRepository;
    private ShippingOrderRepository shippingRepo;
    private ReceiptRepository receiptRepo;

    // Models
    private Inventory inventory;
    private Employee employee;
    private Schedule schedule;
    private Register register;
    private Cashier cashier;

    // Scanner for user input
    private Scanner scanner;

    /**
     * Initializes all components required by the system.
     */
    public InitializationManager() {
        this.scanner = new Scanner(System.in);
        initializeRepositories();
        initializeModels();
        initializeControllers();
        fillInventory();
    }

    /**
     * Initializes all repositories.
     */
    private void initializeRepositories() {
        supplierRepo = new SupplierRepository();
        orderRepo = new SupplierOrderRepository();
        itemRepository = new ItemRepository();
        giftCardRepository = new GiftCardRepository(new ArrayList<>());
        shippingRepo = new ShippingOrderRepository();
        receiptRepo = new ReceiptRepository();
    }

    /**
     * Initializes models like Employee and Schedule.
     */
    private void initializeModels() {
        schedule = new Schedule();
        employee = new Employee("Mason", 1, schedule);
        inventory = new Inventory();
    }

    /**
     * Initializes all controllers with their respective repositories and models.
     */
    private void initializeControllers() {
        supplierController = new SupplierController(supplierRepo, orderRepo);
        pricingController = new PricingController(inventory);
        giftCardController = new GiftCardController(giftCardRepository);
        shippingController = new ShippingController(shippingRepo);
        receiptController = new ReceiptController(receiptRepo); // Initialize ReceiptController

        register = new Register(); // Initialize Register model
        registerController = new RegisterController(register);

        cashier = new Cashier("John Doe", 101, null);
        cashierController = new CashierController(cashier, inventory, registerController, receiptRepo);
    }

    /**
     * Populates the inventory with initial items.
     */
    private void fillInventory() {
        // Example: Adding initial items
        inventory.addItem(new Item("Football", 29.99, "Sports", 50, 1));
        inventory.addItem(new Item("Basketball", 24.99, "Sports", 40, 1));
        inventory.addItem(new Item("Tennis Racket", 89.99, "Sports", 20, 1));
        // Add more items as needed
    }

    // Getters for Controllers and Models

    public SupplierController getSupplierController() { return supplierController; }
    public PricingController getPricingController() { return pricingController; }
    public GiftCardController getGiftCardController() { return giftCardController; }
    public ShippingController getShippingController() { return shippingController; }
    public CashierController getCashierController() { return cashierController; }
    public RegisterController getRegisterController() { return registerController; }
    public ReceiptController getReceiptController() { return receiptController; }
    public Inventory getInventory() { return inventory; }
    public Employee getEmployee() { return employee; }
    public Register getRegister() { return register; }
    public Cashier getCashier() { return cashier; }
    public Scanner getScanner() { return scanner; }
    public ShippingOrderRepository getShippingRepo() {
        return shippingRepo;
    }

    /**
     * Closes the Scanner and any other resources.
     */
    public void shutdown() {
        if (scanner != null) {
            scanner.close();
        }
    }
}
