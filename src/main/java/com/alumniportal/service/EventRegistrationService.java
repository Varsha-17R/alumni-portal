package com.alumniportal.service;

import com.alumniportal.entity.EventRegistration;
import com.alumniportal.entity.RegistrationStatus;
import com.alumniportal.repository.EventRegistrationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventRegistrationService {

    private final EventRegistrationRepository
            eventRegistrationRepository;

    public EventRegistrationService(
            EventRegistrationRepository eventRegistrationRepository) {

        this.eventRegistrationRepository =
                eventRegistrationRepository;
    }

    // =========================================================
    // CREATE REGISTRATION
    // =========================================================

    public EventRegistration registerForEvent(
            EventRegistration registration) {

        Long eventId =
                registration.getEvent().getId();

        Long studentId =
                registration.getStudent().getId();

        // Check duplicate registration
        boolean alreadyRegistered =
                eventRegistrationRepository
                        .existsByEventIdAndStudentId(
                                eventId,
                                studentId
                        );

        if (alreadyRegistered) {

            throw new IllegalStateException(
                    "You have already registered for this event."
            );
        }

        // New registration
        registration.setStatus(
                RegistrationStatus.REGISTERED
        );

        return eventRegistrationRepository.save(
                registration
        );
    }

    // =========================================================
    // GET ALL REGISTRATIONS
    // =========================================================

    public List<EventRegistration>
    getAllRegistrations() {

        return eventRegistrationRepository.findAll();
    }

    // =========================================================
    // GET REGISTRATION BY ID
    // =========================================================

    public EventRegistration
    getRegistrationById(Long id) {

        return eventRegistrationRepository
                .findById(id)
                .orElse(null);
    }

    // =========================================================
    // GET REGISTRATIONS BY STUDENT
    // =========================================================

    public List<EventRegistration>
    getRegistrationsByStudent(Long studentId) {

        return eventRegistrationRepository
                .findByStudentId(studentId);
    }

    // =========================================================
    // GET PARTICIPANTS OF EVENT
    // =========================================================

    public List<EventRegistration>
    getParticipantsByEvent(Long eventId) {

        return eventRegistrationRepository
                .findByEventId(eventId);
    }

    // =========================================================
    // CANCEL REGISTRATION
    // =========================================================

    public EventRegistration
    cancelRegistration(Long id) {

        EventRegistration registration =
                eventRegistrationRepository
                        .findById(id)
                        .orElse(null);

        if (registration == null) {
            return null;
        }

        registration.setStatus(
                RegistrationStatus.CANCELLED
        );

        return eventRegistrationRepository.save(
                registration
        );
    }

    // =========================================================
    // DELETE REGISTRATION
    // =========================================================

    public void deleteRegistration(Long id) {

        eventRegistrationRepository.deleteById(id);
    }
}