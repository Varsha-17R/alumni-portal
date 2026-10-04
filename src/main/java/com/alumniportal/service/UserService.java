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

import java.util.List;
import java.util.Optional;

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

        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        User savedUser =
                userRepository.save(user);

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

        // -----------------------------------------------------
        // Check whether user exists
        // -----------------------------------------------------

        User user =
                userRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "User not found."
                                )
                        );


        // -----------------------------------------------------
        // 1. Delete job applications submitted by this user
        // -----------------------------------------------------

        jobApplicationRepository
                .findByStudentId(id)
                .forEach(jobApplicationRepository::delete);


        // -----------------------------------------------------
        // 2. Find jobs posted by this user
        // -----------------------------------------------------

        var postedJobs =
                jobRepository.findByPostedById(id);


        // -----------------------------------------------------
        // 3. Delete applications belonging to those jobs
        // -----------------------------------------------------

        postedJobs.forEach(job -> {

            jobApplicationRepository
                    .findByJobId(job.getId())
                    .forEach(jobApplicationRepository::delete);

        });


        // -----------------------------------------------------
        // 4. Delete jobs posted by this user
        // -----------------------------------------------------

        postedJobs.forEach(jobRepository::delete);


        // -----------------------------------------------------
        // 5. Delete messages sent by this user
        // -----------------------------------------------------

        messageRepository
                .findBySenderId(id)
                .forEach(messageRepository::delete);


        // -----------------------------------------------------
        // 6. Delete messages received by this user
        // -----------------------------------------------------

        messageRepository
                .findByReceiverId(id)
                .forEach(messageRepository::delete);


        // -----------------------------------------------------
        // 7. Delete mentorship requests as student
        // -----------------------------------------------------

        mentorshipRequestRepository
                .findByStudentId(id)
                .forEach(
                        mentorshipRequestRepository::delete
                );


        // -----------------------------------------------------
        // 8. Delete mentorship requests as alumni
        // -----------------------------------------------------

        mentorshipRequestRepository
                .findByAlumniId(id)
                .forEach(
                        mentorshipRequestRepository::delete
                );


        // -----------------------------------------------------
        // 9. Delete profile
        // -----------------------------------------------------

        profileRepository
                .findByUserId(id)
                .ifPresent(profileRepository::delete);


        // -----------------------------------------------------
        // 10. Finally delete the user
        // -----------------------------------------------------

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