package com.alumniportal.repository;

import com.alumniportal.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationRepository
        extends JpaRepository<JobApplication, Long> {

    // Get applications submitted by a student
    List<JobApplication> findByStudentId(Long studentId);

    // Get applications received for a particular job
    List<JobApplication> findByJobId(Long jobId);

    // Check whether a student has already applied for a job
    boolean existsByStudentIdAndJobId(
            Long studentId,
            Long jobId
    );
}