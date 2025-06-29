package org.example.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Service
public class DataGenerationService {

    @Value("${app.data.processing.location}")
    private String dataProcessingLocation;

    private final Random random = new Random();

    public File generateStudentData(int recordCount) throws IOException {
        // Use SXSSFWorkbook for low-memory writing
        SXSSFWorkbook workbook = new SXSSFWorkbook(100);
        Sheet sheet = workbook.createSheet("Students");

        // Create header row
        String[] headers = {"studentId", "firstName", "lastName", "DOB", "class", "score", "status", "photoPath"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        // Generate student data rows
        for (int i = 1; i <= recordCount; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(i);
            row.createCell(1).setCellValue(generateRandomName(3, 8));
            row.createCell(2).setCellValue(generateRandomName(3, 8));
            row.createCell(3).setCellValue(generateRandomDob().toString());
            row.createCell(4).setCellValue("Class" + (random.nextInt(5) + 1));
            row.createCell(5).setCellValue(55 + random.nextInt(31));
            row.createCell(6).setCellValue(1);
            row.createCell(7).setCellValue("");
        }

        // Ensure target directory exists
        Path dirPath = Paths.get(dataProcessingLocation);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        // Build file path
        String fileName = "students_" + System.currentTimeMillis() + ".xlsx";
        Path filePath = dirPath.resolve(fileName);

        // Write and flush to file
        try (FileOutputStream outputStream = new FileOutputStream(filePath.toFile())) {
            workbook.write(outputStream);
            workbook.dispose(); // Clean up temp files
        }

        return filePath.toFile();
    }

    private String generateRandomName(int minLength, int maxLength) {
        int length = minLength + random.nextInt(maxLength - minLength + 1);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char c = (char) ('a' + random.nextInt(26));
            if (i == 0) c = Character.toUpperCase(c);
            sb.append(c);
        }
        return sb.toString();
    }

    private LocalDate generateRandomDob() {
        LocalDate start = LocalDate.of(2000, 1, 1);
        LocalDate end = LocalDate.of(2010, 12, 31);
        long days = ChronoUnit.DAYS.between(start, end);
        return start.plusDays(random.nextInt((int) days + 1));
    }
}
