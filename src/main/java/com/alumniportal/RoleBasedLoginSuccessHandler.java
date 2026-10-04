package com.alumniportal.config;

import com.alumniportal.entity.User;
import com.alumniportal.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class RoleBasedLoginSuccessHandler
        implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    public RoleBasedLoginSuccessHandler(
            UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        /*
         * Update the user's last successful login time.
         */
        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElse(null);

        if (user != null) {
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
        }

        /*
         * Redirect the user based on their role.
         */
        if (authentication.getAuthorities().stream()
                .anyMatch(auth ->
                        auth.getAuthority().equals("ROLE_ADMIN"))) {

            response.sendRedirect("/admin.html");

        } else {

            response.sendRedirect("/dashboard.html");
        }
    }
}