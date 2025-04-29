package com.healthcare.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.healthcare.model.User;
import com.healthcare.service.AppointmentService;
import com.healthcare.service.HealthRecordService;
import com.healthcare.service.UserService;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private HealthRecordService healthRecordService;
    
    @Autowired
    private AppointmentService appointmentService;
    
    @GetMapping
    public String dashboard(Model model, Principal principal) {
        User user = userService.getUserByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Add recent health records to the model
        model.addAttribute("recentHealthRecords", healthRecordService.getRecentHealthRecordsByUser(user));
        
        // Add upcoming appointments to the model
        model.addAttribute("upcomingAppointments", appointmentService.getUpcomingAppointmentsByUser(user));
        
        return "dashboard";
    }
}