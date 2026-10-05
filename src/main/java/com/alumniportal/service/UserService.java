package com.alumniportal.service;

import com.alumniportal.entity.Role;
import com.alumniportal.entity.User;
import com.alumniportal.repository.UserRepository;
import com.alumniportal.repository.ProfileRepository;
import com.alumniportal.repository.JobRepository;
import com.alumniportal.repository.JobApplicationRepository;
import com.alumniportal.repository.MessageRepository;
import com.alumniportal.repository.MentorshipRequestRepository;
import com.alumniportal.repository.EventRegistrationRepository;

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
    private final EventRegistrationRepository eventRegistrationRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserService(
            UserRepository userRepository,
            ProfileRepository profileRepository,
            JobRepository jobRepository,
            JobApplicationRepository jobApplicationRepository,
            MessageRepository messageRepository,
            MentorshipRequestRepository mentorshipRequestRepository,
            EventRegistrationRepository eventRegistrationRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService) {

        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.jobRepository = jobRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.messageRepository = messageRepository;
        this.mentorshipRequestRepository =
                mentorshipRequestRepository;
        this.eventRegistrationRepository =
                eventRegistrationRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public User saveUser(User user) {

        String name = user.getName();
        String email = user.getEmail();
        String role = user.getRole().name();

        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        user.setEmailVerified(false);
        user.setPhoneVerified(false);

        String emailOtp =
                String.format(
                        "%06d",
                        new Random().nextInt(1000000)
                );

        user.setEmailVerificationOtp(emailOtp);

        user.setEmailOtpExpiry(
                LocalDateTime.now().plusMinutes(5)
        );

        User savedUser =
                userRepository.save(user);

        emailService.sendEmailVerificationOtp(
                email,
                name,
                emailOtp
        );

        emailService.sendWelcomeEmail(
                email,
                name,
                role
        );

        return savedUser;
    }

    public User saveUpdatedUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {

        Optional<User> user =
                userRepository.findById(id);

        return user.orElse(null);
    }

    public User getUserByEmail(String email) {

        Optional<User> user =
                userRepository.findByEmail(email);

        return user.orElse(null);
    }

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
         * =====================================================
         * DELETE EVENT REGISTRATIONS
         * =====================================================
         *
         * A user's event registrations contain:
         *
         * event_registrations.student_id -> users.id
         *
         * Therefore registrations must be deleted before
         * deleting the user.
         */
        eventRegistrationRepository
                .findByStudentId(id)
                .forEach(eventRegistrationRepository::delete);


        /*
         * =====================================================
         * DELETE JOB APPLICATIONS
         * =====================================================
         *
         * Applications submitted by the user.
         */
        jobApplicationRepository
                .findByStudentId(id)
                .forEach(jobApplicationRepository::delete);


        /*
         * =====================================================
         * DELETE JOBS POSTED BY USER
         * =====================================================
         *
         * First delete applications belonging to those jobs.
         * Then delete the jobs.
         */
        var postedJobs =
                jobRepository.findByPostedById(id);

        postedJobs.forEach(job -> {

            jobApplicationRepository
                    .findByJobId(job.getId())
                    .forEach(jobApplicationRepository::delete);

        });

        postedJobs.forEach(
                jobRepository::delete
        );


        /*
         * =====================================================
         * DELETE SENT MESSAGES
         * =====================================================
         */
        messageRepository
                .findBySenderId(id)
                .forEach(messageRepository::delete);


        /*
         * =====================================================
         * DELETE RECEIVED MESSAGES
         * =====================================================
         */
        messageRepository
                .findByReceiverId(id)
                .forEach(messageRepository::delete);


        /*
         * =====================================================
         * DELETE MENTORSHIP REQUESTS AS STUDENT
         * =====================================================
         */
        mentorshipRequestRepository
                .findByStudentId(id)
                .forEach(mentorshipRequestRepository::delete);


        /*
         * =====================================================
         * DELETE MENTORSHIP REQUESTS AS ALUMNI
         * =====================================================
         */
        mentorshipRequestRepository
                .findByAlumniId(id)
                .forEach(mentorshipRequestRepository::delete);


        /*
         * =====================================================
         * DELETE PROFILE
         * =====================================================
         */
        profileRepository
                .findByUserId(id)
                .ifPresent(profileRepository::delete);


        /*
         * =====================================================
         * FINALLY DELETE USER
         * =====================================================
         */
        userRepository.delete(user);
    }

    public List<User> getAlumni() {

        return userRepository.findByRole(
                Role.ALUMNI
        );
    }

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