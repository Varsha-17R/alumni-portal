package com.alumniportal.service;

import com.alumniportal.entity.MentorshipRequest;
import com.alumniportal.entity.RequestStatus;
import com.alumniportal.repository.MentorshipRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MentorshipRequestService {

    private final MentorshipRequestRepository mentorshipRequestRepository;

    public MentorshipRequestService(
            MentorshipRequestRepository mentorshipRequestRepository) {

        this.mentorshipRequestRepository =
                mentorshipRequestRepository;
    }

    // =========================================================
    // CREATE MENTORSHIP REQUEST
    // =========================================================

    public MentorshipRequest createRequest(
            MentorshipRequest request) {

        Long studentId =
                request.getStudent().getId();

        Long alumniId =
                request.getAlumni().getId();

        // Check whether the student already requested this alumni
        boolean alreadyRequested =
                mentorshipRequestRepository
                        .existsByStudentIdAndAlumniId(
                                studentId,
                                alumniId
                        );

        if (alreadyRequested) {

            throw new IllegalStateException(
                    "You have already sent a mentorship request to this alumni."
            );
        }

        // Every new request starts as PENDING
        request.setStatus(
                RequestStatus.PENDING
        );

        return mentorshipRequestRepository.save(
                request
        );
    }

    // =========================================================
    // GET ALL REQUESTS
    // =========================================================

    public List<MentorshipRequest> getAllRequests() {

        return mentorshipRequestRepository.findAll();
    }

    // =========================================================
    // GET REQUEST BY ID
    // =========================================================

    public MentorshipRequest getRequestById(Long id) {

        return mentorshipRequestRepository
                .findById(id)
                .orElse(null);
    }

    // =========================================================
    // GET REQUESTS BY STUDENT
    // =========================================================

    public List<MentorshipRequest> getRequestsByStudent(
            Long studentId) {

        return mentorshipRequestRepository
                .findByStudentId(studentId);
    }

    // =========================================================
    // GET REQUESTS BY ALUMNI
    // =========================================================

    public List<MentorshipRequest> getRequestsByAlumni(
            Long alumniId) {

        return mentorshipRequestRepository
                .findByAlumniId(alumniId);
    }

    // =========================================================
    // ACCEPT REQUEST
    // =========================================================

    public MentorshipRequest acceptRequest(Long id) {

        MentorshipRequest request =
                mentorshipRequestRepository
                        .findById(id)
                        .orElse(null);

        if (request == null) {
            return null;
        }

        request.setStatus(
                RequestStatus.ACCEPTED
        );

        return mentorshipRequestRepository.save(
                request
        );
    }

    // =========================================================
    // REJECT REQUEST
    // =========================================================

    public MentorshipRequest rejectRequest(Long id) {

        MentorshipRequest request =
                mentorshipRequestRepository
                        .findById(id)
                        .orElse(null);

        if (request == null) {
            return null;
        }

        request.setStatus(
                RequestStatus.REJECTED
        );

        return mentorshipRequestRepository.save(
                request
        );
    }

    // =========================================================
    // DELETE REQUEST
    // =========================================================

    public void deleteRequest(Long id) {

        mentorshipRequestRepository.deleteById(id);
    }
}