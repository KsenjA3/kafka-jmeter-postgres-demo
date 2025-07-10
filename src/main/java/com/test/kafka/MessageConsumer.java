package com.test.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.model.Message;
import com.test.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class MessageConsumer {
    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);
    private final MessageRepository messageRepository;

    public MessageConsumer(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @KafkaListener(topics = "${kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(@Payload String messageJson,
                      @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                      @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                      @Header(KafkaHeaders.OFFSET) long offset) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(messageJson);
            for (JsonNode metric : root.path("metrics")) {
                Message msg = Message.builder()
                    .name(metric.path("name").asText())
                    .timestamp(metric.path("timestamp").asLong())
                    .dataType(metric.path("dataType").asText())
                    .value(metric.path("value").asText())
                    .build();
                messageRepository.save(msg);
            }
        } catch (Exception e) {
            logger.error("Error processing message from Kafka: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process Kafka message", e);
        }
    }

    @KafkaListener(topics = "${kafka.topic}.dlq", groupId = "${spring.kafka.consumer.group-id}.dlq")
    public void listenDeadLetterQueue(@Payload String message) {
        logger.warn("Processing message from dead letter queue: {}", message);
        // Handle failed messages
    }
} 