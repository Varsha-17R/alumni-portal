package com.alumniportal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String education;

    private String graduationYear;

    private String company;

    private String jobTitle;

    private String skills;

    private String location;

    // =========================================================
    // LINKEDIN PROFILE
    // =========================================================

    @Column(name = "linkedin_url")
    private String linkedinUrl;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // =========================================================
    // PROFILE PHOTO
    // =========================================================

    @Lob
    @JsonIgnore
    @Column(name = "photo", columnDefinition = "LONGBLOB")
    private byte[] photo;

    @JsonIgnore
    @Column(name = "photo_content_type")
    private String photoContentType;

    // =========================================================
    // CONSTRUCTORS
    // =========================================================

    public Profile() {
    }

    public Profile(String education, String graduationYear,
                   String company, String jobTitle,
                   String skills, String location,
                   String linkedinUrl, User user) {

        this.education = education;
        this.graduationYear = graduationYear;
        this.company = company;
        this.jobTitle = jobTitle;
        this.skills = skills;
        this.location = location;
        this.linkedinUrl = linkedinUrl;
        this.user = user;
    }

    // =========================================================
    // GETTERS AND SETTERS
    // =========================================================

    public Long getId() {
        return id;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(String graduationYear) {
        this.graduationYear = graduationYear;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    // =========================================================
    // LINKEDIN GETTER AND SETTER
    // =========================================================

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    // =========================================================
    // USER GETTER AND SETTER
    // =========================================================

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // =========================================================
    // PHOTO GETTERS AND SETTERS
    // =========================================================

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return photoContentType;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }
}