package com.alumniportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class AlumniPortalBackendApplication {

    public static void main(String[] args) {

        BCryptPasswordEncoder encoder =
                new BCryptPasswordEncoder();

        String encodedPassword =
                encoder.encode("Rahul@123");

        System.out.println("======================================");
        System.out.println("BCrypt Password:");
        System.out.println(encodedPassword);
        System.out.println("======================================");

        SpringApplication.run(
                AlumniPortalBackendApplication.class,
                args
        );
    }
}
