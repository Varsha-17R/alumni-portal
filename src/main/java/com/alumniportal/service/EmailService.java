package com.alumniportal.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // =========================================================
    // SEND WELCOME EMAIL
    // =========================================================

    public void sendWelcomeEmail(
            String toEmail,
            String name,
            String role) {

        try {

            SimpleMailMessage message =
                    new SimpleMailMessage();

            message.setFrom(fromEmail);

            message.setTo(toEmail);

            message.setSubject(
                    "Welcome to Alumni Portal"
            );

            message.setText(
                    "Hello " + name + ",\n\n" +

                            "Welcome to the Alumni Portal!\n\n" +

                            "Your registration has been completed successfully.\n\n" +

                            "Name: " + name + "\n" +
                            "Email: " + toEmail + "\n" +
                            "Role: " + role + "\n\n" +

                            "You can now log in to the Alumni Portal " +
                            "using your registered email and password.\n\n" +

                            "Thank you for joining our Alumni Portal.\n\n" +

                            "Regards,\n" +
                            "Alumni Portal Team"
            );

            mailSender.send(message);

            System.out.println(
                    "Welcome email sent successfully to: "
                            + toEmail
            );

        } catch (Exception e) {

            System.out.println(
                    "Welcome email could not be sent: "
                            + e.getMessage()
            );
        }
    }
}