package com.alumniportal.service;

import com.alumniportal.entity.Job;
import com.alumniportal.entity.JobApplication;
import com.alumniportal.entity.User;
import com.alumniportal.entity.Role;
import com.alumniportal.repository.JobRepository;
import com.alumniportal.repository.JobApplicationRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final EntityManager entityManager;

    public JobService(
            JobRepository jobRepository,
            JobApplicationRepository jobApplicationRepository,
            EntityManager entityManager) {

        this.jobRepository = jobRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.entityManager = entityManager;
    }

    // =========================================================
    // CREATE JOB
    // =========================================================

    public Job createJob(Job job, Long userId) {

        User user = entityManager.find(User.class, userId);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Only ALUMNI can post jobs
        if (user.getRole() != Role.ALUMNI) {
            throw new RuntimeException(
                    "Only alumni users are allowed to post jobs"
            );
        }

        // Always use the logged-in user's ID
        // instead of trusting postedBy from frontend
        job.setPostedBy(user);

        return jobRepository.save(job);
    }

    // =========================================================
    // GET ALL JOBS
    // =========================================================

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    // =========================================================
    // GET JOB BY ID
    // =========================================================

    public Job getJobById(Long id) {
        return jobRepository.findById(id).orElse(null);
    }

    // =========================================================
    // GET JOBS BY USER
    // =========================================================

    public List<Job> getJobsByUser(Long userId) {
        return jobRepository.findByPostedById(userId);
    }

    // =========================================================
    // SEARCH BY TITLE
    // =========================================================

    public List<Job> searchByTitle(String title) {
        return jobRepository
                .findByTitleContainingIgnoreCase(title);
    }

    // =========================================================
    // SEARCH BY LOCATION
    // =========================================================

    public List<Job> searchByLocation(String location) {
        return jobRepository
                .findByLocationContainingIgnoreCase(location);
    }

    // =========================================================
    // SEARCH BY COMPANY
    // =========================================================

    public List<Job> searchByCompany(String company) {
        return jobRepository
                .findByCompanyContainingIgnoreCase(company);
    }

    // =========================================================
    // APPROVE JOB
    // =========================================================

    public Job approveJob(Long id) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Job not found"));

        job.setStatus("APPROVED");

        return jobRepository.save(job);
    }

    // =========================================================
    // REJECT JOB
    // =========================================================

    public Job rejectJob(Long id) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Job not found"));

        job.setStatus("REJECTED");

        return jobRepository.save(job);
    }

    // =========================================================
    // DELETE JOB
    // =========================================================

    @Transactional
    public void deleteJob(Long id) {

        // First delete all applications associated with this job
        List<JobApplication> applications =
                jobApplicationRepository.findByJobId(id);

        if (!applications.isEmpty()) {
            jobApplicationRepository.deleteAll(applications);
        }

        // Then delete the job
        jobRepository.deleteById(id);
    }
}