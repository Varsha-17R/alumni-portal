package com.alumniportal.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
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

    @Async
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


    // =========================================================
    // SEND EMAIL VERIFICATION OTP
    // =========================================================

    @Async
    public void sendEmailVerificationOtp(
            String toEmail,
            String name,
            String otp) {

        try {

            SimpleMailMessage message =
                    new SimpleMailMessage();

            message.setFrom(fromEmail);
            message.setTo(toEmail);

            message.setSubject(
                    "Alumni Portal - Email Verification OTP"
            );

            message.setText(
                    "Hello " + name + ",\n\n" +

                            "Thank you for registering with the Alumni Portal.\n\n" +

                            "Your email verification OTP is:\n\n" +

                            otp + "\n\n" +

                            "This OTP is valid for 5 minutes.\n\n" +

                            "Please do not share this OTP with anyone.\n\n" +

                            "If you did not request this verification, " +
                            "please ignore this email.\n\n" +

                            "Regards,\n" +
                            "Alumni Portal Team"
            );

            mailSender.send(message);

            System.out.println(
                    "Email verification OTP sent successfully to: "
                            + toEmail
            );

        } catch (Exception e) {

            System.out.println(
                    "Email verification OTP could not be sent: "
                            + e.getMessage()
            );
        }
    }


    // =========================================================
    // MENTORSHIP REQUEST NOTIFICATION
    // =========================================================

    public void sendMentorshipRequestNotification(
            String toEmail,
            String alumniName,
            String studentName) {

        try {

            SimpleMailMessage message =
                    new SimpleMailMessage();

            message.setFrom(fromEmail);
            message.setTo(toEmail);

            message.setSubject(
                    "New Mentorship Request - Alumni Portal"
            );

            message.setText(
                    "Hello " + alumniName + ",\n\n" +

                            "You have received a new mentorship request " +
                            "from " + studentName + ".\n\n" +

                            "Please log in to the Alumni Portal to " +
                            "view and respond to the request.\n\n" +

                            "Regards,\n" +
                            "Alumni Portal Team"
            );

            mailSender.send(message);

        } catch (Exception e) {

            System.out.println(
                    "Mentorship notification could not be sent: "
                            + e.getMessage()
            );
        }
    }


    // =========================================================
    // MENTORSHIP STATUS NOTIFICATION
    // =========================================================

    public void sendMentorshipStatusNotification(
            String toEmail,
            String studentName,
            String alumniName,
            String status) {

        try {

            SimpleMailMessage message =
                    new SimpleMailMessage();

            message.setFrom(fromEmail);
            message.setTo(toEmail);

            message.setSubject(
                    "Mentorship Request Update - Alumni Portal"
            );

            message.setText(
                    "Hello " + studentName + ",\n\n" +

                            "Your mentorship request to " +
                            alumniName + " has been " +
                            status.toLowerCase() + ".\n\n" +

                            "Please log in to the Alumni Portal " +
                            "for more details.\n\n" +

                            "Regards,\n" +
                            "Alumni Portal Team"
            );

            mailSender.send(message);

        } catch (Exception e) {

            System.out.println(
                    "Mentorship status notification could not be sent: "
                            + e.getMessage()
            );
        }
    }


    // =========================================================
    // JOB APPLICATION NOTIFICATION
    // =========================================================

    public void sendJobApplicationNotification(
            String toEmail,
            String applicantName,
            String jobTitle) {

        try {

            SimpleMailMessage message =
                    new SimpleMailMessage();

            message.setFrom(fromEmail);
            message.setTo(toEmail);

            message.setSubject(
                    "New Job Application - Alumni Portal"
            );

            message.setText(
                    "Hello,\n\n" +

                            applicantName +
                            " has applied for the job:\n\n" +

                            "Job Title: " + jobTitle + "\n\n" +

                            "Please log in to the Alumni Portal " +
                            "to review the application.\n\n" +

                            "Regards,\n" +
                            "Alumni Portal Team"
            );

            mailSender.send(message);

        } catch (Exception e) {

            System.out.println(
                    "Job application notification could not be sent: "
                            + e.getMessage()
            );
        }
    }


    // =========================================================
    // JOB APPLICATION STATUS NOTIFICATION
    // =========================================================

    public void sendJobApplicationStatusNotification(
            String toEmail,
            String applicantName,
            String jobTitle,
            String status) {

        try {

            SimpleMailMessage message =
                    new SimpleMailMessage();

            message.setFrom(fromEmail);
            message.setTo(toEmail);

            message.setSubject(
                    "Job Application Update - Alumni Portal"
            );

            message.setText(
                    "Hello " + applicantName + ",\n\n" +

                            "There is an update to your application.\n\n" +

                            "Job Title: " + jobTitle + "\n" +
                            "Application Status: " + status + "\n\n" +

                            "Please log in to the Alumni Portal " +
                            "for more details.\n\n" +

                            "Regards,\n" +
                            "Alumni Portal Team"
            );

            mailSender.send(message);

        } catch (Exception e) {

            System.out.println(
                    "Job application status notification "
                            + "could not be sent: "
                            + e.getMessage()
            );
        }
    }


    // =========================================================
    // EVENT NOTIFICATION
    // =========================================================

    public void sendEventNotification(
            String toEmail,
            String name,
            String eventTitle,
            String eventDate,
            String eventLocation) {

        try {

            SimpleMailMessage message =
                    new SimpleMailMessage();

            message.setFrom(fromEmail);
            message.setTo(toEmail);

            message.setSubject(
                    "New Event Update - Alumni Portal"
            );

            message.setText(
                    "Hello " + name + ",\n\n" +

                            "A new event has been added to the " +
                            "Alumni Portal.\n\n" +

                            "Event: " + eventTitle + "\n" +
                            "Date: " + eventDate + "\n" +
                            "Location: " + eventLocation + "\n\n" +

                            "Please log in to the Alumni Portal " +
                            "for more information.\n\n" +

                            "Regards,\n" +
                            "Alumni Portal Team"
            );

            mailSender.send(message);

        } catch (Exception e) {

            System.out.println(
                    "Event notification could not be sent: "
                            + e.getMessage()
            );
        }
    }


    // =========================================================
    // GENERAL PORTAL NOTIFICATION
    // =========================================================

    public void sendPortalNotification(
            String toEmail,
            String name,
            String subject,
            String notificationMessage) {

        try {

            SimpleMailMessage message =
                    new SimpleMailMessage();

            message.setFrom(fromEmail);
            message.setTo(toEmail);

            message.setSubject(subject);

            message.setText(
                    "Hello " + name + ",\n\n" +

                            notificationMessage + "\n\n" +

                            "Please log in to the Alumni Portal " +
                            "for more details.\n\n" +

                            "Regards,\n" +
                            "Alumni Portal Team"
            );

            mailSender.send(message);

        } catch (Exception e) {

            System.out.println(
                    "Portal notification could not be sent: "
                            + e.getMessage()
            );
        }
    }
}