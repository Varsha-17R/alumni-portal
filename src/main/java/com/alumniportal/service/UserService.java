package com.alumniportal.service;

import com.alumniportal.entity.Role;
import com.alumniportal.entity.User;
import com.alumniportal.repository.UserRepository;
import com.alumniportal.repository.ProfileRepository;

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
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserService(
            UserRepository userRepository,
            ProfileRepository profileRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService) {

        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    // =========================================================
    // CREATE USER
    // =========================================================

    public User saveUser(User user) {

        // Save original details for email
        String name = user.getName();
        String email = user.getEmail();
        String role = user.getRole().name();

        // Encrypt password before saving
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        // Save user to database
        User savedUser =
                userRepository.save(user);

        // Send welcome email
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
        profileRepository.findByUserId(id)
                .ifPresent(profileRepository::delete);

        userRepository.deleteById(id);
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