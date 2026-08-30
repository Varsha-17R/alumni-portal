package com.alumniportal.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "mentorship_requests")
public class MentorshipRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "alumni_id", nullable = false)
    private User alumni;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    public MentorshipRequest() {
    }

    public MentorshipRequest(User student, User alumni, RequestStatus status) {
        this.student = student;
        this.alumni = alumni;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public User getAlumni() {
        return alumni;
    }

    public void setAlumni(User alumni) {
        this.alumni = alumni;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }
}
