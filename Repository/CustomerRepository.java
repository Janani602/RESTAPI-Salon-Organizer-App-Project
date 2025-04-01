package com.demo.project.repository;

import com.demo.project.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    // Find customer by email
    Customer findByEmail(String email);

    // Find customers by name (search containing keyword)
    @Query("SELECT c FROM Customer c WHERE c.customerName LIKE %?1%")
    List<Customer> findByCustomerName(String name);

    // Find customers with invalid phone numbers (not exactly 10 digits)
    @Query("SELECT c FROM Customer c WHERE LENGTH(c.phoneNumber) <> 10")
    List<Customer> findCustomersWithInvalidPhoneNumbers();

    //  Find customers with invalid emails
    @Query("SELECT c FROM Customer c WHERE c.email NOT LIKE '%@%.%'")
    List<Customer> findCustomersWithInvalidEmails();
}
