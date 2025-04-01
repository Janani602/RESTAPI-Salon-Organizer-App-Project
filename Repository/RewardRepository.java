package com.demo.project.repository;

import com.demo.project.entity.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Integer> {

    Optional<Reward> findByRewardName(String rewardName);
    //  Find rewards by customer ID (Corrected @ManyToOne relationship)
    List<Reward> findByCustomer_CustomerId(Integer customerId);

    // Find rewards with points greater than a certain value
    @Query("SELECT r FROM Reward r WHERE r.rewardPoints > :points")
    List<Reward> findRewardsWithPointsGreaterThan(Integer points);

    //  Find rewards by name (case-insensitive search, improved LIKE query)
    @Query("SELECT r FROM Reward r WHERE LOWER(r.rewardName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Reward> findByRewardNameContainingIgnoreCase(String name);
}
