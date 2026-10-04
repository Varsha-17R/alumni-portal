package com.alumniportal.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "job_applications")
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status;

    // =========================================================
    // RESUME
    // =========================================================

    @Lob
    @Column(name = "resume", columnDefinition = "LONGBLOB")
    private byte[] resume;

    @Column(name = "resume_file_name")
    private String resumeFileName;

    @Column(name = "resume_content_type")
    private String resumeContentType;

    // =========================================================
    // CONSTRUCTORS
    // =========================================================

    public JobApplication() {
    }

    public JobApplication(Job job, User student, ApplicationStatus status) {
        this.job = job;
        this.student = student;
        this.status = status;
    }

    // =========================================================
    // GETTERS AND SETTERS
    // =========================================================

    public Long getId() {
        return id;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    // =========================================================
    // RESUME GETTERS AND SETTERS
    // =========================================================

    public byte[] getResume() {
        return resume;
    }

    public void setResume(byte[] resume) {
        this.resume = resume;
    }

    public String getResumeFileName() {
        return resumeFileName;
    }

    public void setResumeFileName(String resumeFileName) {
        this.resumeFileName = resumeFileName;
    }

    public String getResumeContentType() {
        return resumeContentType;
    }

    public void setResumeContentType(String resumeContentType) {
        this.resumeContentType = resumeContentType;
    }
}