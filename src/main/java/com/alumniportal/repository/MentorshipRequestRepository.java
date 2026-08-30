package com.alumniportal.repository;

import com.alumniportal.entity.MentorshipRequest;
import com.alumniportal.entity.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MentorshipRequestRepository
        extends JpaRepository<MentorshipRequest, Long> {

    List<MentorshipRequest> findByStudentId(Long studentId);

    List<MentorshipRequest> findByAlumniId(Long alumniId);

    boolean existsByStudentIdAndAlumniId(
            Long studentId,
            Long alumniId
    );

    boolean existsByStudentIdAndAlumniIdAndStatus(
            Long studentId,
            Long alumniId,
            RequestStatus status
    );
}