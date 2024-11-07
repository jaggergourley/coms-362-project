package src.main.java.com.sportinggoods.controller;

import src.main.java.com.sportinggoods.model.Customer;
import src.main.java.com.sportinggoods.repository.CustomerRepository;

public class CustomerController {
    private CustomerRepository customerRepo;

    public CustomerController(CustomerRepository customerRepo) {
        this.customerRepo = customerRepo;
    }

    public boolean addCustomer(Customer customer) {
        return customerRepo.addCustomer(customer);
    }

    public Customer getCustomer(int customerId) {
        return customerRepo.getCustomerById(customerId);
    }
}
