package com.alumniportal.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;

    @Column
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;


    /*
     * =========================================================
     * ADMIN VERIFICATION
     * =========================================================
     *
     * This field is used by the Admin to verify alumni.
     * It is kept separate from email and phone verification.
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(nullable = false)
    private boolean verified = false;


    /*
     * =========================================================
     * EMAIL VERIFICATION
     * =========================================================
     *
     * emailVerified:
     * Indicates whether the user's email address has been
     * successfully verified using OTP.
     */
    @Column(nullable = false)
    private boolean emailVerified = false;

    /*
     * Stores the email verification OTP.
     */
    @Column
    private String emailVerificationOtp;

    /*
     * Stores the expiry date and time of the email OTP.
     */
    @Column
    private LocalDateTime emailOtpExpiry;


    /*
     * =========================================================
     * PHONE VERIFICATION
     * =========================================================
     *
     * phoneVerified:
     * Indicates whether the user's phone number has been
     * successfully verified using SMS OTP.
     */
    @Column(nullable = false)
    private boolean phoneVerified = false;

    /*
     * Stores the phone verification OTP.
     */
    @Column
    private String phoneVerificationOtp;

    /*
     * Stores the expiry date and time of the phone OTP.
     */
    @Column
    private LocalDateTime phoneOtpExpiry;


    /*
     * =========================================================
     * LOGIN / ACTIVITY TRACKING
     * =========================================================
     *
     * Stores the date and time of the user's most recent
     * successful login.
     *
     * Used by Admin Reports & Analytics.
     */
    @Column
    private LocalDateTime lastLogin;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public User() {
    }


    // =========================================================
    // GETTERS AND SETTERS
    // =========================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }


    // =========================================================
    // ADMIN VERIFICATION
    // =========================================================

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }


    // =========================================================
    // EMAIL VERIFICATION
    // =========================================================

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }


    public String getEmailVerificationOtp() {
        return emailVerificationOtp;
    }

    public void setEmailVerificationOtp(String emailVerificationOtp) {
        this.emailVerificationOtp = emailVerificationOtp;
    }


    public LocalDateTime getEmailOtpExpiry() {
        return emailOtpExpiry;
    }

    public void setEmailOtpExpiry(LocalDateTime emailOtpExpiry) {
        this.emailOtpExpiry = emailOtpExpiry;
    }


    // =========================================================
    // PHONE VERIFICATION
    // =========================================================

    public boolean isPhoneVerified() {
        return phoneVerified;
    }

    public void setPhoneVerified(boolean phoneVerified) {
        this.phoneVerified = phoneVerified;
    }


    public String getPhoneVerificationOtp() {
        return phoneVerificationOtp;
    }

    public void setPhoneVerificationOtp(String phoneVerificationOtp) {
        this.phoneVerificationOtp = phoneVerificationOtp;
    }


    public LocalDateTime getPhoneOtpExpiry() {
        return phoneOtpExpiry;
    }

    public void setPhoneOtpExpiry(LocalDateTime phoneOtpExpiry) {
        this.phoneOtpExpiry = phoneOtpExpiry;
    }


    // =========================================================
    // LAST LOGIN
    // =========================================================

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }
}