package com.demo.project.repository;

import com.demo.project.entity.Appointment;

// import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    // Find appointments by customer ID
    List<Appointment> findByCustomer_CustomerId(Integer customerId);

    // Find appointments by exact date
    List<Appointment> findByAppointmentDate(LocalDate appointmentDate);

    
    @Query("SELECT a FROM Appointment a WHERE a.status = 'Completed'")
    List<Appointment> findCompletedAppointments();

      @Query("SELECT a FROM Appointment a WHERE a.appointmentDate >= :currentDate")
    List<Appointment> findUpcomingAppointments(@Param("currentDate") LocalDate currentDate);

   
}