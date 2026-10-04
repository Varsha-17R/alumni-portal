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

        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElse(null);

        if (user == null) {
            response.sendRedirect("/login.html?error=true");
            return;
        }

        String loginType =
                request.getParameter("loginType");

        if (loginType == null ||
                loginType.trim().isEmpty()) {

            loginType = "user";
        }

        loginType = loginType.toLowerCase();

        String userRole =
                user.getRole().name().toUpperCase();


        // =====================================================
        // ADMIN LOGIN
        // =====================================================

        if ("admin".equals(loginType)) {

            if (!"ADMIN".equals(userRole)) {

                response.sendRedirect(
                        "/login.html?roleMismatch=true"
                );

                return;
            }

            // Update last login only after valid login type
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);

            response.sendRedirect("/admin.html");

            return;
        }


        // =====================================================
        // USER LOGIN
        // =====================================================

        if ("user".equals(loginType)) {

            if ("ADMIN".equals(userRole)) {

                response.sendRedirect(
                        "/login.html?roleMismatch=true"
                );

                return;
            }

            // Only STUDENT and ALUMNI can use User Login
            if (!"STUDENT".equals(userRole) &&
                    !"ALUMNI".equals(userRole)) {

                response.sendRedirect(
                        "/login.html?roleMismatch=true"
                );

                return;
            }

            // Update last login
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);

            response.sendRedirect("/dashboard.html");

            return;
        }


        // =====================================================
        // INVALID LOGIN TYPE
        // =====================================================

        response.sendRedirect(
                "/login.html?roleMismatch=true"
        );
    }
}