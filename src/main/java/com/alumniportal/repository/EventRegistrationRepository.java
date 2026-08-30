package com.alumniportal.repository;

import com.alumniportal.entity.EventRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRegistrationRepository
        extends JpaRepository<EventRegistration, Long> {

    // Get registrations by student
    List<EventRegistration> findByStudentId(Long studentId);

    // Get registrations for an event
    List<EventRegistration> findByEventId(Long eventId);

    // Check whether student already registered
    boolean existsByEventIdAndStudentId(
            Long eventId,
            Long studentId
    );
}