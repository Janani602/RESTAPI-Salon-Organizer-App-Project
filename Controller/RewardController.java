package com.demo.project.controller;

import com.demo.project.entity.Reward;
import com.demo.project.service.RewardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rewards")
public class RewardController {

    @Autowired
    private RewardService rewardService;

    @PostMapping
    public ResponseEntity<Object> addReward(@Valid @RequestBody Reward reward, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(getValidationErrors(result));
        }
        try {
            Reward savedReward = rewardService.saveReward(reward);
            return ResponseEntity.ok(savedReward);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping(value = "/bulk", consumes = "application/json")
    public ResponseEntity<Object> saveAllRewards(@RequestBody List<Reward> rewards) {
        try {
            List<Reward> savedRewards = rewardService.saveAllRewards(rewards);
            return ResponseEntity.ok(savedRewards);
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Reward>> getAllRewards() {
        return ResponseEntity.ok(rewardService.getAllRewards());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getRewardById(@PathVariable int id) {
        try {
            Reward reward = rewardService.getRewardById(id);
            return ResponseEntity.ok(reward);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Object> updateReward(@PathVariable int id, @Valid @RequestBody Reward reward, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(getValidationErrors(result));
        }
        try {
            return ResponseEntity.ok(rewardService.updateReward(id, reward));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteReward(@PathVariable int id) {
        try {
            rewardService.deleteReward(id);
            return ResponseEntity.ok(Map.of("message", "Reward with ID " + id + " has been deleted."));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }


    private Map<String, String> getValidationErrors(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : result.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return errors;
    }
}
