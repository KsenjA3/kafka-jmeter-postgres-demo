package com.test.kafka;

import com.test.model.Message;
import com.test.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class MessageConsumer {
    
    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);
    
    private final MessageRepository messageRepository;
    
    public MessageConsumer(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }
    
    @KafkaListener(topics = "${kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(@Payload String message,
                      @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                      @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                      @Header(KafkaHeaders.OFFSET) long offset) {
        
        try {
            logger.info("Received message from topic {} partition {} offset {}: {}", 
                       topic, partition, offset, message);
            
            // Create message entity
            Message messageEntity = new Message();
            messageEntity.setContent(message);
            messageEntity.setSource("kafka-consumer");
            messageEntity.setSequenceNumber(System.currentTimeMillis());
            messageEntity.setCreatedAt(LocalDateTime.now());
            
            // Save to database
            Message savedMessage = messageRepository.save(messageEntity);
            
            logger.info("Message saved to database with ID: {}", savedMessage.getId());
            
        } catch (Exception e) {
            logger.error("Error processing message from Kafka: {}", e.getMessage(), e);
            // In a real application, you might want to implement dead letter queue
            throw new RuntimeException("Failed to process Kafka message", e);
        }
    }
    
    @KafkaListener(topics = "${kafka.topic}.dlq", groupId = "${spring.kafka.consumer.group-id}.dlq")
    public void listenDeadLetterQueue(@Payload String message) {
        logger.warn("Processing message from dead letter queue: {}", message);
        // Handle failed messages
    }
} 