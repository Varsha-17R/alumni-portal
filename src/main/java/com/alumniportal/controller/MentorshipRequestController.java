package com.alumniportal.controller;

import com.alumniportal.entity.MentorshipRequest;
import com.alumniportal.entity.RequestStatus;
import com.alumniportal.entity.User;
import com.alumniportal.repository.UserRepository;
import com.alumniportal.service.MentorshipRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mentorship")
@CrossOrigin(origins = "*")
public class MentorshipRequestController {

    private final MentorshipRequestService mentorshipRequestService;
    private final UserRepository userRepository;

    public MentorshipRequestController(
            MentorshipRequestService mentorshipRequestService,
            UserRepository userRepository) {

        this.mentorshipRequestService = mentorshipRequestService;
        this.userRepository = userRepository;
    }

    // =========================================================
    // CREATE MENTORSHIP REQUEST
    // =========================================================

    @PostMapping("/requests")
    public ResponseEntity<?> createRequest(
            @RequestBody Map<String, Object> data) {

        try {

            // Get student object
            Map<String, Object> studentData =
                    (Map<String, Object>) data.get("student");

            // Get alumni object
            Map<String, Object> alumniData =
                    (Map<String, Object>) data.get("alumni");

            if (studentData == null || alumniData == null) {

                return ResponseEntity
                        .badRequest()
                        .body("Student and alumni are required.");
            }

            // Get IDs only
            Number studentIdNumber =
                    (Number) studentData.get("id");

            Number alumniIdNumber =
                    (Number) alumniData.get("id");

            if (studentIdNumber == null || alumniIdNumber == null) {

                return ResponseEntity
                        .badRequest()
                        .body("Student ID and alumni ID are required.");
            }

            Long studentId =
                    studentIdNumber.longValue();

            Long alumniId =
                    alumniIdNumber.longValue();

            // Find actual users from database
            User student =
                    userRepository
                            .findById(studentId)
                            .orElse(null);

            User alumni =
                    userRepository
                            .findById(alumniId)
                            .orElse(null);

            if (student == null) {

                return ResponseEntity
                        .badRequest()
                        .body("Student not found.");
            }

            if (alumni == null) {

                return ResponseEntity
                        .badRequest()
                        .body("Alumni not found.");
            }

            // Create request using database users
            MentorshipRequest request =
                    new MentorshipRequest();

            request.setStudent(student);
            request.setAlumni(alumni);
            request.setStatus(RequestStatus.PENDING);

            MentorshipRequest savedRequest =
                    mentorshipRequestService
                            .createRequest(request);

            return ResponseEntity.ok(savedRequest);

        } catch (IllegalStateException e) {

            return ResponseEntity
                    .status(409)
                    .body(e.getMessage());

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .badRequest()
                    .body("Unable to create mentorship request.");
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