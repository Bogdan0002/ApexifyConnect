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
            // Debug statement to confirm file details
            System.out.println("Received file: " + (file != null ? file.getOriginalFilename() : "null"));
            System.out.println("File size: " + (file != null ? file.getSize() : 0));

            if (file == null || file.isEmpty()) {
                return ResponseEntity.status(500).body("File upload failed: File is empty");
            }

            String originalFilename = file.getOriginalFilename();
            if (originalFilename != null && originalFilename.endsWith(".exe")) {
                return ResponseEntity.status(500).body("File upload failed: Unsupported file type");
            }

            String fileUrl = fileService.uploadFile(file);
            return ResponseEntity.ok(fileUrl);
        } catch (Exception e) {
            e.printStackTrace(); // Debug stack trace
            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
        }
    }

}
