package com.healthcare.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.healthcare.model.HealthRecord;
import com.healthcare.model.User;
import com.healthcare.service.HealthRecordService;
import com.healthcare.service.UserService;

@Controller
@RequestMapping("/health-records")
public class HealthRecordController {

    @Autowired
    private HealthRecordService healthRecordService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public String getAllHealthRecords(Model model, Principal principal) {
        User user = userService.getUserByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        model.addAttribute("healthRecords", healthRecordService.getAllHealthRecordsByUser(user));
        return "health-records/list";
    }
    
    @GetMapping("/new")
    public String showNewHealthRecordForm(Model model) {
        model.addAttribute("healthRecord", new HealthRecord());
        return "health-records/form";
    }
    
    @PostMapping("/save")
    public String saveHealthRecord(@ModelAttribute HealthRecord healthRecord, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            healthRecord.setUser(user);
            healthRecordService.saveHealthRecord(healthRecord);
            redirectAttributes.addFlashAttribute("successMessage", "Health record saved successfully");
            return "redirect:/health-records";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/health-records/new";
        }
    }
    
    @GetMapping("/edit/{id}")
    public String showEditHealthRecordForm(@PathVariable Long id, Model model, Principal principal) {
        User user = userService.getUserByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        HealthRecord healthRecord = healthRecordService.getHealthRecordById(id)
                .orElseThrow(() -> new RuntimeException("Health record not found"));
        
        // Security check to ensure the user owns this health record
        if (!healthRecord.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You don't have permission to edit this health record");
        }
        
        model.addAttribute("healthRecord", healthRecord);
        return "health-records/form";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteHealthRecord(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            HealthRecord healthRecord = healthRecordService.getHealthRecordById(id)
                    .orElseThrow(() -> new RuntimeException("Health record not found"));
            
            // Security check to ensure the user owns this health record
            if (!healthRecord.getUser().getId().equals(user.getId())) {
                throw new RuntimeException("You don't have permission to delete this health record");
            }
            
            healthRecordService.deleteHealthRecord(id);
            redirectAttributes.addFlashAttribute("successMessage", "Health record deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/health-records";
    }
}