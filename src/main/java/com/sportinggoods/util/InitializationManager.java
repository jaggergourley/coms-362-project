package com.sportinggoods.util;

import com.sportinggoods.controller.*;
import com.sportinggoods.model.*;
import com.sportinggoods.repository.*;
import java.util.List;
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
    private UtilityController utilityController;
    private MaintenanceRequestController maintenanceRequestController;
    private FeedbackController feedbackController;// Added

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
    private RegionalManger regionalManager;
    private Register register;
    private Schedule schedule;
    private Shipper shipper;
    private ShippingOrder shippingOrder;
    private Store store;
    private Supplier supplier;
    private SupplierOrder supplierOrder;
    private Utility utility;

    // Maintenance-related models
    private MaintenanceRequest maintenanceRequest; // Added

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
    private UtilityRepository utilityRepo;
    private MaintenanceRequestRepository maintenanceRequestRepo;
    private FeedbackRepository feedbackRepo;// Added
    private PickupOrderRepository pickupOrderRepo;

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
        fillUtilities();
    }

    /**
     * Initializes all controllers.
     */
    private void initializeControllers() {
        cashierController = new CashierController(cashier, inventory, registerController, receiptRepo, couponRepo);
        customerController = new CustomerController(customerRepo);
        discountController = initializeDiscountController(); // Update for DiscountController
        giftCardController = new GiftCardController(giftCardRepo);
        pricingController = new PricingController(inventory);
        receiptController = new ReceiptController(receiptRepo);
        registerController = new RegisterController(register);
        shippingController = new ShippingController(shippingOrderRepo);
        supplierController = new SupplierController(supplierRepo, supplierOrderRepo);
        utilityController = new UtilityController(utilityRepo);
        maintenanceRequestController = new MaintenanceRequestController(maintenanceRequestRepo);
        feedbackController = new FeedbackController(feedbackRepo);// Added
    }

    /**
     * Initializes the DiscountController with the current inventory.
     */
    private DiscountController initializeDiscountController() {
        if (inventory == null) {
            throw new IllegalStateException("Inventory must be initialized before DiscountController.");
        }
        return new DiscountController(discountRepo, inventory);
    }

    /**
     * Initializes models.
     */
    private void initializeModels() {
        cashier = new Cashier("John Doe", 101, null);
        cart = new Cart();
        employee = new Employee("Mason", 1, new Schedule());
        regionalManager = new RegionalManger(1, "Regional Manager", true, true, true, new Schedule());
        inventory = new Inventory(1); // Ensure Inventory is initialized
        receipt = new Receipt();
        register = new Register();
        schedule = new Schedule();
        shipper = new Shipper();
        shippingOrder = new ShippingOrder();
        store = new Store(1, "123 lane");
        supplier = new Supplier();
        supplierOrder = new SupplierOrder();
        utility = new Utility();

        // Add some initial stores
        regionalManager.addStore("123 Main Street");
        regionalManager.addStore("456 Elm Street");
        regionalManager.addStore("789 Maple Avenue");
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
        utilityRepo = new UtilityRepository();
        maintenanceRequestRepo = new MaintenanceRequestRepository();
        feedbackRepo = new FeedbackRepository();// Added
        pickupOrderRepo = new PickupOrderRepository(getInventory(1));
    }

    /**
     * Fills utilities with initial data.
     */
    private void fillUtilities() {
        List<Utility> existingUtilities = utilityRepo.getAllUtilities();
        if (!existingUtilities.isEmpty()) {
            return;
        }
        utilityRepo.addUtility(new Utility("U001", 1, "HVAC", "Active", 750.5, "2024-11-18", "09:00-21:00"));
        utilityRepo.addUtility(new Utility("U002", 1, "Lighting", "Active", 450.2, "2024-11-18", "09:00-21:00"));
        utilityRepo.addUtility(new Utility("U003", 1, "Water", "Active", 150.0, "2024-11-18", "24/7"));
    }

    /**
     * Fills maintenance requests with initial data.
     */

    public MaintenanceRequestController getMaintenanceRequestController() {
        return maintenanceRequestController;
    }

    public CashierController getCashierController() { return cashierController; }
    public CustomerController getCustomerController() { return customerController; }
    public DiscountController getDiscountController() { return discountController; }
    public GiftCardController getGiftCardController() { return giftCardController; }
    public PricingController getPricingController() { return pricingController; }
    public ReceiptController getReceiptController() { return receiptController; }
    public RegisterController getRegisterController() { return registerController; }
    public ShippingController getShippingController() { return shippingController; }
    public SupplierController getSupplierController() { return supplierController; }
    public UtilityController getUtilityController() { return utilityController; }
    public FeedbackController getFeedbackController() { return feedbackController; }

    // Getters for models
    public Cashier getCashier() { return cashier; }
    public Cart getCart() { return cart; }
    public Coupon getCoupon() { return coupon; }
    public Customer getCustomer() { return customer; }
    public Discount getDiscount() { return discount; }
    public Employee getEmployee() { return employee; }
    public GiftCard getGiftCard() { return giftCard; }
    public Inventory getInventory(int storeId) { return new Inventory(storeId); }
    public Item getItem() { return item; }
    public Manager getManager() { return manager; }
    public Receipt getReceipt() { return receipt; }
    public RegionalManger getRegionalManager() { return regionalManager; }
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
    public UtilityRepository getUtilityRepo() { return utilityRepo; }
    public FeedbackRepository getFeedbackRepo() { return feedbackRepo; }
    public PickupOrderRepository getPickupOrderRepository() { return pickupOrderRepo; }

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
