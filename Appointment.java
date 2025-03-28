package com.demo.project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer appointmentId;

    @NotNull(message = "Appointment date cannot be empty")
    @Column(nullable = false)
    private LocalDate appointmentDate;

    @NotBlank(message = "Service type cannot be empty")
    @Column(nullable = false)
    private String serviceType;

    @NotBlank(message = "Status cannot be empty")
    @Column(nullable = false)
    private String status;

    @NotNull(message = "Price cannot be empty")
    @DecimalMin(value = "100.0", message = "Price must be at least 100.0")
    @Column(nullable = false)
    private Double price;

    @ManyToOne
@JoinColumn(name = "customer_id")
@JsonBackReference //  Prevents infinite recursion
private Customer customer;


    // 🔹 Transient Field for Bulk Insert Support (Maps `customerId` from JSON)
    @Transient
    @JsonProperty("customerId") // Allows JSON to map this field
    private Integer customerId;

    // Default Constructor
    public Appointment() {}

    // Parameterized Constructor
    public Appointment(LocalDate appointmentDate, String serviceType, String status, Double price, Customer customer) {
        this.appointmentDate = appointmentDate;
        this.serviceType = serviceType;
        this.status = status;
        this.price = price;
        this.customer = customer;
    }

    // Getters and Setters
    public Integer getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Integer appointmentId) { this.appointmentId = appointmentId; }

    public LocalDate getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Integer getCustomerId() {
        return (customer != null) ? customer.getCustomerId() : null;
    }
    
    
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
        if (customerId != null) {
            this.customer = new Customer();
            this.customer.setCustomerId(customerId);
        }
    }
}
