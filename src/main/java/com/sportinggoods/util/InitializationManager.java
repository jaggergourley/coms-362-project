package com.sportinggoods.util;

import com.sportinggoods.controller.*;
import com.sportinggoods.model.*;
import com.sportinggoods.repository.*;

import java.util.Scanner;

/**
 * Manages initialization of controllers, repositories, and models.
 */
public class InitializationManager {
    // Controllers
    private CashierController cashierController;
    private CustomerController customerController;
    private DiscountController discountController;
    private GiftCardController giftCardController;
    private PricingController pricingController;
    private ReceiptController receiptController;
    private RegisterController registerController;
    private ShippingController shippingController;
    private SupplierController supplierController;

    // Models
    private Cashier cashier;
    private Cart cart;
    private Coupon coupon;
    private Customer customer;
    private Discount discount;
    private Employee employee;
    private GiftCard giftCard;
    private Inventory inventory;
    private Item item;
    private Manager manager;
    private Receipt receipt;
    private Register register;
    private Schedule schedule;
    private Shipper shipper;
    private ShippingOrder shippingOrder;
    private Store store;
    private Supplier supplier;
    private SupplierOrder supplierOrder;

    // Repositories
    private CashierRepository cashierRepo;
    private CouponRepository couponRepo;
    private CustomerRepository customerRepo;
    private DiscountRepository discountRepo;
    private GiftCardRepository giftCardRepo;
    private ReceiptRepository receiptRepo;
    private RegisterRepository registerRepo;
    private ShippingOrderRepository shippingOrderRepo;
    private SupplierOrderRepository supplierOrderRepo;
    private SupplierRepository supplierRepo;

    // Scanner for user input
    private Scanner scanner;

    /**
     * Initializes all components.
     */
    public InitializationManager() {
        this.scanner = new Scanner(System.in);
        initializeRepositories();
        initializeModels();
        initializeControllers();
        fillInventory();
    }

    /**
     * Initializes all controllers.
     */
    private void initializeControllers() {
        cashierController = new CashierController(cashier, inventory, registerController, receiptRepo, couponRepo);
        customerController = new CustomerController(customerRepo);
        discountController = new DiscountController(discountRepo, inventory);
        giftCardController = new GiftCardController(giftCardRepo);
        pricingController = new PricingController(inventory);
        receiptController = new ReceiptController(receiptRepo);
        registerController = new RegisterController(register);
        shippingController = new ShippingController(shippingOrderRepo);
        supplierController = new SupplierController(supplierRepo, supplierOrderRepo);
    }

    /**
     * Initializes models.
     */
    private void initializeModels() {
        cashier = new Cashier("John Doe", 101, null);
        cart = new Cart();
        employee = new Employee("Mason", 1, new Schedule());
        inventory = new Inventory(1);
        receipt = new Receipt();
        register = new Register();
        schedule = new Schedule();
        shipper = new Shipper();
        shippingOrder = new ShippingOrder();
        store = new Store(1, "123 lane");
        supplier = new Supplier();
        supplierOrder = new SupplierOrder();
    }

    /**
     * Initializes all repositories.
     */
    private void initializeRepositories() {
        cashierRepo = new CashierRepository();
        couponRepo = new CouponRepository();
        customerRepo = new CustomerRepository();
        discountRepo = new DiscountRepository();
        giftCardRepo = new GiftCardRepository();
        receiptRepo = new ReceiptRepository();
        registerRepo = new RegisterRepository();
        shippingOrderRepo = new ShippingOrderRepository();
        supplierOrderRepo = new SupplierOrderRepository();
        supplierRepo = new SupplierRepository();
    }

    /**
     * Fills inventory with initial items.
     */
    private void fillInventory() {
        inventory.addItem(new Item("Tennis Ball", 29.99, "Sports", 10, 1));
        inventory.addItem(new Item("Tennis Racket", 89.99, "Sports", 5, 1));
        inventory.addItem(new Item("Football", 24.99, "Sports", 5, 1));
        inventory.addItem(new Item("Bike", 309.99, "Sports", 5, 1));
        inventory.addItem(new Item("Shorts", 89.99, "Apparel", 5, 1));
    }

    // Getters for controllers
    public CashierController getCashierController() { return cashierController; }
    public CustomerController getCustomerController() { return customerController; }
    public DiscountController getDiscountController() { return discountController; }
    public GiftCardController getGiftCardController() { return giftCardController; }
    public PricingController getPricingController() { return pricingController; }
    public ReceiptController getReceiptController() { return receiptController; }
    public RegisterController getRegisterController() { return registerController; }
    public ShippingController getShippingController() { return shippingController; }
    public SupplierController getSupplierController() { return supplierController; }

    // Getters for models
    public Cashier getCashier() { return cashier; }
    public Cart getCart() { return cart; }
    public Coupon getCoupon() { return coupon; }
    public Customer getCustomer() { return customer; }
    public Discount getDiscount() { return discount; }
    public Employee getEmployee() { return employee; }
    public GiftCard getGiftCard() { return giftCard; }
    public Inventory getInventory() { return inventory; }
    public Item getItem() { return item; }
    public Manager getManager() { return manager; }
    public Receipt getReceipt() { return receipt; }
    public Register getRegister() { return register; }
    public Schedule getSchedule() { return schedule; }
    public Shipper getShipper() { return shipper; }
    public ShippingOrder getShippingOrder() { return shippingOrder; }
    public Store getStore() { return store; }
    public Supplier getSupplier() { return supplier; }
    public SupplierOrder getSupplierOrder() { return supplierOrder; }

    // Getters for repositories
    public CashierRepository getCashierRepo() { return cashierRepo; }
    public CouponRepository getCouponRepo() { return couponRepo; }
    public CustomerRepository getCustomerRepo() { return customerRepo; }
    public DiscountRepository getDiscountRepo() { return discountRepo; }
    public GiftCardRepository getGiftCardRepo() { return giftCardRepo; }
    public ReceiptRepository getReceiptRepo() { return receiptRepo; }
    public RegisterRepository getRegisterRepo() { return registerRepo; }
    public ShippingOrderRepository getShippingOrderRepo() { return shippingOrderRepo; }
    public SupplierOrderRepository getSupplierOrderRepo() { return supplierOrderRepo; }
    public SupplierRepository getSupplierRepo() { return supplierRepo; }

    public Scanner getScanner() { return scanner; }

    /**
     * Shuts down and releases resources.
     */
    public void shutdown() {
        if (scanner != null) {
            scanner.close();
        }
    }
}
