package com.alumniportal.repository;

import com.alumniportal.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository
        extends JpaRepository<Message, Long> {

    // Get messages sent by a user
    List<Message> findBySenderId(Long senderId);

    // Get messages received by a user
    List<Message> findByReceiverId(Long receiverId);

    // Get conversation between two users
    List<Message> findBySenderIdAndReceiverId(
            Long senderId,
            Long receiverId
    );

    // Get conversation in both directions
    List<Message> findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(
            Long senderId,
            Long receiverId,
            Long receiverId2,
            Long senderId2
    );
}
