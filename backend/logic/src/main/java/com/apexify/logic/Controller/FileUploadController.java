package com.apexify.logic.Controller;

import com.apexify.logic.Service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/uploads")
@CrossOrigin(origins = "http://localhost:5173")
public class FileUploadController {

    private final FileService fileService;

    // Inject the FileService using constructor injection
    public FileUploadController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/profile-picture")
    public ResponseEntity<String> uploadProfilePicture(@RequestParam("file") MultipartFile file) {
        try {
            // Use the FileService to handle the upload
            String fileUrl = fileService.uploadFile(file);
            return ResponseEntity.ok(fileUrl);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
        }
    }
}
