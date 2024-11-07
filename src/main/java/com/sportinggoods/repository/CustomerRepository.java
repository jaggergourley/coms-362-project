package src.main.java.com.sportinggoods.repository;

import src.main.java.com.sportinggoods.model.Customer;
import src.main.java.com.sportinggoods.util.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class CustomerRepository {
    private final String filePath = "data/customers.csv";

    public CustomerRepository() {
        FileUtils.initializeFile(filePath, "customerId,name");
    }

    public boolean addCustomer(Customer customer) {
        return FileUtils.appendToFile(filePath, customer.toCSV());
    }

    public Customer getCustomerById(int customerId) {
        List<String> lines = FileUtils.readAllLines(filePath);
        for (String line : lines) {
            Customer customer = Customer.fromCSV(line);
            if (customer != null && customer.getCustomerId() == customerId) {
                return customer;
            }
        }
        return null;
    }

}
