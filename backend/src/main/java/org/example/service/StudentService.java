package org.example.service;

import org.example.dto.StudentDto;
import org.example.entity.Student;
import org.example.exception.InvalidFileFormatException;
import org.example.exception.ResourceNotFoundException;
import org.example.repository.StudentRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String[] CSV_HEADERS = {
        "First Name", "Last Name", "Date of Birth", "Class", "Score"
    };

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    //CSV Processing
    public int processAndSaveStudents(MultipartFile file) throws IOException, InvalidFileFormatException {
        validateCsvFile(file);
        List<Student> students = parseStudentsFromCsv(file);
        studentRepository.saveAll(students);
        return students.size();
    }

    private void validateCsvFile(MultipartFile file) throws InvalidFileFormatException {
        String contentType = Optional.ofNullable(file.getContentType()).orElse("");
        String fileName = Optional.ofNullable(file.getOriginalFilename()).orElse("");
        if (!contentType.equals("text/csv") && !fileName.toLowerCase().endsWith(".csv")) {
            throw new InvalidFileFormatException("Only CSV files are supported");
        }
    }

    private List<Student> parseStudentsFromCsv(MultipartFile file) throws IOException, InvalidFileFormatException {
        List<Student> students = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             CSVParser csvParser = CSVFormat.DEFAULT.builder()
                     .setHeader(CSV_HEADERS)
                     .setSkipHeaderRecord(true)
                     .setIgnoreHeaderCase(true)
                     .setTrim(true)
                     .build()
                     .parse(reader)) {

            for (CSVRecord record : csvParser) {
                students.add(createStudentFromRecord(record));
            }
        } catch (IllegalArgumentException e) {
            throw new InvalidFileFormatException("Missing required columns in CSV file");
        }

        return students;
    }

    private Student createStudentFromRecord(CSVRecord record) throws InvalidFileFormatException {
        try {
            Student student = new Student();
            student.setFirstName(record.get("First Name"));
            student.setLastName(record.get("Last Name"));
            student.setDob(parseDate(record.get("Date of Birth")));
            student.setStudentClass(record.get("Class"));
            student.setScore(Integer.parseInt(record.get("Score")));
            student.setStatus(1); // Active
            return student;
        } catch (Exception e) {
            throw new InvalidFileFormatException(
                "Invalid data format in record #" + record.getRecordNumber() + ": " + e.getMessage());
        }
    }

    private LocalDate parseDate(String dateString) {
        return LocalDate.parse(dateString, DATE_FORMATTER);
    }

    //Student CRUD Operations
    public Page<Student> getAllStudents(Pageable pageable) {
        return studentRepository.findAllByStatus(1, pageable); // Only active students
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    public Student createStudent(StudentDto studentDto) {
        Student student = new Student();
        mapDtoToEntity(studentDto, student);
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, StudentDto studentDto) {
        Student student = getStudentById(id);
        mapDtoToEntity(studentDto, student);
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        Student student = getStudentById(id);
        student.setStatus(0); // Soft delete
        studentRepository.save(student);
    }

    private void mapDtoToEntity(StudentDto dto, Student entity) {
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setDob(dto.getDob());
        entity.setStudentClass(dto.getStudentClass());
        entity.setScore(dto.getScore());
    }

    //Photo Upload
    public Student uploadPhoto(Long studentId, MultipartFile file) throws IOException {
        Student student = getStudentById(studentId);
        String photoPath = storePhotoFile(file, studentId);
        student.setPhotoPath(photoPath);
        return studentRepository.save(student);
    }

    private String storePhotoFile(MultipartFile file, Long studentId) throws IOException {
        String extension = file.getContentType() != null && file.getContentType().equals("image/jpeg") ? ".jpg" : ".png";
        String fileName = "student_" + studentId + "_" + System.currentTimeMillis() + extension;

        Path uploadPath = Paths.get("uploads/photos");
        Files.createDirectories(uploadPath);

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        }

        return fileName;
    }

    // Additional Methods
    public List<Student> saveAllStudents(List<Student> students) {
        return studentRepository.saveAll(students);
    }

    public long countStudents() {
        return studentRepository.count();
    }
}
