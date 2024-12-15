package com.apexify.logic.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
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

    public String uploadFile(MultipartFile file) throws Exception {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File targetFile = new File(uploadDir, fileName);

        // Create directories if not exist
        targetFile.getParentFile().mkdirs();

        try (FileOutputStream fos = new FileOutputStream(targetFile)) {
            fos.write(file.getBytes());
        }

        return "/uploads/" + fileName; // This path should be accessible from your frontend
    }
}
