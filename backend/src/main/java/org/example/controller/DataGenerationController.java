package org.example.controller;

import jakarta.servlet.http.HttpServletResponse; 
import org.example.service.DataGenerationService;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Files;

@RestController
@RequestMapping("/api/data-generation")
public class DataGenerationController {

    private final DataGenerationService dataGenerationService;

    public DataGenerationController(DataGenerationService dataGenerationService) {
        this.dataGenerationService = dataGenerationService;
    }

    @PostMapping("/generate-students")
    public void generateStudents(@RequestParam int recordCount, HttpServletResponse response) {
        try {
            File generatedFile = dataGenerationService.generateStudentData(recordCount);

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" + generatedFile.getName());

            Files.copy(generatedFile.toPath(), response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            throw new RuntimeException("Error generating Excel file: " + e.getMessage());
        }
    }
}
