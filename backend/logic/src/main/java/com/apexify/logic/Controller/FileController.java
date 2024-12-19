package com.apexify.logic.Controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@CrossOrigin
public class FileController {

    @GetMapping({"/uploads/{fileName}", "/Uploads/{fileName}"})
    public ResponseEntity<Resource> serveFile(@PathVariable String fileName) throws IOException {
        String uploadPath = "C:/Users/felly/OneDrive/Desktop/VIAUC/BPR/Uploads/";
        Path filePath = Paths.get(uploadPath + fileName);
        Resource resource = new FileSystemResource(filePath.toFile());

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .body(resource);
    }
}
