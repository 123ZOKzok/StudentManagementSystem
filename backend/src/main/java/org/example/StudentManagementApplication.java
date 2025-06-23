package org.example;

import org.example.entity.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
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

    @Bean
    CommandLineRunner initDatabase(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.default.admin.username}") String adminUsername,
            @Value("${app.default.admin.password}") String adminPassword,
            @Value("${app.default.admin.email}") String adminEmail,
            @Value("${app.default.teacher.username}") String teacherUsername,
            @Value("${app.default.teacher.password}") String teacherPassword,
            @Value("${app.default.teacher.email}") String teacherEmail,
            @Value("${app.default.staff.username}") String staffUsername,
            @Value("${app.default.staff.password}") String staffPassword,
            @Value("${app.default.staff.email}") String staffEmail
    ) {
        return args -> {
            if (userRepository.findByUsername(adminUsername).isEmpty()) {
                User admin = new User();
                admin.setUsername(adminUsername);
                admin.setPassword(passwordEncoder.encode(adminPassword));
                admin.setEmail(adminEmail);
                admin.setRoles(List.of("ROLE_ADMIN"));
                userRepository.save(admin);
                System.out.println("Admin user created successfully");
            }

            if (userRepository.findByUsername(teacherUsername).isEmpty()) {
                User teacher = new User();
                teacher.setUsername(teacherUsername);
                teacher.setPassword(passwordEncoder.encode(teacherPassword));
                teacher.setEmail(teacherEmail);
                teacher.setRoles(List.of("ROLE_TEACHER"));
                userRepository.save(teacher);
                System.out.println("Teacher user created successfully");
            }

            if (userRepository.findByUsername(staffUsername).isEmpty()) {
                User staff = new User();
                staff.setUsername(staffUsername);
                staff.setPassword(passwordEncoder.encode(staffPassword));
                staff.setEmail(staffEmail);
                staff.setRoles(List.of("ROLE_STAFF"));
                userRepository.save(staff);
                System.out.println("Staff user created successfully");
            }
        };
    }
}
