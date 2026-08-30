package com.alumniportal.service;

import com.alumniportal.entity.Message;
import com.alumniportal.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    // =========================================================
    // SEND MESSAGE
    // =========================================================

    public Message sendMessage(Message message) {

        // Automatically set the message date and time
        message.setSentAt(LocalDateTime.now());

        return messageRepository.save(message);
    }

    // =========================================================
    // GET ALL MESSAGES
    // =========================================================

    public List<Message> getAllMessages() {

        return messageRepository.findAll();
    }

    // =========================================================
    // GET MESSAGE BY ID
    // =========================================================

    public Message getMessageById(Long id) {

        return messageRepository
                .findById(id)
                .orElse(null);
    }

    // =========================================================
    // GET SENT MESSAGES
    // =========================================================

    public List<Message> getSentMessages(Long senderId) {

        return messageRepository
                .findBySenderId(senderId);
    }

    // =========================================================
    // GET RECEIVED MESSAGES
    // =========================================================

    public List<Message> getReceivedMessages(Long receiverId) {

        return messageRepository
                .findByReceiverId(receiverId);
    }

    // =========================================================
    // GET CONVERSATION
    // =========================================================

    public List<Message> getConversation(
            Long user1,
            Long user2) {

        return messageRepository
                .findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(
                        user1,
                        user2,
                        user1,
                        user2
                );
    }

    // =========================================================
    // DELETE MESSAGE
    // =========================================================

    public void deleteMessage(Long id) {

        messageRepository.deleteById(id);
    }
}