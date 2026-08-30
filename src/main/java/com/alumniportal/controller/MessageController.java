package com.alumniportal.controller;

import com.alumniportal.entity.Message;
import com.alumniportal.entity.User;
import com.alumniportal.service.MessageService;
import com.alumniportal.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;

    public MessageController(
            MessageService messageService,
            UserService userService) {

        this.messageService = messageService;
        this.userService = userService;
    }


    // =========================================================
    // SEND MESSAGE
    // =========================================================

    @PostMapping
    public ResponseEntity<Message> sendMessage(
            @RequestBody Message message,
            Principal principal) {

        // Get currently logged-in user
        User sender =
                userService.getUserByEmail(principal.getName());

        if (sender == null) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        // Set sender from logged-in account
        message.setSender(sender);

        // Validate receiver
        if (message.getReceiver() == null ||
                message.getReceiver().getId() == null) {

            return ResponseEntity
                    .badRequest()
                    .build();
        }

        // Get actual receiver from database
        User receiver =
                userService.getUserById(
                        message.getReceiver().getId()
                );

        if (receiver == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        message.setReceiver(receiver);

        // Save message
        Message savedMessage =
                messageService.sendMessage(message);

        return ResponseEntity.ok(savedMessage);
    }


    // =========================================================
    // GET ALL MESSAGES
    // =========================================================

    @GetMapping
    public ResponseEntity<List<Message>> getAllMessages() {

        return ResponseEntity.ok(
                messageService.getAllMessages()
        );
    }


    // =========================================================
    // GET MESSAGE BY ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessageById(
            @PathVariable Long id) {

        Message message =
                messageService.getMessageById(id);

        if (message == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(message);
    }


    // =========================================================
    // GET SENT MESSAGES
    // =========================================================

    @GetMapping("/sent/{senderId}")
    public ResponseEntity<List<Message>> getSentMessages(
            @PathVariable Long senderId) {

        return ResponseEntity.ok(
                messageService.getSentMessages(senderId)
        );
    }


    // =========================================================
    // GET RECEIVED MESSAGES
    // =========================================================

    @GetMapping("/received/{receiverId}")
    public ResponseEntity<List<Message>> getReceivedMessages(
            @PathVariable Long receiverId) {

        return ResponseEntity.ok(
                messageService.getReceivedMessages(receiverId)
        );
    }


    // =========================================================
    // GET CONVERSATION
    // =========================================================

    @GetMapping("/conversation/{user1}/{user2}")
    public ResponseEntity<List<Message>> getConversation(
            @PathVariable Long user1,
            @PathVariable Long user2) {

        return ResponseEntity.ok(
                messageService.getConversation(
                        user1,
                        user2
                )
        );
    }


    // =========================================================
    // DELETE MESSAGE
    // =========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(
            @PathVariable Long id) {

        Message message =
                messageService.getMessageById(id);

        if (message == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        messageService.deleteMessage(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}