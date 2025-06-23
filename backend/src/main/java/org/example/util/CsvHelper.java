package org.example.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.example.entity.Student;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CsvHelper {
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static ByteArrayInputStream studentsToCSV(List<Student> students) {
        // Create CSV format with headers
        final CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader("ID", "First Name", "Last Name", "Date of Birth",
                        "Class", "Score", "Status")
                .build();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             PrintWriter writer = new PrintWriter(out);
             CSVPrinter csvPrinter = new CSVPrinter(writer, format)) {

            // Write records
            for (Student student : students) {
                csvPrinter.printRecord(
                        student.getStudentId(),
                        student.getFirstName(),
                        student.getLastName(),
                        student.getDob() != null ?
                                student.getDob().format(DATE_FORMATTER) : "",
                        student.getStudentClass(),
                        student.getScore(),
                        student.getStatus() == 1 ? "Active" : "Inactive"
                );
            }

            // Ensure all data is written
            csvPrinter.flush();
            writer.flush();

            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new CsvExportException("Failed to generate CSV", e);
        }
    }

    // Custom exception for CSV export errors
    public static class CsvExportException extends RuntimeException {
        public CsvExportException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}