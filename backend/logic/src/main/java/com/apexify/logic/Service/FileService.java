package com.apexify.logic.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Service class responsible for handling file operations in the ApexifyConnect platform.
 * Manages file uploads and ensures proper storage in the designated directory.
 * Mainly used for uploading pictures.
 */
@Service
public class FileService {

    /**
     * The directory path where uploaded files will be stored
     */
    private final String uploadDir = "C:/Users/felly/OneDrive/Desktop/VIAUC/BPR/Uploads";

    /**
     * Initializes the FileService and creates the upload directory if it doesn't exist
     */
    public FileService() {
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    /**
     * Uploads a file to the designated directory with a unique timestamp-based filename.
     * Used for uploading pictures
     *
     * @param file The MultipartFile to be uploaded
     * @return The relative path to the uploaded file
     * @throws IOException if there's an error during file writing
     * @throws NullPointerException if the file is null
     * @throws IllegalArgumentException if the file is empty or has an unsupported type
     */
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

        return "/Uploads/" + fileName;
    }
}
