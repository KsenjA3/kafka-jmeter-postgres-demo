package com.test.service;

import com.test.model.Message;
import com.test.repository.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    // Получить все сообщения
    @Transactional(readOnly = true)
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    // Получить сообщения по имени метрики
    @Transactional(readOnly = true)
    public List<Message> getMessagesByName(String name) {
        return messageRepository.findByName(name);
    }

    // Получить сообщение по ID
    @Transactional(readOnly = true)
    public Optional<Message> getMessageById(Long id) {
        return messageRepository.findById(id);
    }

    // Получить количество всех сообщений
    @Transactional(readOnly = true)
    public long getMessageCount() {
        return messageRepository.count();
    }

    // Сохранить новое сообщение
    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    // Очистить все сообщения
    public void clearAllMessages() {
        messageRepository.deleteAll();
    }
} 