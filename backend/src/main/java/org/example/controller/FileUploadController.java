package org.example.controller;

import org.example.exception.InvalidFileFormatException;
import org.example.service.FileStorageService;
import org.example.service.StudentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/file-upload")
public class FileUploadController {

    private final StudentService studentService;
    private final FileStorageService fileStorageService;

    @Value("${app.file.storage.location}")
    private String fileStorageLocation;

    public FileUploadController(StudentService studentService, FileStorageService fileStorageService) {
        this.studentService = studentService;
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/students")
    public ResponseEntity<?> uploadStudents(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select a file to upload");
        }

        try {
            // Store the file in a subdirectory "students"
            String storedPath = fileStorageService.storeFile(file, "students");

            // Process and save students
            int processedCount = studentService.processAndSaveStudents(file);

            return ResponseEntity.ok(Map.of(
                "message", "Successfully processed " + processedCount + " students",
                "processedCount", processedCount,
                "filePath", storedPath
            ));
        } catch (InvalidFileFormatException e) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error reading or storing file: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error processing file: " + e.getMessage());
        }
    }
}
