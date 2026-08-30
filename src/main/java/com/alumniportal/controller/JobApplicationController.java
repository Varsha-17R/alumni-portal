package com.alumniportal.controller;

import com.alumniportal.entity.JobApplication;
import com.alumniportal.service.JobApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "*")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    public JobApplicationController(
            JobApplicationService jobApplicationService) {

        this.jobApplicationService =
                jobApplicationService;
    }

    // =========================================================
    // CREATE / APPLY FOR JOB
    // =========================================================

    @PostMapping
    public ResponseEntity<?> createApplication(
            @RequestBody JobApplication application) {

        try {

            JobApplication savedApplication =
                    jobApplicationService
                            .createApplication(application);

            return ResponseEntity.ok(savedApplication);

        } catch (IllegalStateException e) {

            // Student has already applied for this job
            return ResponseEntity
                    .status(409)
                    .body(e.getMessage());
        }
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