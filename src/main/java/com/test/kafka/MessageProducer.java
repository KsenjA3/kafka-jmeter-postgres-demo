package com.test.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class MessageProducer {
    
    private static final Logger logger = LoggerFactory.getLogger(MessageProducer.class);
    
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic;
    
    public MessageProducer(KafkaTemplate<String, String> kafkaTemplate,
                          @Value("${kafka.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }
    
    //Упрощённая версия отправки: вызывает следующий метод с key = null
    public void sendMessage(String message) {
        sendMessage(message, null);
    }
    
    //Отправляет сообщение в Kafka с возможностью указать ключ (key)
    //Использует асинхронную отправку
    //С помощью whenComplete добавляется обработчик результат
    public void sendMessage(String message, String key) {
        try {
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, key, message);
            
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    logger.info("Message sent successfully to topic {}: partition={}, offset={}", 
                              topic, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
                } else {
                    logger.error("Failed to send message to topic {}: {}", topic, ex.getMessage());
                }
            });
            
        } catch (Exception e) {
            logger.error("Error sending message to Kafka: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to send message to Kafka", e);
        }
    }
    
    //Синхронная отправка сообщения (метод блокируется, пока не получит результат)
    public void sendMessageSync(String message) {
        try {
            SendResult<String, String> result = kafkaTemplate.send(topic, message).get();
            logger.info("Message sent synchronously to topic {}: partition={}, offset={}", 
                      topic, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
        } catch (Exception e) {
            logger.error("Error sending message synchronously to Kafka: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to send message to Kafka", e);
        }
    }
} 