package com.test.repository;

import com.test.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    // Поиск по имени метрики
    List<Message> findByName(String name);
} 