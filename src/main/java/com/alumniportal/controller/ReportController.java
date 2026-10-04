package com.alumniportal.controller;

import com.alumniportal.entity.RequestStatus;
import com.alumniportal.entity.Role;
import com.alumniportal.repository.EventRepository;
import com.alumniportal.repository.JobRepository;
import com.alumniportal.repository.MessageRepository;
import com.alumniportal.repository.MentorshipRequestRepository;
import com.alumniportal.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
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

        // =====================================================
        // LOAD DATA ONCE
        // =====================================================

        List<com.alumniportal.entity.User> users =
                userRepository.findAll();

        List<com.alumniportal.entity.Job> jobs =
                jobRepository.findAll();

        List<com.alumniportal.entity.Event> events =
                eventRepository.findAll();

        List<com.alumniportal.entity.MentorshipRequest> mentorshipRequests =
                mentorshipRequestRepository.findAll();


        // =====================================================
        // USERS
        // =====================================================

        long totalUsers = users.size();

        long totalStudents =
                users.stream()
                        .filter(user ->
                                user.getRole() == Role.STUDENT)
                        .count();

        long totalAlumni =
                users.stream()
                        .filter(user ->
                                user.getRole() == Role.ALUMNI)
                        .count();

        long verifiedAlumni =
                users.stream()
                        .filter(user ->
                                user.getRole() == Role.ALUMNI &&
                                        user.isVerified())
                        .count();

        long pendingAlumni =
                users.stream()
                        .filter(user ->
                                user.getRole() == Role.ALUMNI &&
                                        !user.isVerified())
                        .count();


        // =====================================================
        // USER ACTIVITY
        // =====================================================

        LocalDateTime thirtyDaysAgo =
                LocalDateTime.now().minusDays(30);

        long activeUsers =
                users.stream()
                        .filter(user ->
                                user.getLastLogin() != null &&
                                        !user.getLastLogin()
                                                .isBefore(thirtyDaysAgo))
                        .count();

        long inactiveUsers =
                users.stream()
                        .filter(user ->
                                user.getLastLogin() != null &&
                                        user.getLastLogin()
                                                .isBefore(thirtyDaysAgo))
                        .count();

        long neverLoggedInUsers =
                users.stream()
                        .filter(user ->
                                user.getLastLogin() == null)
                        .count();

        double activeUserPercentage =
                totalUsers > 0
                        ? (activeUsers * 100.0) / totalUsers
                        : 0.0;


        // =====================================================
        // JOBS
        // =====================================================

        long totalJobs = jobs.size();

        long approvedJobs =
                jobs.stream()
                        .filter(job ->
                                "APPROVED".equalsIgnoreCase(
                                        job.getStatus()))
                        .count();

        long pendingJobs =
                jobs.stream()
                        .filter(job ->
                                "PENDING".equalsIgnoreCase(
                                        job.getStatus()))
                        .count();

        long rejectedJobs =
                jobs.stream()
                        .filter(job ->
                                "REJECTED".equalsIgnoreCase(
                                        job.getStatus()))
                        .count();


        // =====================================================
        // EVENTS
        // =====================================================

        long totalEvents = events.size();

        long approvedEvents =
                events.stream()
                        .filter(event ->
                                "APPROVED".equalsIgnoreCase(
                                        event.getStatus()))
                        .count();

        long pendingEvents =
                events.stream()
                        .filter(event ->
                                "PENDING".equalsIgnoreCase(
                                        event.getStatus()))
                        .count();

        long rejectedEvents =
                events.stream()
                        .filter(event ->
                                "REJECTED".equalsIgnoreCase(
                                        event.getStatus()))
                        .count();


        // =====================================================
        // MENTORSHIP
        // =====================================================

        long totalMentorshipRequests =
                mentorshipRequests.size();

        long pendingMentorshipRequests =
                mentorshipRequests.stream()
                        .filter(request ->
                                request.getStatus()
                                        == RequestStatus.PENDING)
                        .count();

        long acceptedMentorshipRequests =
                mentorshipRequests.stream()
                        .filter(request ->
                                request.getStatus()
                                        == RequestStatus.ACCEPTED)
                        .count();

        long rejectedMentorshipRequests =
                mentorshipRequests.stream()
                        .filter(request ->
                                request.getStatus()
                                        == RequestStatus.REJECTED)
                        .count();


        // =====================================================
        // MESSAGES
        // =====================================================

        long totalMessages =
                messageRepository.count();


        // =====================================================
        // ADD VALUES TO REPORT
        // =====================================================

        // ---------- Users ----------

        report.put("totalUsers", totalUsers);

        report.put("totalStudents", totalStudents);

        report.put("totalAlumni", totalAlumni);

        report.put("verifiedAlumni", verifiedAlumni);

        report.put("pendingAlumni", pendingAlumni);


        // ---------- User Activity ----------

        report.put("activeUsers", activeUsers);

        report.put("inactiveUsers", inactiveUsers);

        report.put("neverLoggedInUsers",
                neverLoggedInUsers);

        report.put(
                "activeUserPercentage",
                Math.round(
                        activeUserPercentage * 100.0
                ) / 100.0
        );


        // ---------- Jobs ----------

        report.put("totalJobs", totalJobs);

        report.put("approvedJobs", approvedJobs);

        report.put("pendingJobs", pendingJobs);

        report.put("rejectedJobs", rejectedJobs);


        // ---------- Events ----------

        report.put("totalEvents", totalEvents);

        report.put("approvedEvents", approvedEvents);

        report.put("pendingEvents", pendingEvents);

        report.put("rejectedEvents", rejectedEvents);


        // ---------- Mentorship ----------

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


        // ---------- Messages ----------

        report.put(
                "totalMessages",
                totalMessages
        );


        return report;
    }
}