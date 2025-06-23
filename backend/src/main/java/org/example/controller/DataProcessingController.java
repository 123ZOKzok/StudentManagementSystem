package org.example.controller;

import org.example.service.DataProcessingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@RequestMapping("/api/data-processing")
public class DataProcessingController {

    private final DataProcessingService dataProcessingService;

    public DataProcessingController(DataProcessingService dataProcessingService) {
        this.dataProcessingService = dataProcessingService;
    }

    @PostMapping("/process-excel")
    public ResponseEntity<String> processLatestExcelFile() {
        try {
            File csvFile = dataProcessingService.processLatestExcelToCsv();
            return ResponseEntity.ok(csvFile.getAbsolutePath());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error processing Excel file: " + e.getMessage());
        }
    }
}
