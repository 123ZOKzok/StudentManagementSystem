package org.example.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Data
public class StudentDto {

    @NotBlank(message = "First name is required")
    @Size(max = 15, message = "First name must be less than 15 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 15, message = "Last name must be less than 15 characters")
    private String lastName;

    @NotNull(message = "Date of birth is required")
    private LocalDate dob;

    @NotBlank(message = "Class is required")
    @Size(max = 20, message = "Class name must be less than 20 characters")
    private String studentClass;

    @NotNull(message = "Score is required")
    @Min(value = 0, message = "Score must be at least 0")
    @Max(value = 100, message = "Score must not exceed 100")
    private Integer score;
}
