package com.sportinggoods.util;

import com.sportinggoods.controller.*;
import com.sportinggoods.model.*;
import com.sportinggoods.repository.*;
import java.time.LocalDateTime;
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
    private AppointmentController appointmentController;
    private FeedbackController feedbackController;
    private EmployeeController employeeController;
    private TrainingProgramController trainingProgramController;
    private EmployeeTrainingController employeeTrainingController;


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
    private Appointment appointment;
    private MaintenanceRequest maintenanceRequest;
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
    private EmployeeRepository employeeRepo;
    private TrainingProgramRepository trainingProgramRepo;
    private EmployeeTrainingRepository employeeTrainingRepo;


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
        defaultEmployees();
        defaultTrainingPrograms();
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
        employeeController = new EmployeeController(employeeRepo);
        trainingProgramController = new TrainingProgramController(trainingProgramRepo, scanner);
        employeeTrainingController = new EmployeeTrainingController(employeeTrainingRepo, trainingProgramController, employeeController, scanner);
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
        inventory = new Inventory(1); // Ensure Inventory is initialized
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

        

        // Add some initial stores
        // regionalManager.getStoreList().addStore(new Store(1, "123 Main Street"));
        // regionalManager.getStoreList().addStore(new Store(2, "456 Elm Street"));
        // regionalManager.getStoreList().addStore(new Store(3, "789 Maple Avenue"));

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
        feedbackRepo = new FeedbackRepository();
        pickupOrderRepo = new PickupOrderRepository(getInventory(1));
        appointmentRepo = new AppointmentRepository();
        employeeRepo = new EmployeeRepository();
        trainingProgramRepo = new TrainingProgramRepository();
        employeeTrainingRepo = new EmployeeTrainingRepository();
    }


    private void defaultEmployees() {
        List<Employee> existingEmployees = employeeRepo.getAllEmployees();
        if (!existingEmployees.isEmpty()) {
            return; // Employees already populated
        }
        employeeRepo.addEmployee(new Employee("Alice", 1, null, 1, "Manager", "Sports"));
        employeeRepo.addEmployee(new Employee("Bob", 2, null, 1, "Sales Associate", "Fitness"));
        employeeRepo.addEmployee(new Employee("Charlie", 3, null, 2, "Technician", "Outdoor"));
        employeeRepo.addEmployee(new Employee("Daisy", 4, null, 2, "Stocker", "Equipment"));
        employeeRepo.addEmployee(new Employee("Eve", 5, null, 3, "Cashier", "Customer Service"));
        employeeRepo.addEmployee(new Employee("Frank", 6, null, 3, "Maintenance", "Maintenance"));
    }

    private void defaultTrainingPrograms() {
        List<TrainingProgram> existingPrograms = trainingProgramRepo.getAllTrainingPrograms();
        if (!existingPrograms.isEmpty()) {
            return; // Training programs already populated
        }

        // Add default training programs
        trainingProgramRepo.addTrainingProgram(new TrainingProgram(101, "Customer Service Essentials",
                "Learn the basics of excellent customer service.", 20));
        trainingProgramRepo.addTrainingProgram(new TrainingProgram(102, "Advanced Sales Techniques",
                "Enhance your sales skills and close more deals.", 15));
        trainingProgramRepo.addTrainingProgram(new TrainingProgram(103, "Inventory Management 101",
                "Understand the fundamentals of managing inventory.", 25));
        trainingProgramRepo.addTrainingProgram(new TrainingProgram(104, "Leadership and Management",
                "Develop leadership skills for managing teams effectively.", 10));
        trainingProgramRepo.addTrainingProgram(new TrainingProgram(105, "Workplace Safety Training",
                "Ensure a safe and healthy workplace environment.", 30));

        System.out.println("Default training programs have been added.");
    }

    /**
     * Fills maintenance requests with initial data.
     */

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
    public FeedbackController getFeedbackController() { return feedbackController; }
    public AppointmentController getAppointmentController() { return appointmentController; }
    public EmployeeController getEmployeeController() { return employeeController; }
    public TrainingProgramController getTrainingProgramController() { return trainingProgramController; }
    public EmployeeTrainingController getEmployeeTrainingController() { return employeeTrainingController; }

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
    public AppointmentRepository getAppointmentRepo() { return appointmentRepo; }
    public EmployeeRepository getEmployeeRepo() { return employeeRepo; }
    public TrainingProgramRepository getTrainingProgramRepo() { return trainingProgramRepo; }
    public EmployeeTrainingRepository getEmployeeTrainingRepo() { return employeeTrainingRepo; }

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
