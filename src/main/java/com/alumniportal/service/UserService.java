package com.alumniportal.service;

import com.alumniportal.entity.Role;
import com.alumniportal.entity.User;
import com.alumniportal.repository.UserRepository;
import com.alumniportal.repository.ProfileRepository;
import com.alumniportal.repository.JobRepository;
import com.alumniportal.repository.JobApplicationRepository;
import com.alumniportal.repository.MessageRepository;
import com.alumniportal.repository.MentorshipRequestRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final JobRepository jobRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final MessageRepository messageRepository;
    private final MentorshipRequestRepository mentorshipRequestRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserService(
            UserRepository userRepository,
            ProfileRepository profileRepository,
            JobRepository jobRepository,
            JobApplicationRepository jobApplicationRepository,
            MessageRepository messageRepository,
            MentorshipRequestRepository mentorshipRequestRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService) {

        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.jobRepository = jobRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.messageRepository = messageRepository;
        this.mentorshipRequestRepository =
                mentorshipRequestRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }


    // =========================================================
    // CREATE USER
    // =========================================================

    public User saveUser(User user) {

        String name = user.getName();
        String email = user.getEmail();
        String role = user.getRole().name();

        /*
         * Encode the user's password before saving.
         */
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        /*
         * Email verification starts as false.
         */
        user.setEmailVerified(false);

        /*
         * Phone verification starts as false.
         */
        user.setPhoneVerified(false);

        /*
         * Generate a 6-digit email verification OTP.
         */
        String emailOtp =
                String.format(
                        "%06d",
                        new Random().nextInt(1000000)
                );

        /*
         * Store the OTP.
         */
        user.setEmailVerificationOtp(emailOtp);

        /*
         * OTP will expire after 5 minutes.
         */
        user.setEmailOtpExpiry(
                LocalDateTime.now().plusMinutes(5)
        );

        /*
         * Save the user.
         */
        User savedUser =
                userRepository.save(user);

        /*
         * Send the email verification OTP.
         */
        emailService.sendEmailVerificationOtp(
                email,
                name,
                emailOtp
        );

        /*
         * Keep the existing welcome email.
         */
        emailService.sendWelcomeEmail(
                email,
                name,
                role
        );

        return savedUser;
    }


    // =========================================================
    // UPDATE USER
    // =========================================================

    public User saveUpdatedUser(User user) {
        return userRepository.save(user);
    }


    // =========================================================
    // GET ALL USERS
    // =========================================================

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    // =========================================================
    // GET USER BY ID
    // =========================================================

    public User getUserById(Long id) {

        Optional<User> user =
                userRepository.findById(id);

        return user.orElse(null);
    }


    // =========================================================
    // GET USER BY EMAIL
    // =========================================================

    public User getUserByEmail(String email) {

        Optional<User> user =
                userRepository.findByEmail(email);

        return user.orElse(null);
    }


    // =========================================================
    // DELETE USER
    // =========================================================

    @Transactional
    public void deleteUser(Long id) {

        User user =
                userRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "User not found."
                                )
                        );

        /*
         * Delete applications submitted by the user.
         */
        jobApplicationRepository
                .findByStudentId(id)
                .forEach(jobApplicationRepository::delete);

        /*
         * Find jobs posted by the user.
         */
        var postedJobs =
                jobRepository.findByPostedById(id);

        /*
         * Delete applications belonging
         * to the user's posted jobs.
         */
        postedJobs.forEach(job -> {

            jobApplicationRepository
                    .findByJobId(job.getId())
                    .forEach(jobApplicationRepository::delete);

        });

        /*
         * Delete posted jobs.
         */
        postedJobs.forEach(jobRepository::delete);

        /*
         * Delete messages sent by the user.
         */
        messageRepository
                .findBySenderId(id)
                .forEach(messageRepository::delete);

        /*
         * Delete messages received by the user.
         */
        messageRepository
                .findByReceiverId(id)
                .forEach(messageRepository::delete);

        /*
         * Delete mentorship requests where
         * the user is the student.
         */
        mentorshipRequestRepository
                .findByStudentId(id)
                .forEach(mentorshipRequestRepository::delete);

        /*
         * Delete mentorship requests where
         * the user is the alumni.
         */
        mentorshipRequestRepository
                .findByAlumniId(id)
                .forEach(mentorshipRequestRepository::delete);

        /*
         * Delete the user's profile.
         */
        profileRepository
                .findByUserId(id)
                .ifPresent(profileRepository::delete);

        /*
         * Finally delete the user.
         */
        userRepository.delete(user);
    }


    // =========================================================
    // GET ALL ALUMNI
    // =========================================================

    public List<User> getAlumni() {
        return userRepository.findByRole(Role.ALUMNI);
    }


    // =========================================================
    // SPRING SECURITY LOGIN
    // =========================================================

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new UsernameNotFoundException(
                                        "User not found with email: "
                                                + email
                                )
                        );

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}