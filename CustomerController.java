package com.demo.project.controller;

import com.demo.project.entity.Customer;
import com.demo.project.service.CustomerService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<Object> createCustomer(@Valid @RequestBody Customer customer, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(getValidationErrors(result));
        }
        return ResponseEntity.ok(customerService.saveCustomer(customer));
    }

    @PostMapping("/bulk")
    public ResponseEntity<Object> createCustomers(@Valid @RequestBody List<Customer> customers) {
        Map<String, String> errors = new HashMap<>();
        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            if (customer.getCustomerName() == null || customer.getCustomerName().isBlank()) {
                errors.put("customers[" + i + "].customerName", "Customer name cannot be empty");
            }
            if (customer.getEmail() == null || !customer.getEmail().matches(".+@.+\\..+")) {
                errors.put("customers[" + i + "].email", "Invalid email format");
            }
            if (customer.getPhoneNumber() == null || !customer.getPhoneNumber().matches("^[6-9]\\d{9}$")) {
                errors.put("customers[" + i + "].phoneNumber", "Phone number must be exactly 10 digits and start with 6-9");
            }
        }
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
        return ResponseEntity.ok(customerService.saveCustomers(customers));
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCustomerById(@PathVariable int id) {
        Optional<Customer> customer = customerService.getCustomerById(id);
        if (customer.isPresent()) {
            return ResponseEntity.ok(customer.get());
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Customer with ID " + id + " not found!");
            return ResponseEntity.status(404).body(errorResponse);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCustomer(@PathVariable int id, @Valid @RequestBody Customer customer) {
        try {
            return ResponseEntity.ok(customerService.updateCustomer(id, customer));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable int id) {
        if (!customerService.getCustomerById(id).isPresent()) {
            return ResponseEntity.status(404).body("Customer with ID " + id + " not found!");
        }
        customerService.deleteCustomer(id);
        return ResponseEntity.ok("Customer with ID " + id + " has been deleted.");
    }

    @GetMapping("/search/name/{name}")
    public ResponseEntity<Object> getCustomersByName(@PathVariable String name) {
        List<Customer> customers = customerService.getCustomersByName(name);
        if (customers.isEmpty()) {
            return ResponseEntity.status(404).body("{\"message\": \"No customers found with name: " + name + "\"}");
        }
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/search/email/{email}")
    public ResponseEntity<Object> getCustomerByEmail(@PathVariable String email) {
        Customer customer = customerService.getCustomerByEmail(email);
        if (customer == null) {
            return ResponseEntity.status(404).body("{\"message\": \"Customer with email " + email + " not found!\"}");
        }
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/sorted")
    public ResponseEntity<List<Customer>> getCustomersWithSorting(
            @RequestParam(defaultValue = "customerName") String sortBy,
            @RequestParam(defaultValue = "asc") String order) {
        Sort sort = order.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        return ResponseEntity.ok(customerService.getCustomersWithSorting(sort));
    }

    private Map<String, String> getValidationErrors(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : result.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return errors;
    }
}
