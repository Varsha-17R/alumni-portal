package com.alumniportal.service;

import com.alumniportal.entity.ApplicationStatus;
import com.alumniportal.entity.JobApplication;
import com.alumniportal.repository.JobApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;

    public JobApplicationService(
            JobApplicationRepository jobApplicationRepository) {

        this.jobApplicationRepository = jobApplicationRepository;
    }

    // =========================================================
    // CREATE APPLICATION
    // =========================================================

    public JobApplication createApplication(
            JobApplication application) {

        /*
         * Check whether this student has already
         * applied for this job.
         */

        Long studentId =
                application.getStudent().getId();

        Long jobId =
                application.getJob().getId();

        boolean alreadyApplied =
                jobApplicationRepository
                        .existsByStudentIdAndJobId(
                                studentId,
                                jobId
                        );

        if (alreadyApplied) {

            throw new IllegalStateException(
                    "You have already applied for this job."
            );
        }

        /*
         * Every new application starts as PENDING.
         */

        application.setStatus(
                ApplicationStatus.PENDING
        );

        return jobApplicationRepository.save(
                application
        );
    }

    // =========================================================
    // GET ALL APPLICATIONS
    // =========================================================

    public List<JobApplication> getAllApplications() {

        return jobApplicationRepository.findAll();
    }

    // =========================================================
    // GET APPLICATION BY ID
    // =========================================================

    public JobApplication getApplicationById(Long id) {

        return jobApplicationRepository
                .findById(id)
                .orElse(null);
    }

    // =========================================================
    // GET APPLICATIONS BY STUDENT
    // =========================================================

    public List<JobApplication> getApplicationsByStudent(
            Long studentId) {

        return jobApplicationRepository
                .findByStudentId(studentId);
    }

    // =========================================================
    // GET APPLICATIONS BY JOB
    // =========================================================

    public List<JobApplication> getApplicationsByJob(
            Long jobId) {

        return jobApplicationRepository
                .findByJobId(jobId);
    }

    // =========================================================
    // ACCEPT APPLICATION
    // =========================================================

    public JobApplication acceptApplication(Long id) {

        JobApplication application =
                jobApplicationRepository
                        .findById(id)
                        .orElse(null);

        if (application == null) {
            return null;
        }

        application.setStatus(
                ApplicationStatus.ACCEPTED
        );

        return jobApplicationRepository.save(
                application
        );
    }

    // =========================================================
    // REJECT APPLICATION
    // =========================================================

    public JobApplication rejectApplication(Long id) {

        JobApplication application =
                jobApplicationRepository
                        .findById(id)
                        .orElse(null);

        if (application == null) {
            return null;
        }

        application.setStatus(
                ApplicationStatus.REJECTED
        );

        return jobApplicationRepository.save(
                application
        );
    }

    // =========================================================
    // DELETE APPLICATION
    // =========================================================

    public void deleteApplication(Long id) {

        jobApplicationRepository.deleteById(id);
    }
}