package com.alumniportal.controller;

import com.alumniportal.entity.Job;
import com.alumniportal.service.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    // =========================================================
    // CREATE JOB
    // =========================================================

    @PostMapping
    public ResponseEntity<?> createJob(
            @RequestBody Job job,
            @RequestParam Long userId) {

        try {

            Job savedJob =
                    jobService.createJob(job, userId);

            return ResponseEntity.ok(savedJob);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    // =========================================================
    // GET ALL JOBS
    // =========================================================

    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs() {

        return ResponseEntity.ok(
                jobService.getAllJobs()
        );
    }

    // =========================================================
    // GET JOB BY ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(
            @PathVariable Long id) {

        Job job =
                jobService.getJobById(id);

        if (job == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(job);
    }

    // =========================================================
    // GET JOBS BY USER
    // =========================================================

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Job>> getJobsByUser(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                jobService.getJobsByUser(userId)
        );
    }

    // =========================================================
    // SEARCH BY TITLE
    // =========================================================

    @GetMapping("/search/title")
    public ResponseEntity<List<Job>> searchByTitle(
            @RequestParam String title) {

        return ResponseEntity.ok(
                jobService.searchByTitle(title)
        );
    }

    // =========================================================
    // SEARCH BY LOCATION
    // =========================================================

    @GetMapping("/search/location")
    public ResponseEntity<List<Job>> searchByLocation(
            @RequestParam String location) {

        return ResponseEntity.ok(
                jobService.searchByLocation(location)
        );
    }

    // =========================================================
    // SEARCH BY COMPANY
    // =========================================================

    @GetMapping("/search/company")
    public ResponseEntity<List<Job>> searchByCompany(
            @RequestParam String company) {

        return ResponseEntity.ok(
                jobService.searchByCompany(company)
        );
    }

    // =========================================================
    // DELETE JOB
    // =========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(
            @PathVariable Long id) {

        Job job =
                jobService.getJobById(id);

        if (job == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        jobService.deleteJob(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}