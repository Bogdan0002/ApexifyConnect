package com.apexify.logic.integration;

import com.apexify.logic.Service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class FileServiceIntegrationTest {

    @Autowired
    private FileService fileService;

    private final String uploadDir = "C:/Users/felly/OneDrive/Desktop/VIAUC/BPR/Uploads";
    private static final Logger logger = Logger.getLogger(FileServiceIntegrationTest.class.getName());

    @BeforeEach
    public void setUp() throws Exception {
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            logger.info("Directory created: " + created);
        } else {
            logger.info("Directory already exists: " + uploadDir);
        }

        Files.walk(Paths.get(uploadDir))
                .map(java.nio.file.Path::toFile)
                .forEach(File::delete);
    }

    @Test
    public void testUploadFile_NullFile() {
        assertThrows(NullPointerException.class, () -> {
            fileService.uploadFile(null);
        });
    }

    @Test
    public void testUploadFile_EmptyFile() {
        MockMultipartFile emptyFile = new MockMultipartFile("file", "empty.txt", "text/plain", new byte[0]);

        assertThrows(IllegalArgumentException.class, () -> {
            fileService.uploadFile(emptyFile);
        });
    }


    @Test
    public void testUploadFile_UnsupportedFileType() {
        MockMultipartFile unsupportedFile = new MockMultipartFile("file", "unsupported.exe", "application/octet-stream", new byte[10]);

        assertThrows(IllegalArgumentException.class, () -> {
            fileService.uploadFile(unsupportedFile);
        });
    }

}