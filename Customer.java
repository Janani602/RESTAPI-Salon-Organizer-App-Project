package com.demo.project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer customerId; 

    @NotBlank(message = "Customer name cannot be empty")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Customer name must contain only letters and spaces")
    private String customerName;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Phone number must be exactly 10 digits and start with 6-9")
    private String phoneNumber;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference  
    private List<Reward> rewards = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
   @JsonManagedReference  
private List<Appointment> appointments = new ArrayList<>();


    public Customer() {}

 
    public Customer(String customerName, String email, String phoneNumber) {
        this.customerName = customerName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    // Getters and Setters
    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public List<Reward> getRewards() { return rewards; }
    
    public void setRewards(List<Reward> rewards) {
        this.rewards.clear();
        if (rewards != null) {
            for (Reward reward : rewards) {
                reward.setCustomer(this); 
                this.rewards.add(reward);
            }
        }
    }

    public List<Appointment> getAppointments() { return appointments; }
    
    public void setAppointments(List<Appointment> appointments) {
        this.appointments.clear();
        if (appointments != null) {
            for (Appointment appointment : appointments) {
                addAppointment(appointment);
            }
        }
    }

    public void addAppointment(Appointment appointment) {
        appointment.setCustomer(this);
        this.appointments.add(appointment);
    }
}
