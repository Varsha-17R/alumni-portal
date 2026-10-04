package com.alumniportal.controller;

import com.alumniportal.entity.User;
import com.alumniportal.repository.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/verification")
public class VerificationController {

    private final UserRepository userRepository;

    public VerificationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // =========================================================
    // VERIFY EMAIL OTP
    // =========================================================

    @PostMapping("/email")
    public ResponseEntity<?> verifyEmail(
            @RequestBody Map<String, String> request) {

        String email = request.get("email");
        String otp = request.get("otp");

        // Validate request
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message",
                            "Email is required."
                    ));
        }

        if (otp == null || otp.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message",
                            "OTP is required."
                    ));
        }

        // Find user
        User user = userRepository
                .findByEmail(email)
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(404)
                    .body(Map.of(
                            "message",
                            "User not found."
                    ));
        }

        // Already verified
        if (user.isEmailVerified()) {
            return ResponseEntity.ok(
                    Map.of(
                            "message",
                            "Email is already verified."
                    )
            );
        }

        // Check OTP
        if (user.getEmailVerificationOtp() == null ||
                !user.getEmailVerificationOtp().equals(otp.trim())) {

            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message",
                            "Invalid OTP."
                    ));
        }

        // Check expiry
        if (user.getEmailOtpExpiry() == null ||
                LocalDateTime.now()
                        .isAfter(user.getEmailOtpExpiry())) {

            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message",
                            "OTP has expired. Please request a new OTP."
                    ));
        }

        // Mark email as verified
        user.setEmailVerified(true);

        // Remove OTP after successful verification
        user.setEmailVerificationOtp(null);
        user.setEmailOtpExpiry(null);

        userRepository.save(user);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Email verified successfully."
                )
        );
    }
}