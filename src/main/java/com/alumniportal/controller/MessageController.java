package com.alumniportal.controller;

import com.alumniportal.entity.Message;
import com.alumniportal.entity.User;
import com.alumniportal.service.MessageService;
import com.alumniportal.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
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
    public ResponseEntity<?> sendMessage(
            @RequestBody Map<String, Object> data,
            Principal principal) {

        try {

            // Get currently logged-in user
            User sender =
                    userService.getUserByEmail(
                            principal.getName()
                    );

            if (sender == null) {
                return ResponseEntity
                        .badRequest()
                        .body("Sender not found.");
            }

            // Get receiver data from request
            Map<String, Object> receiverData =
                    (Map<String, Object>) data.get("receiver");

            if (receiverData == null) {

                return ResponseEntity
                        .badRequest()
                        .body("Receiver is required.");
            }

            Number receiverIdNumber =
                    (Number) receiverData.get("id");

            if (receiverIdNumber == null) {

                return ResponseEntity
                        .badRequest()
                        .body("Receiver ID is required.");
            }

            Long receiverId =
                    receiverIdNumber.longValue();

            // Get actual receiver from database
            User receiver =
                    userService.getUserById(receiverId);

            if (receiver == null) {

                return ResponseEntity
                        .notFound()
                        .build();
            }

            // Get message content
            String content =
                    data.get("content") != null
                            ? data.get("content").toString()
                            : "";

            if (content.trim().isEmpty()) {

                return ResponseEntity
                        .badRequest()
                        .body("Message content is required.");
            }

            // Create message using real database users
            Message message =
                    new Message();

            message.setSender(sender);
            message.setReceiver(receiver);
            message.setContent(content);

            // Save message
            Message savedMessage =
                    messageService.sendMessage(message);

            return ResponseEntity.ok(savedMessage);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .badRequest()
                    .body("Unable to send message.");
        }
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