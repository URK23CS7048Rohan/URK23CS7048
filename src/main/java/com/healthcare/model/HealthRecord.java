package com.healthcare.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "health_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HealthRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private LocalDateTime recordDate;
    
    private Double bloodPressureSystolic;
    
    private Double bloodPressureDiastolic;
    
    private Double heartRate;
    
    private Double bloodSugar;
    
    private Double temperature;
    
    private Double weight;
    
    private String symptoms;
    
    private String diagnosis;
    
    private String medications;
    
    private String notes;
    
    @PrePersist
    protected void onCreate() {
        if (recordDate == null) {
            recordDate = LocalDateTime.now();
        }
    }
}