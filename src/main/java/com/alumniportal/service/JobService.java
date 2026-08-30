package com.alumniportal.service;

import com.alumniportal.entity.Job;
import com.alumniportal.entity.User;
import com.alumniportal.entity.Role;
import com.alumniportal.repository.JobRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final EntityManager entityManager;

    public JobService(
            JobRepository jobRepository,
            EntityManager entityManager) {

        this.jobRepository = jobRepository;
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
    // DELETE JOB
    // =========================================================

    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }
}