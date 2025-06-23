package org.example;

import org.example.entity.User;
import org.example.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
public class StudentManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentManagementApplication.class, args);
    }

    /**
     * Initializes the database with test users when the application starts
     * This runs after the application context is loaded
     */
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Create admin user if not exists
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setEmail("admin@school.com");
                admin.setRoles(List.of("ROLE_ADMIN")); // Add roles
                userRepository.save(admin);
                System.out.println("Admin user created successfully");
            }

            // Create teacher user if not exists
            if (userRepository.findByUsername("teacher").isEmpty()) {
                User teacher = new User();
                teacher.setUsername("teacher");
                teacher.setPassword(passwordEncoder.encode("teacher123"));
                teacher.setEmail("teacher@school.com");
                teacher.setRoles(List.of("ROLE_TEACHER")); // Add roles
                userRepository.save(teacher);
                System.out.println("Teacher user created successfully");
            }

            // Create staff user if not exists
            if (userRepository.findByUsername("staff").isEmpty()) {
                User staff = new User();
                staff.setUsername("staff");
                staff.setPassword(passwordEncoder.encode("staff123"));
                staff.setEmail("staff@school.com");
                staff.setRoles(List.of("ROLE_STAFF")); // Add roles
                userRepository.save(staff);
                System.out.println("Staff user created successfully");
            }
        };
    }
}