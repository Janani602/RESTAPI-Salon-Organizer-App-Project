package com.demo.project.service;

import com.demo.project.entity.Customer;
import com.demo.project.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

  
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

   
    public List<Customer> saveCustomers(List<Customer> customers) {
        return customerRepository.saveAll(customers);
    }

 
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }


    public Optional<Customer> getCustomerById(int id) {
        return customerRepository.findById(id);
    }

    
    public Customer updateCustomer(int id, Customer updatedCustomer) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + id));

        existingCustomer.setCustomerName(updatedCustomer.getCustomerName());
        existingCustomer.setEmail(updatedCustomer.getEmail());
        existingCustomer.setPhoneNumber(updatedCustomer.getPhoneNumber());

        return customerRepository.save(existingCustomer);
    }

 
    public void deleteCustomer(int id) {
        customerRepository.deleteById(id);
    }

    // Get Customers by Name (JPQL)
    public List<Customer> getCustomersByName(String name) {
        return customerRepository.findByCustomerName(name);
    }

    // Get Customer by Email (JPQL)
    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }


    public Page<Customer> getCustomersWithPagination(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }
    public List<Customer> getCustomersWithSorting(Sort sort) {
        return customerRepository.findAll(sort);
    }
}