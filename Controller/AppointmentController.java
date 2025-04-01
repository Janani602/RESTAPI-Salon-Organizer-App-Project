package com.demo.project.controller;

import com.demo.project.entity.Appointment;
import com.demo.project.service.AppointmentService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<Object> createAppointment(@Valid @RequestBody Appointment appointment, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(getValidationErrors(result));
        }
        return ResponseEntity.ok(appointmentService.saveAppointment(appointment));
    }

    @PostMapping("/bulk")
public ResponseEntity<Object> createBulkAppointments(@Valid @RequestBody List<Appointment> appointments, BindingResult result) {
    if (result.hasErrors()) {
        return ResponseEntity.badRequest().body(getValidationErrors(result));
    }
    return ResponseEntity.ok(appointmentService.saveBulkAppointments(appointments));
}

    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getAppointmentById(@PathVariable int id) {
        try {
            Appointment appointment = appointmentService.getAppointmentById(id);  // No Optional handling needed
            return ResponseEntity.ok(appointment);
        } catch (RuntimeException e) {  // Handle exception when appointment is not found
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }
    

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByCustomerId(@PathVariable int customerId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByCustomerId(customerId));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<Appointment>> getAppointmentsByDate(@PathVariable String date) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDate(LocalDate.parse(date)));
    }

    @GetMapping("/completed")
    public ResponseEntity<List<Appointment>> getCompletedAppointments() {
        return ResponseEntity.ok(appointmentService.getCompletedAppointments());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateAppointment(@PathVariable int id,@Valid @RequestBody Appointment appointment, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(getValidationErrors(result));
        }
        return ResponseEntity.ok(appointmentService.updateAppointment(id, appointment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable int id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.ok("Appointment with ID " + id + " has been deleted.");
    }
    
  private Map<String, String> getValidationErrors(BindingResult result) {
    Map<String, String> errors = new HashMap<>();
    for (FieldError error : result.getFieldErrors()) {
        errors.put(error.getField(), error.getDefaultMessage());
    }
    return errors;
  }
    @GetMapping("/upcoming")
    public ResponseEntity<List<Appointment>> getUpcomingAppointments() {
        return ResponseEntity.ok(appointmentService.getUpcomingAppointments());
    }

    @GetMapping("/sort")
    public ResponseEntity<List<Appointment>> getSortedAppointments(
            @RequestParam(defaultValue = "appointmentDate") String sortBy,
            @RequestParam(defaultValue = "asc") String order) {

        return ResponseEntity.ok(appointmentService.getSortedAppointments(sortBy, order));
    }
}