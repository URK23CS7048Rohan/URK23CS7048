package com.healthcare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.healthcare.model.HealthRecord;
import com.healthcare.model.User;

@Repository
public interface HealthRecordRepository extends JpaRepository<HealthRecord, Long> {
    
    List<HealthRecord> findByUserOrderByRecordDateDesc(User user);
    
    List<HealthRecord> findTop5ByUserOrderByRecordDateDesc(User user);
}