package com.alumniportal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            RoleBasedLoginSuccessHandler successHandler)
            throws Exception {

        http
                // Disable CSRF for REST APIs
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // =========================
                        // ADMIN PAGE
                        // =========================
                        .requestMatchers("/admin.html")
                        .hasRole("ADMIN")

                        // =========================
                        // PUBLIC STATIC PAGES
                        // =========================
                        .requestMatchers(
                                "/login.html",
                                "/register.html",
                                "/dashboard.html",
                                "/profile.html",
                                "/alumni.html",
                                "/mentorship.html",
                                "/jobs.html",
                                "/events.html",
                                "/messages.html",
                                "/applications.html",

                                // Admin pages
                                "/admin-users.html",
                                "/admin-alumni.html",
                                "/admin-jobs.html",
                                "/admin-events.html",


                                // Static resources
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/favicon.ico"
                        ).permitAll()

                        // =========================
                        // LOGIN
                        // =========================
                        .requestMatchers("/login")
                        .permitAll()

                        // =========================
                        // USER REGISTRATION
                        // =========================
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/users"
                        ).permitAll()

                        // =========================
                        // PROFILE APIs
                        // =========================
                        .requestMatchers("/api/profiles/**")
                        .permitAll()

                        // =========================
                        // ADMIN USER APIs
                        // =========================

                        // Only ADMIN can get all users
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/users"
                        ).authenticated()

                        // Only ADMIN can delete users
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/users/**"
                        ).hasRole("ADMIN")

                        // =========================
                        // OTHER AUTHENTICATED APIs
                        // =========================
                        .anyRequest()
                        .authenticated()
                )

                // =========================
                // FORM LOGIN
                // =========================
                .formLogin(form -> form
                        .loginPage("/login.html")
                        .loginProcessingUrl("/login")
                        .successHandler(successHandler)
                        .failureUrl("/login.html?error=true")
                        .permitAll()
                )

                // =========================
                // LOGOUT
                // =========================
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl(
                                "/login.html?logout=true"
                        )
                        .permitAll()
                );

        return http.build();
    }
}