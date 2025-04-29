package com.healthcare.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healthcare.model.Appointment;
import com.healthcare.model.Appointment.AppointmentStatus;
import com.healthcare.model.User;
import com.healthcare.repository.AppointmentRepository;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Transactional(readOnly = true)
    public List<Appointment> getAllAppointmentsByUser(User user) {
        return appointmentRepository.findByUserOrderByAppointmentDateTimeDesc(user);
    }
    
    @Transactional(readOnly = true)
    public List<Appointment> getRecentAppointmentsByUser(User user) {
        return appointmentRepository.findTop5ByUserOrderByAppointmentDateTimeDesc(user);
    }
    
    @Transactional(readOnly = true)
    public List<Appointment> getUpcomingAppointmentsByUser(User user) {
        return appointmentRepository.findByUserAndAppointmentDateTimeAfterOrderByAppointmentDateTime(
                user, LocalDateTime.now());
    }
    
    @Transactional(readOnly = true)
    public List<Appointment> getAppointmentsByUserAndStatus(User user, AppointmentStatus status) {
        return appointmentRepository.findByUserAndStatusOrderByAppointmentDateTimeDesc(user, status);
    }
    
    @Transactional(readOnly = true)
    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }
    
    @Transactional
    public Appointment saveAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }
    
    @Transactional
    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }
    
    @Transactional
    public Appointment updateAppointmentStatus(Long id, AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }
}