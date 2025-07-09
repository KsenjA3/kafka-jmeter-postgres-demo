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

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class MessageController {
    
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    
    private final MessageService messageService;
    
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }
    
    /**
     * Send message to Kafka
     * POST /api/messages
     */
    @PostMapping("/messages")
    public ResponseEntity<Map<String, Object>> sendMessage(@RequestBody Map<String, String> request) {
        try {
            String content = request.get("content");
            if (content == null || content.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Message content is required"));
            }
            
            messageService.sendMessage(content);
            
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Message sent to Kafka",
                "content", content
            ));
            
        } catch (Exception e) {
            logger.error("Error sending message: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to send message: " + e.getMessage()));
        }
    }
    
    /**
     * Get all messages from database
     * GET /api/messages
     */
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        try {
            List<Message> messages = messageService.getAllMessages();
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            logger.error("Error getting messages: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get messages by source
     * GET /api/messages/source/{source}
     */
    @GetMapping("/messages/source/{source}")
    public ResponseEntity<List<Message>> getMessagesBySource(@PathVariable String source) {
        try {
            List<Message> messages = messageService.getMessagesBySource(source);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            logger.error("Error getting messages by source: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get message count
     * GET /api/count
     */
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> getMessageCount() {
        try {
            long count = messageService.getMessageCount();
            return ResponseEntity.ok(Map.of(
                "count", count,
                "timestamp", System.currentTimeMillis()
            ));
        } catch (Exception e) {
            logger.error("Error getting message count: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to get message count"));
        }
    }
    
    /**
     * Get statistics
     * GET /api/statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        try {
            Map<String, Object> statistics = messageService.getStatistics();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            logger.error("Error getting statistics: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to get statistics"));
        }
    }
    
    /**
     * Health check
     * GET /api/health
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        try {
            long messageCount = messageService.getMessageCount();
            return ResponseEntity.ok(Map.of(
                "status", "healthy",
                "service", "kafka-jmeter-postgres-demo",
                "messageCount", messageCount,
                "timestamp", System.currentTimeMillis()
            ));
        } catch (Exception e) {
            logger.error("Health check failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                    "status", "unhealthy",
                    "error", e.getMessage()
                ));
        }
    }
    
    /**
     * Clear all messages (for testing)
     * DELETE /api/messages
     */
    @DeleteMapping("/messages")
    public ResponseEntity<Map<String, Object>> clearAllMessages() {
        try {
            messageService.clearAllMessages();
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "All messages cleared"
            ));
        } catch (Exception e) {
            logger.error("Error clearing messages: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to clear messages"));
        }
    }
    
    /**
     * Legacy endpoint for JMeter compatibility
     * POST /send
     */
    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> sendMessageLegacy(@RequestBody String content) {
        try {
            if (content == null || content.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Message content is required"));
            }
            
            messageService.sendMessage(content);
            
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "messageId", java.util.UUID.randomUUID().toString(),
                "count", messageService.getMessageCount()
            ));
            
        } catch (Exception e) {
            logger.error("Error sending message (legacy): {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Internal server error: " + e.getMessage()));
        }
    }
    
    /**
     * Legacy endpoint for JMeter compatibility
     * GET /query
     */
    @GetMapping("/query")
    public ResponseEntity<Map<String, Object>> queryLegacy() {
        try {
            long totalMessages = messageService.getMessageCount();
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "query", "SELECT * FROM messages",
                "totalMessages", totalMessages,
                "timestamp", System.currentTimeMillis()
            ));
        } catch (Exception e) {
            logger.error("Error processing query request (legacy): {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Internal server error: " + e.getMessage()));
        }
    }
} 