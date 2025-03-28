package com.demo.project.service;

import com.demo.project.entity.Appointment;
import com.demo.project.entity.Customer;
import com.demo.project.repository.AppointmentRepository;
import com.demo.project.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Appointment saveAppointment(Appointment appointment) {
        validateAppointment(appointment);
        return appointmentRepository.save(appointment);
    }
    public List<Appointment> saveBulkAppointments(List<Appointment> appointments) {
        return appointmentRepository.saveAll(appointments);
    }
    

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Appointment getAppointmentById(int id) {
        return appointmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Appointment not found with ID: " + id));
    }

    public List<Appointment> getAppointmentsByCustomerId(int customerId) {
        return appointmentRepository.findByCustomer_CustomerId(customerId);
    }

    public List<Appointment> getAppointmentsByDate(LocalDate date) {
        return appointmentRepository.findByAppointmentDate(date);
    }

    public List<Appointment> getCompletedAppointments() {
        return appointmentRepository.findCompletedAppointments();
    }

    public Appointment updateAppointment(int id, Appointment updatedAppointment) {
        Appointment existingAppointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with ID: " + id));

        validateAppointment(updatedAppointment);

        if (updatedAppointment.getCustomer() != null) {
            Customer customer = customerRepository.findById(updatedAppointment.getCustomer().getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + updatedAppointment.getCustomer().getCustomerId()));
            existingAppointment.setCustomer(customer);
        }

        existingAppointment.setAppointmentDate(updatedAppointment.getAppointmentDate());
        existingAppointment.setServiceType(updatedAppointment.getServiceType());
        existingAppointment.setStatus(updatedAppointment.getStatus());
        existingAppointment.setPrice(updatedAppointment.getPrice());

        return appointmentRepository.save(existingAppointment);
    }

    public void deleteAppointment(int id) {
        if (!appointmentRepository.existsById(id)) {
            throw new RuntimeException("Appointment with ID " + id + " not found!");
        }
        appointmentRepository.deleteById(id);
    }

    public List<Appointment> getUpcomingAppointments() {
        return appointmentRepository.findUpcomingAppointments(LocalDate.now());
    }

    public List<Appointment> getSortedAppointments(String sortBy, String order) {
        Sort sort = order.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        return appointmentRepository.findAll(sort);
    }

    private void validateAppointment(Appointment appointment) {
        if (appointment.getAppointmentDate() == null) {
            throw new IllegalArgumentException("Appointment date cannot be null!");
        }
        if (appointment.getAppointmentDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Appointment date cannot be in the past!");
        }
        if (appointment.getPrice() == null || appointment.getPrice() < 100) {
            throw new IllegalArgumentException("Price must be at least 100!");
        }
    }
}
