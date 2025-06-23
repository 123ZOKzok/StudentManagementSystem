package org.example.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    @Value("${app.file.storage.location}")
    private String fileStorageLocation;

    public String storeFile(MultipartFile file, String subDirectory) throws IOException {
        Path storagePath = Paths.get(fileStorageLocation, subDirectory);
        if (!Files.exists(storagePath)) {
            Files.createDirectories(storagePath);
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path targetLocation = storagePath.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        return targetLocation.toString();
    }

    public byte[] loadFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readAllBytes(path);
    }

    public void deleteFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.deleteIfExists(path);
    }
}