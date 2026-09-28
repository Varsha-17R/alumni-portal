package com.alumniportal.controller;

import com.alumniportal.entity.RequestStatus;
import com.alumniportal.entity.Role;
import com.alumniportal.repository.EventRepository;
import com.alumniportal.repository.JobRepository;
import com.alumniportal.repository.MessageRepository;
import com.alumniportal.repository.MentorshipRequestRepository;
import com.alumniportal.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final EventRepository eventRepository;
    private final MentorshipRequestRepository mentorshipRequestRepository;
    private final MessageRepository messageRepository;

    public ReportController(
            UserRepository userRepository,
            JobRepository jobRepository,
            EventRepository eventRepository,
            MentorshipRequestRepository mentorshipRequestRepository,
            MessageRepository messageRepository) {

        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.eventRepository = eventRepository;
        this.mentorshipRequestRepository = mentorshipRequestRepository;
        this.messageRepository = messageRepository;
    }

    @GetMapping
    public Map<String, Object> getReports() {

        Map<String, Object> report = new HashMap<>();

        // ================= USERS =================

        long totalUsers = userRepository.count();

        long totalAlumni =
                userRepository.findByRole(Role.ALUMNI).size();

        long verifiedAlumni =
                userRepository.findByRole(Role.ALUMNI)
                        .stream()
                        .filter(user -> user.isVerified())
                        .count();

        long pendingAlumni =
                totalAlumni - verifiedAlumni;


        // ================= JOBS =================

        long totalJobs =
                jobRepository.count();

        long approvedJobs =
                jobRepository.findAll()
                        .stream()
                        .filter(job ->
                                "APPROVED".equalsIgnoreCase(
                                        job.getStatus()))
                        .count();

        long pendingJobs =
                jobRepository.findAll()
                        .stream()
                        .filter(job ->
                                "PENDING".equalsIgnoreCase(
                                        job.getStatus()))
                        .count();

        long rejectedJobs =
                jobRepository.findAll()
                        .stream()
                        .filter(job ->
                                "REJECTED".equalsIgnoreCase(
                                        job.getStatus()))
                        .count();


        // ================= EVENTS =================

        long totalEvents =
                eventRepository.count();

        long approvedEvents =
                eventRepository.findAll()
                        .stream()
                        .filter(event ->
                                "APPROVED".equalsIgnoreCase(
                                        event.getStatus()))
                        .count();

        long pendingEvents =
                eventRepository.findAll()
                        .stream()
                        .filter(event ->
                                "PENDING".equalsIgnoreCase(
                                        event.getStatus()))
                        .count();

        long rejectedEvents =
                eventRepository.findAll()
                        .stream()
                        .filter(event ->
                                "REJECTED".equalsIgnoreCase(
                                        event.getStatus()))
                        .count();


        // ================= MENTORSHIP =================

        long totalMentorshipRequests =
                mentorshipRequestRepository.count();

        long pendingMentorshipRequests =
                mentorshipRequestRepository
                        .findAll()
                        .stream()
                        .filter(request ->
                                request.getStatus()
                                        == RequestStatus.PENDING)
                        .count();

        long acceptedMentorshipRequests =
                mentorshipRequestRepository
                        .findAll()
                        .stream()
                        .filter(request ->
                                request.getStatus()
                                        == RequestStatus.ACCEPTED)
                        .count();

        long rejectedMentorshipRequests =
                mentorshipRequestRepository
                        .findAll()
                        .stream()
                        .filter(request ->
                                request.getStatus()
                                        == RequestStatus.REJECTED)
                        .count();


        // ================= MESSAGES =================

        long totalMessages =
                messageRepository.count();


        // ================= ADD TO REPORT =================

        report.put("totalUsers", totalUsers);

        report.put("totalAlumni", totalAlumni);
        report.put("verifiedAlumni", verifiedAlumni);
        report.put("pendingAlumni", pendingAlumni);

        report.put("totalJobs", totalJobs);
        report.put("approvedJobs", approvedJobs);
        report.put("pendingJobs", pendingJobs);
        report.put("rejectedJobs", rejectedJobs);

        report.put("totalEvents", totalEvents);
        report.put("approvedEvents", approvedEvents);
        report.put("pendingEvents", pendingEvents);
        report.put("rejectedEvents", rejectedEvents);

        report.put(
                "totalMentorshipRequests",
                totalMentorshipRequests
        );

        report.put(
                "pendingMentorshipRequests",
                pendingMentorshipRequests
        );

        report.put(
                "acceptedMentorshipRequests",
                acceptedMentorshipRequests
        );

        report.put(
                "rejectedMentorshipRequests",
                rejectedMentorshipRequests
        );

        report.put("totalMessages", totalMessages);

        return report;
    }
}