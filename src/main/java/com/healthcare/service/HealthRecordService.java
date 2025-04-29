package com.healthcare.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healthcare.model.HealthRecord;
import com.healthcare.model.User;
import com.healthcare.repository.HealthRecordRepository;

@Service
public class HealthRecordService {

    @Autowired
    private HealthRecordRepository healthRecordRepository;
    
    @Transactional(readOnly = true)
    public List<HealthRecord> getAllHealthRecordsByUser(User user) {
        return healthRecordRepository.findByUserOrderByRecordDateDesc(user);
    }
    
    @Transactional(readOnly = true)
    public List<HealthRecord> getRecentHealthRecordsByUser(User user) {
        return healthRecordRepository.findTop5ByUserOrderByRecordDateDesc(user);
    }
    
    @Transactional(readOnly = true)
    public Optional<HealthRecord> getHealthRecordById(Long id) {
        return healthRecordRepository.findById(id);
    }
    
    @Transactional
    public HealthRecord saveHealthRecord(HealthRecord healthRecord) {
        return healthRecordRepository.save(healthRecord);
    }
    
    @Transactional
    public void deleteHealthRecord(Long id) {
        healthRecordRepository.deleteById(id);
    }
}