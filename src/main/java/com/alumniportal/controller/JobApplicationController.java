package com.alumniportal.controller;

import com.alumniportal.entity.Job;
import com.alumniportal.entity.JobApplication;
import com.alumniportal.entity.User;
import com.alumniportal.repository.JobRepository;
import com.alumniportal.repository.UserRepository;
import com.alumniportal.service.JobApplicationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "*")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public JobApplicationController(
            JobApplicationService jobApplicationService,
            JobRepository jobRepository,
            UserRepository userRepository) {

        this.jobApplicationService = jobApplicationService;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    // =========================================================
    // CREATE / APPLY FOR JOB - WITHOUT RESUME
    // =========================================================

    @PostMapping
    public ResponseEntity<?> createApplication(
            @RequestBody Map<String, Object> data) {

        try {

            Map<String, Object> jobData =
                    (Map<String, Object>) data.get("job");

            Map<String, Object> studentData =
                    (Map<String, Object>) data.get("student");

            if (jobData == null || studentData == null) {

                return ResponseEntity
                        .badRequest()
                        .body("Job and student are required.");
            }

            Number jobIdNumber =
                    (Number) jobData.get("id");

            Number studentIdNumber =
                    (Number) studentData.get("id");

            if (jobIdNumber == null || studentIdNumber == null) {

                return ResponseEntity
                        .badRequest()
                        .body("Job ID and student ID are required.");
            }

            Long jobId =
                    jobIdNumber.longValue();

            Long studentId =
                    studentIdNumber.longValue();

            Job job =
                    jobRepository
                            .findById(jobId)
                            .orElse(null);

            User student =
                    userRepository
                            .findById(studentId)
                            .orElse(null);

            if (job == null) {

                return ResponseEntity
                        .badRequest()
                        .body("Job not found.");
            }

            if (student == null) {

                return ResponseEntity
                        .badRequest()
                        .body("Student not found.");
            }

            JobApplication application =
                    new JobApplication();

            application.setJob(job);
            application.setStudent(student);

            JobApplication savedApplication =
                    jobApplicationService
                            .createApplication(application);

            return ResponseEntity.ok(savedApplication);

        } catch (IllegalStateException e) {

            return ResponseEntity
                    .status(409)
                    .body(e.getMessage());

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .badRequest()
                    .body("Unable to submit job application.");
        }
    }

    // =========================================================
    // CREATE / APPLY FOR JOB WITH RESUME
    // =========================================================

    @PostMapping(
            value = "/with-resume",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> createApplicationWithResume(
            @RequestPart("application") String applicationJson,
            @RequestPart("resume") MultipartFile resume) {

        try {

            if (resume == null || resume.isEmpty()) {

                return ResponseEntity
                        .badRequest()
                        .body("Please upload your resume.");
            }

            // Maximum resume size = 5 MB
            long maxFileSize = 5 * 1024 * 1024;

            if (resume.getSize() > maxFileSize) {

                return ResponseEntity
                        .badRequest()
                        .body("Resume size must not exceed 5 MB.");
            }

            // PDF only
            String contentType = resume.getContentType();

            if (!"application/pdf".equalsIgnoreCase(contentType)) {

                return ResponseEntity
                        .badRequest()
                        .body("Only PDF resumes are allowed.");
            }

            /*
             * Expected JSON:
             *
             * {
             *   "job": {
             *       "id": 1
             *   },
             *   "student": {
             *       "id": 2
             *   }
             * }
             */

            com.fasterxml.jackson.databind.ObjectMapper objectMapper =
                    new com.fasterxml.jackson.databind.ObjectMapper();

            Map<String, Object> data =
                    objectMapper.readValue(
                            applicationJson,
                            Map.class
                    );

            Map<String, Object> jobData =
                    (Map<String, Object>) data.get("job");

            Map<String, Object> studentData =
                    (Map<String, Object>) data.get("student");

            if (jobData == null || studentData == null) {

                return ResponseEntity
                        .badRequest()
                        .body("Job and student are required.");
            }

            Number jobIdNumber =
                    (Number) jobData.get("id");

            Number studentIdNumber =
                    (Number) studentData.get("id");

            if (jobIdNumber == null || studentIdNumber == null) {

                return ResponseEntity
                        .badRequest()
                        .body("Job ID and student ID are required.");
            }

            Long jobId =
                    jobIdNumber.longValue();

            Long studentId =
                    studentIdNumber.longValue();

            Job job =
                    jobRepository
                            .findById(jobId)
                            .orElse(null);

            User student =
                    userRepository
                            .findById(studentId)
                            .orElse(null);

            if (job == null) {

                return ResponseEntity
                        .badRequest()
                        .body("Job not found.");
            }

            if (student == null) {

                return ResponseEntity
                        .badRequest()
                        .body("Student not found.");
            }

            JobApplication application =
                    new JobApplication();

            application.setJob(job);
            application.setStudent(student);

            application.setResume(
                    resume.getBytes()
            );

            application.setResumeFileName(
                    resume.getOriginalFilename()
            );

            application.setResumeContentType(
                    resume.getContentType()
            );

            JobApplication savedApplication =
                    jobApplicationService
                            .createApplication(application);

            return ResponseEntity.ok(savedApplication);

        } catch (IllegalStateException e) {

            return ResponseEntity
                    .status(409)
                    .body(e.getMessage());

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .badRequest()
                    .body("Unable to submit job application with resume.");
        }
    }

    // =========================================================
    // DOWNLOAD / VIEW RESUME
    // =========================================================

    @GetMapping("/{id}/resume")
    public ResponseEntity<byte[]> downloadResume(
            @PathVariable Long id) {

        JobApplication application =
                jobApplicationService
                        .getApplicationById(id);

        if (application == null ||
                application.getResume() == null) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        String fileName =
                application.getResumeFileName();

        if (fileName == null ||
                fileName.trim().isEmpty()) {

            fileName = "resume.pdf";
        }

        String contentType =
                application.getResumeContentType();

        if (contentType == null ||
                contentType.trim().isEmpty()) {

            contentType = "application/pdf";
        }

        return ResponseEntity
                .ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + fileName + "\""
                )
                .contentType(
                        MediaType.parseMediaType(contentType)
                )
                .body(
                        application.getResume()
                );
    }

    // =========================================================
    // GET ALL APPLICATIONS
    // =========================================================

    @GetMapping
    public ResponseEntity<List<JobApplication>>
    getAllApplications() {

        return ResponseEntity.ok(
                jobApplicationService
                        .getAllApplications()
        );
    }

    // =========================================================
    // GET APPLICATION BY ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<JobApplication>
    getApplicationById(
            @PathVariable Long id) {

        JobApplication application =
                jobApplicationService
                        .getApplicationById(id);

        if (application == null) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(application);
    }

    // =========================================================
    // GET APPLICATIONS BY STUDENT
    // =========================================================

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<JobApplication>>
    getApplicationsByStudent(
            @PathVariable Long studentId) {

        return ResponseEntity.ok(
                jobApplicationService
                        .getApplicationsByStudent(
                                studentId
                        )
        );
    }

    // =========================================================
    // GET APPLICATIONS BY JOB
    // =========================================================

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<JobApplication>>
    getApplicationsByJob(
            @PathVariable Long jobId) {

        return ResponseEntity.ok(
                jobApplicationService
                        .getApplicationsByJob(
                                jobId
                        )
        );
    }

    // =========================================================
    // ACCEPT APPLICATION
    // =========================================================

    @PutMapping("/{id}/accept")
    public ResponseEntity<JobApplication>
    acceptApplication(
            @PathVariable Long id) {

        JobApplication application =
                jobApplicationService
                        .acceptApplication(id);

        if (application == null) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(application);
    }

    // =========================================================
    // REJECT APPLICATION
    // =========================================================

    @PutMapping("/{id}/reject")
    public ResponseEntity<JobApplication>
    rejectApplication(
            @PathVariable Long id) {

        JobApplication application =
                jobApplicationService
                        .rejectApplication(id);

        if (application == null) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(application);
    }

    // =========================================================
    // DELETE APPLICATION
    // =========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    deleteApplication(
            @PathVariable Long id) {

        JobApplication application =
                jobApplicationService
                        .getApplicationById(id);

        if (application == null) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        jobApplicationService
                .deleteApplication(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}