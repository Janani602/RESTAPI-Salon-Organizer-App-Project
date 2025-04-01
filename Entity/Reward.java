package com.demo.project.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "rewards")
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rewardId;

    @NotNull(message = "Reward points cannot be empty")
    @Min(value = 100, message = "Reward points must be at least 100!")
    @Column(nullable = false)
    private Integer rewardPoints;

    @NotBlank(message = "Reward name cannot be empty")
    @Column(nullable = false, length = 100)  // Removed unique constraint
    private String rewardName;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonBackReference  // Prevents infinite recursion
    private Customer customer;

    // Default constructor
    public Reward() {}

    // Parameterized constructor
    public Reward(Integer rewardPoints, String rewardName, Customer customer) {
        this.rewardPoints = rewardPoints;
        this.rewardName = rewardName;
        this.setCustomer(customer); //  Uses setter to maintain consistency
    }

    // Getters and Setters
    public Integer getRewardId() { return rewardId; }
    public void setRewardId(Integer rewardId) { this.rewardId = rewardId; }

    public Integer getRewardPoints() { return rewardPoints; }
    public void setRewardPoints(Integer rewardPoints) { this.rewardPoints = rewardPoints; }

    public String getRewardName() { return rewardName; }
    public void setRewardName(String rewardName) { this.rewardName = rewardName; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { 
        this.customer = customer;
        if (customer != null && !customer.getRewards().contains(this)) {
            customer.getRewards().add(this); //  Ensures proper bidirectional relationship
        }
    }

    @Override
    public String toString() {
        return "Reward{" +
                "rewardId=" + rewardId +
                ", rewardPoints=" + rewardPoints +
                ", rewardName='" + rewardName + '\'' +
                ", customerId=" + (customer != null ? customer.getCustomerId() : "null") +
                '}';
    }
}
