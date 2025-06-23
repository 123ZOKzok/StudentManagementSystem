package org.example.controller;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.dto.StudentDto;
import org.example.entity.Student;
import org.example.exception.FileUploadException;
import org.example.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping(value = "/api/students", produces = MediaType.APPLICATION_JSON_VALUE)
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Count endpoint
    @GetMapping("/count")
    public ResponseEntity<Long> getStudentCount() {
        return ResponseEntity.ok(studentService.countStudents());
    }

    //Updated pagination endpoint
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("studentId").descending());
        Page<Student> pageStudents = studentService.getAllStudents(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("content", pageStudents.getContent());
        response.put("currentPage", pageStudents.getNumber());
        response.put("totalItems", pageStudents.getTotalElements());
        response.put("totalPages", pageStudents.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Student> createStudent(@Valid @RequestBody StudentDto studentDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(studentService.createStudent(studentDto));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Student> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentDto studentDto) {
        return ResponseEntity.ok(studentService.updateStudent(id, studentDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{id}/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Student> uploadPhoto(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(studentService.uploadPhoto(id, file));
        } catch (IOException e) {
            throw new FileUploadException("Failed to upload student photo: " + e.getMessage());
        }
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<Student>> importStudents(@RequestParam("file") MultipartFile file) {
        try {
            List<Student> importedStudents = parseExcelFile(file.getInputStream());
            List<Student> savedStudents = studentService.saveAllStudents(importedStudents);
            return ResponseEntity.ok(savedStudents);
        } catch (IOException e) {
            throw new FileUploadException("Failed to import students: " + e.getMessage());
        }
    }

    // Excel parser helpers
    private List<Student> parseExcelFile(InputStream is) throws IOException {
        List<Student> students = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            if (rows.hasNext()) rows.next(); // Skip header

            while (rows.hasNext()) {
                Row currentRow = rows.next();
                Student student = new Student();

                student.setFirstName(getStringCellValue(currentRow, 0));
                student.setLastName(getStringCellValue(currentRow, 1));
                student.setDob(getDateCellValue(currentRow, 2));
                student.setStudentClass(getStringCellValue(currentRow, 3));
                student.setScore(getNumericCellValue(currentRow, 4));
                student.setStatus(1);
                students.add(student);
            }
        }
        return students;
    }

    private String getStringCellValue(Row row, int cellNum) {
        Cell cell = row.getCell(cellNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        return cell.getStringCellValue();
    }

    private LocalDate getDateCellValue(Row row, int cellNum) {
        Cell cell = row.getCell(cellNum);
        return cell != null ? cell.getLocalDateTimeCellValue().toLocalDate() : null;
    }

    private Integer getNumericCellValue(Row row, int cellNum) {
        Cell cell = row.getCell(cellNum);
        return cell != null ? (int) cell.getNumericCellValue() : null;
    }
}
