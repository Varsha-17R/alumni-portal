package com.alumniportal.controller;

import com.alumniportal.entity.EventRegistration;
import com.alumniportal.service.EventRegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/event-registrations")
@CrossOrigin(origins = "*")
public class EventRegistrationController {

    private final EventRegistrationService
            eventRegistrationService;

    public EventRegistrationController(
            EventRegistrationService eventRegistrationService) {

        this.eventRegistrationService =
                eventRegistrationService;
    }

    // =========================================================
    // REGISTER FOR EVENT
    // =========================================================

    @PostMapping
    public ResponseEntity<?> registerForEvent(
            @RequestBody EventRegistration registration) {

        try {

            EventRegistration savedRegistration =
                    eventRegistrationService
                            .registerForEvent(registration);

            return ResponseEntity.ok(
                    savedRegistration
            );

        } catch (IllegalStateException e) {

            return ResponseEntity
                    .status(409)
                    .body(e.getMessage());
        }
    }

    // =========================================================
    // GET ALL REGISTRATIONS
    // =========================================================

    @GetMapping
    public ResponseEntity<List<EventRegistration>>
    getAllRegistrations() {

        return ResponseEntity.ok(
                eventRegistrationService
                        .getAllRegistrations()
        );
    }

    // =========================================================
    // GET REGISTRATION BY ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<EventRegistration>
    getRegistrationById(
            @PathVariable Long id) {

        EventRegistration registration =
                eventRegistrationService
                        .getRegistrationById(id);

        if (registration == null) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(
                registration
        );
    }

    // =========================================================
    // GET STUDENT REGISTRATIONS
    // =========================================================

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EventRegistration>>
    getRegistrationsByStudent(
            @PathVariable Long studentId) {

        return ResponseEntity.ok(
                eventRegistrationService
                        .getRegistrationsByStudent(
                                studentId
                        )
        );
    }

    // =========================================================
    // GET EVENT PARTICIPANTS
    // =========================================================

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<EventRegistration>>
    getParticipantsByEvent(
            @PathVariable Long eventId) {

        return ResponseEntity.ok(
                eventRegistrationService
                        .getParticipantsByEvent(
                                eventId
                        )
        );
    }

    // =========================================================
    // CANCEL REGISTRATION
    // =========================================================

    @PutMapping("/{id}/cancel")
    public ResponseEntity<EventRegistration>
    cancelRegistration(
            @PathVariable Long id) {

        EventRegistration registration =
                eventRegistrationService
                        .cancelRegistration(id);

        if (registration == null) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(
                registration
        );
    }

    // =========================================================
    // DELETE REGISTRATION
    // =========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    deleteRegistration(
            @PathVariable Long id) {

        EventRegistration registration =
                eventRegistrationService
                        .getRegistrationById(id);

        if (registration == null) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        eventRegistrationService
                .deleteRegistration(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}