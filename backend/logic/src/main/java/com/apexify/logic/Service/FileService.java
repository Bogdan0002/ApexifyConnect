package com.apexify.logic.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FileService {

    // Define the upload directory
    private final String uploadDir = "C:/Users/felly/OneDrive/Desktop/VIAUC/BPR/Uploads";

    public FileService() {
        // Ensure the directory exists
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public String uploadFile(MultipartFile file) throws IOException {
        if (file == null) {
            throw new NullPointerException("File is null");
        }
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && originalFilename.endsWith(".exe")) {
            throw new IllegalArgumentException("Unsupported file type");
        }

        String fileName = System.currentTimeMillis() + "_" + originalFilename;
        File uploadFile = new File(uploadDir, fileName);
        try (FileOutputStream fos = new FileOutputStream(uploadFile)) {
            fos.write(file.getBytes());
        }

        return "/uploads/" + fileName;
    }
}
