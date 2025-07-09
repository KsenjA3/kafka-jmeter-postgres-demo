package com.test.service;

import com.test.kafka.MessageProducer;
import com.test.model.Message;
import com.test.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class MessageService {
    
    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);
    
    private final MessageProducer messageProducer;
    private final MessageRepository messageRepository;
    
    public MessageService(MessageProducer messageProducer, MessageRepository messageRepository) {
        this.messageProducer = messageProducer;
        this.messageRepository = messageRepository;
    }
    
    /**
     * Send message to Kafka
     */
    public void sendMessage(String content) {
        sendMessage(content, "jmeter-test", null);
    }
    
    /**
     * Send message to Kafka with source and sequence number
     */
    public void sendMessage(String content, String source, Long sequenceNumber) {
        try {
            // Create message for database
            Message message = new Message();
            message.setContent(content);
            message.setSource(source);
            message.setSequenceNumber(sequenceNumber);
            message.setCreatedAt(LocalDateTime.now());
            
            // Send to Kafka
            messageProducer.sendMessage(content);
            
            logger.info("Message sent to Kafka: {}", content);
            
        } catch (Exception e) {
            logger.error("Error sending message to Kafka: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to send message", e);
        }
    }
    
    /**
     * Get all messages from database
     */
    @Transactional(readOnly = true)
    public List<Message> getAllMessages() {
        return messageRepository.findAllOrderByCreatedAtDesc();
    }
    
    /**
     * Get messages by source
     */
    @Transactional(readOnly = true)
    public List<Message> getMessagesBySource(String source) {
        return messageRepository.findBySourceOrderByCreatedAtDesc(source);
    }
    
    /**
     * Get message count
     */
    @Transactional(readOnly = true)
    public long getMessageCount() {
        return messageRepository.count();
    }
    
    /**
     * Get message count by source
     */
    @Transactional(readOnly = true)
    public long getMessageCountBySource(String source) {
        return messageRepository.countBySource(source);
    }
    
    /**
     * Get messages with sequence numbers
     */
    @Transactional(readOnly = true)
    public List<Message> getMessagesWithSequenceNumber() {
        return messageRepository.findAllWithSequenceNumberOrderBySequenceNumberDesc();
    }
    
    /**
     * Get statistics
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getStatistics() {
        long totalCount = messageRepository.count();
        long kafkaCount = messageRepository.countBySource("kafka-consumer");
        long jmeterCount = messageRepository.countBySource("jmeter-test");
        
        return Map.of(
            "totalMessages", totalCount,
            "kafkaMessages", kafkaCount,
            "jmeterMessages", jmeterCount,
            "timestamp", LocalDateTime.now()
        );
    }
    
    /**
     * Clear all messages (for testing)
     */
    public void clearAllMessages() {
        messageRepository.deleteAll();
        logger.info("All messages cleared from database");
    }
} 