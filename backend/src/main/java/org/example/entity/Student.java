package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "students")
@Data
public class Student {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false)
    private LocalDate dob;

    @Column(name = "class", nullable = false, length = 20)
    private String studentClass;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer score = 0;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 1")
    private Integer status = 1; // 1 = active, 0 = inactive

    @Column(name = "photo_path", length = 255)
    private String photoPath;

    // Additional recommended fields
    @Column(name = "created_at", updatable = false)
    private LocalDate createdAt = LocalDate.now();

    @Column(name = "updated_at")
    private LocalDate updatedAt;
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDate.now();
    }
}