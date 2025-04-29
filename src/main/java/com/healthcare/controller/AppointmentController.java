package com.healthcare.controller;

import java.security.Principal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.healthcare.model.Appointment;
import com.healthcare.model.Appointment.AppointmentStatus;
import com.healthcare.model.User;
import com.healthcare.service.AppointmentService;
import com.healthcare.service.UserService;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public String getAllAppointments(Model model, Principal principal) {
        User user = userService.getUserByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        model.addAttribute("appointments", appointmentService.getAllAppointmentsByUser(user));
        return "appointments/list";
    }
    
    @GetMapping("/upcoming")
    public String getUpcomingAppointments(Model model, Principal principal) {
        User user = userService.getUserByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        model.addAttribute("appointments", appointmentService.getUpcomingAppointmentsByUser(user));
        return "appointments/upcoming";
    }
    
    @GetMapping("/new")
    public String showNewAppointmentForm(Model model) {
        model.addAttribute("appointment", new Appointment());
        model.addAttribute("statuses", AppointmentStatus.values());
        return "appointments/form";
    }
    
    @PostMapping("/save")
    public String saveAppointment(@ModelAttribute Appointment appointment, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            appointment.setUser(user);
            if (appointment.getAppointmentDateTime() == null) {
                appointment.setAppointmentDateTime(LocalDateTime.now());
            }
            
            appointmentService.saveAppointment(appointment);
            redirectAttributes.addFlashAttribute("successMessage", "Appointment saved successfully");
            return "redirect:/appointments";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/appointments/new";
        }
    }
    
    @GetMapping("/edit/{id}")
    public String showEditAppointmentForm(@PathVariable Long id, Model model, Principal principal) {
        User user = userService.getUserByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Appointment appointment = appointmentService.getAppointmentById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        
        // Security check to ensure the user owns this appointment
        if (!appointment.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You don't have permission to edit this appointment");
        }
        
        model.addAttribute("appointment", appointment);
        model.addAttribute("statuses", AppointmentStatus.values());
        return "appointments/form";
    }
    
    @GetMapping("/status/{id}/{status}")
    public String updateAppointmentStatus(@PathVariable Long id, @PathVariable AppointmentStatus status, 
            Principal principal, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            Appointment appointment = appointmentService.getAppointmentById(id)
                    .orElseThrow(() -> new RuntimeException("Appointment not found"));
            
            // Security check to ensure the user owns this appointment
            if (!appointment.getUser().getId().equals(user.getId())) {
                throw new RuntimeException("You don't have permission to update this appointment");
            }
            
            appointmentService.updateAppointmentStatus(id, status);
            redirectAttributes.addFlashAttribute("successMessage", "Appointment status updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/appointments";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteAppointment(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            Appointment appointment = appointmentService.getAppointmentById(id)
                    .orElseThrow(() -> new RuntimeException("Appointment not found"));
            
            // Security check to ensure the user owns this appointment
            if (!appointment.getUser().getId().equals(user.getId())) {
                throw new RuntimeException("You don't have permission to delete this appointment");
            }
            
            appointmentService.deleteAppointment(id);
            redirectAttributes.addFlashAttribute("successMessage", "Appointment deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/appointments";
    }
}