package com.demo.project.service;

import com.demo.project.entity.Reward;
import com.demo.project.entity.Customer;
import com.demo.project.repository.RewardRepository;
import com.demo.project.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RewardService {

    @Autowired
    private RewardRepository rewardRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Reward saveReward(Reward reward) {
        validateReward(reward);

        // Ensure customer exists before saving the reward
        if (reward.getCustomer() != null) {
            Customer customer = customerRepository.findById(reward.getCustomer().getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + reward.getCustomer().getCustomerId()));
            reward.setCustomer(customer);
        }

        // Check if reward name already exists (to prevent duplicates)
        Optional<Reward> existingReward = rewardRepository.findByRewardName(reward.getRewardName());
        if (existingReward.isPresent()) {
            throw new RuntimeException("Reward with name '" + reward.getRewardName() + "' already exists!");
        }

        return rewardRepository.save(reward);
    }

    public List<Reward> saveAllRewards(List<Reward> rewards) {
        for (Reward reward : rewards) {
            validateReward(reward);

            if (reward.getCustomer() != null) {
                Customer customer = customerRepository.findById(reward.getCustomer().getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + reward.getCustomer().getCustomerId()));
                reward.setCustomer(customer);
            }

            // Prevent duplicate rewards
            Optional<Reward> existingReward = rewardRepository.findByRewardName(reward.getRewardName());
            if (existingReward.isPresent()) {
                throw new RuntimeException("Reward with name '" + reward.getRewardName() + "' already exists!");
            }
        }
        return rewardRepository.saveAll(rewards);
    }

    public List<Reward> getAllRewards() {
        return rewardRepository.findAll();
    }

    public Reward getRewardById(int id) {
        return rewardRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Reward not found with ID: " + id));
    }

    public Reward updateReward(int id, Reward reward) {
        Reward existingReward = rewardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reward not found with ID: " + id));

        validateReward(reward);

        if (reward.getCustomer() != null) {
            Customer customer = customerRepository.findById(reward.getCustomer().getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + reward.getCustomer().getCustomerId()));
            existingReward.setCustomer(customer);
        }

        existingReward.setRewardPoints(reward.getRewardPoints());
        existingReward.setRewardName(reward.getRewardName());

        return rewardRepository.save(existingReward);
    }

    public void deleteReward(int id) {
        if (!rewardRepository.existsById(id)) {
            throw new RuntimeException("Reward not found with ID: " + id);
        }
        rewardRepository.deleteById(id);
    }

    private void validateReward(Reward reward) {
        if (reward == null) {
            throw new IllegalArgumentException("Reward cannot be null!");
        }
        if (reward.getRewardPoints() == null || reward.getRewardPoints() < 100) {
            throw new IllegalArgumentException("Reward points must be at least 100!");
        }
        if (reward.getRewardName() == null || reward.getRewardName().trim().isEmpty()) {
            throw new IllegalArgumentException("Reward name cannot be empty!");
        }
    }
}
