package com.alumniportal.repository;

import com.alumniportal.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    // Get jobs posted by a particular user
    List<Job> findByPostedById(Long userId);

    // Search jobs by title
    List<Job> findByTitleContainingIgnoreCase(String title);

    // Search jobs by location
    List<Job> findByLocationContainingIgnoreCase(String location);

    // Search jobs by company
    List<Job> findByCompanyContainingIgnoreCase(String company);
}
