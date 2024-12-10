package com.sportinggoods.util;

import com.sportinggoods.controller.*;
import com.sportinggoods.model.*;
import com.sportinggoods.repository.*;

import java.time.LocalDateTime;
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
    private FeedbackController feedbackController; // Added
    private AppointmentController appointmentController; // Added

    // Models
    private Cashier cashier;
    private Cart cart;
    private Coupon coupon;
    private Customer customer;
    private Discount discount;
    private Employee employee;
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
    private EmployeeList employeeList;
    private Utility utility;
    private Appointment appointment; // Added

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
    private FeedbackRepository feedbackRepo;
    private PickupOrderRepository pickupOrderRepo;
    private AppointmentRepository appointmentRepo;

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
        utilityController = new UtilityController(utilityRepo);
        maintenanceRequestController = new MaintenanceRequestController(maintenanceRequestRepo);
        feedbackController = new FeedbackController(feedbackRepo);
        appointmentController = new AppointmentController(appointmentRepo);
    }

    /**
     * Initializes models.
     */
    private void initializeModels() {
        cashier = new Cashier("John Doe", 101, null, 1);
        cart = new Cart();
        employee = new Employee("Mason", 1, new Schedule(), 1);
        regionalManager = new RegionalManger(1, "Regional Manager", true, true, true, new Schedule());
        inventory = new Inventory(1);
        receipt = new Receipt();
        register = new Register();
        schedule = new Schedule();
        shipper = new Shipper();
        shippingOrder = new ShippingOrder();
        store = new Store(1, "123 Lane");
        supplier = new Supplier();
        supplierOrder = new SupplierOrder();
        utility = new Utility();
        employeeList = new EmployeeList(1);
        appointment = new Appointment();
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
        feedbackRepo = new FeedbackRepository(); // Added
        pickupOrderRepo = new PickupOrderRepository(getInventory(1));
        appointmentRepo = new AppointmentRepository(); // Added
    }

    // Getters for Controllers

    public MaintenanceRequestController getMaintenanceRequestController() {return maintenanceRequestController;}
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
    public FeedbackController getFeedbackController() { return feedbackController; } // Added
    public AppointmentController getAppointmentController() { return appointmentController; } // Added

    // Getters for Models
    public Cashier getCashier() { return cashier; }
    public Cart getCart() { return cart; }
    public Coupon getCoupon() { return coupon; }
    public Customer getCustomer() { return customer; }
    public Discount getDiscount() { return discount; }
    public Employee getEmployee() { return employee; }
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
    public EmployeeList getEmployeeList() { return employeeList; }

    // Getters for Repositories
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
    public AppointmentRepository getAppointmentRepo() { return appointmentRepo; } // Added

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
