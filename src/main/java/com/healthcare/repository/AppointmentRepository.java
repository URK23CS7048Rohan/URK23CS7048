package com.healthcare.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.healthcare.model.Appointment;
import com.healthcare.model.User;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    List<Appointment> findByUserOrderByAppointmentDateTimeDesc(User user);
    
    List<Appointment> findTop5ByUserOrderByAppointmentDateTimeDesc(User user);
    
    List<Appointment> findByUserAndAppointmentDateTimeAfterOrderByAppointmentDateTime(User user, LocalDateTime dateTime);
    
    List<Appointment> findByUserAndStatusOrderByAppointmentDateTimeDesc(User user, Appointment.AppointmentStatus status);
}