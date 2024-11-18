package com.sportinggoods.controller;

import com.sportinggoods.model.Customer;
import com.sportinggoods.repository.CustomerRepository;

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