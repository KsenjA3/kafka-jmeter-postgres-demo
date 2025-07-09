package com.test.repository;

import com.test.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    
    List<Message> findBySourceOrderByCreatedAtDesc(String source);
    
    @Query("SELECT COUNT(m) FROM Message m WHERE m.source = ?1")
    long countBySource(String source);
    
    @Query("SELECT m FROM Message m ORDER BY m.createdAt DESC")
    List<Message> findAllOrderByCreatedAtDesc();
    
    @Query("SELECT m FROM Message m WHERE m.sequenceNumber IS NOT NULL ORDER BY m.sequenceNumber DESC")
    List<Message> findAllWithSequenceNumberOrderBySequenceNumberDesc();
} 