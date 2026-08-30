package com.alumniportal.controller;

import com.alumniportal.entity.MentorshipRequest;
import com.alumniportal.service.MentorshipRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mentorship")
@CrossOrigin(origins = "*")
public class MentorshipRequestController {

    private final MentorshipRequestService mentorshipRequestService;

    public MentorshipRequestController(
            MentorshipRequestService mentorshipRequestService) {

        this.mentorshipRequestService = mentorshipRequestService;
    }

    // =========================================================
    // CREATE MENTORSHIP REQUEST
    // =========================================================

    @PostMapping("/requests")
    public ResponseEntity<?> createRequest(
            @RequestBody MentorshipRequest request) {

        try {

            MentorshipRequest savedRequest =
                    mentorshipRequestService.createRequest(request);

            return ResponseEntity.ok(savedRequest);

        } catch (IllegalStateException e) {

            return ResponseEntity
                    .status(409)
                    .body(e.getMessage());
        }
    }

    // =========================================================
    // GET ALL MENTORSHIP REQUESTS
    // =========================================================

    @GetMapping("/requests")
    public ResponseEntity<List<MentorshipRequest>> getAllRequests() {

        return ResponseEntity.ok(
                mentorshipRequestService.getAllRequests()
        );
    }

    // =========================================================
    // GET MENTORSHIP REQUEST BY ID
    // =========================================================

    @GetMapping("/requests/{id}")
    public ResponseEntity<MentorshipRequest> getRequestById(
            @PathVariable Long id) {

        MentorshipRequest request =
                mentorshipRequestService.getRequestById(id);

        if (request == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(request);
    }

    // =========================================================
    // GET REQUESTS BY STUDENT
    // =========================================================

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<MentorshipRequest>> getStudentRequests(
            @PathVariable Long studentId) {

        return ResponseEntity.ok(
                mentorshipRequestService
                        .getRequestsByStudent(studentId)
        );
    }

    // =========================================================
    // GET REQUESTS RECEIVED BY ALUMNI
    // =========================================================

    @GetMapping("/alumni/{alumniId}")
    public ResponseEntity<List<MentorshipRequest>> getAlumniRequests(
            @PathVariable Long alumniId) {

        return ResponseEntity.ok(
                mentorshipRequestService
                        .getRequestsByAlumni(alumniId)
        );
    }

    // =========================================================
    // ACCEPT MENTORSHIP REQUEST
    // =========================================================

    @PutMapping("/requests/{id}/accept")
    public ResponseEntity<MentorshipRequest> acceptRequest(
            @PathVariable Long id) {

        MentorshipRequest request =
                mentorshipRequestService.acceptRequest(id);

        if (request == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(request);
    }

    // =========================================================
    // REJECT MENTORSHIP REQUEST
    // =========================================================

    @PutMapping("/requests/{id}/reject")
    public ResponseEntity<MentorshipRequest> rejectRequest(
            @PathVariable Long id) {

        MentorshipRequest request =
                mentorshipRequestService.rejectRequest(id);

        if (request == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(request);
    }

    // =========================================================
    // DELETE MENTORSHIP REQUEST
    // =========================================================

    @DeleteMapping("/requests/{id}")
    public ResponseEntity<Void> deleteRequest(
            @PathVariable Long id) {

        MentorshipRequest request =
                mentorshipRequestService.getRequestById(id);

        if (request == null) {
            return ResponseEntity.notFound().build();
        }

        mentorshipRequestService.deleteRequest(id);

        return ResponseEntity.noContent().build();
    }
}