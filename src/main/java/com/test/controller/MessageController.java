package com.test.controller;

import com.test.model.Message;
import com.test.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import com.test.kafka.MessageProducer;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    private final MessageService messageService;
    private final MessageProducer messageProducer;

    public MessageController(MessageService messageService, MessageProducer messageProducer) {
        this.messageService = messageService;
        this.messageProducer = messageProducer;
    }

    // Приём массива метрик или одной метрики (JSON) и сохранение в БД
    @PostMapping("/messages")
    public ResponseEntity<?> saveMessages(@RequestBody String metricsJson) {
        try {
//            logger.info("MessageController - metricsJson: " + metricsJson);
            messageProducer.sendMessage(metricsJson); // Отправляем в Kafka
            return ResponseEntity.ok(Map.of("status", "success", "message", "Message sent to Kafka"));
        } catch (Exception e) {
            logger.error("Error sending message to Kafka: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    // Получить все сообщения
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    // Получить сообщение по id
    @GetMapping("/messages/{id}")
    public ResponseEntity<?> getMessageById(@PathVariable Long id) {
        Optional<Message> msg = messageService.getMessageById(id);
        return msg.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Получить количество сообщений
    @GetMapping("/messages/count")
    public ResponseEntity<Map<String, Object>> getMessageCount() {
        long count = messageService.getMessageCount();
        return ResponseEntity.ok(Map.of("count", count));
    }

    // Удалить все сообщения
    @DeleteMapping("/messages")
    public ResponseEntity<Map<String, Object>> clearAllMessages() {
        messageService.clearAllMessages();
        return ResponseEntity.ok(Map.of("status", "success", "message", "All messages cleared"));
    }
} 