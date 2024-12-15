package com.apexify.logic.unit;

import com.apexify.logic.Service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FileServiceTest {

    private FileService fileService;

    @Mock
    private MultipartFile multipartFile;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        fileService = new FileService();
    }

    @Test
    public void testUploadFile() throws Exception {
        String originalFileName = "test.txt";
        byte[] fileContent = "Hello, World!".getBytes();

        when(multipartFile.getOriginalFilename()).thenReturn(originalFileName);
        when(multipartFile.getBytes()).thenReturn(fileContent);

        String result = fileService.uploadFile(multipartFile);

        assertNotNull(result);
        assertTrue(result.startsWith("/uploads/"));
        assertTrue(result.endsWith("_" + originalFileName));

        File uploadedFile = new File("C:/Users/felly/OneDrive/Desktop/VIAUC/BPR/Uploads", result.substring(9));
        assertTrue(uploadedFile.exists());
        assertArrayEquals(fileContent, Files.readAllBytes(uploadedFile.toPath()));

        // Clean up
        uploadedFile.delete();
    }

    @Test
    public void testUploadFile_NullFile() {
        MultipartFile file = null;

        assertThrows(NullPointerException.class, () -> {
            fileService.uploadFile(file);
        });
    }

    @Test
    public void testUploadFile_EmptyFile() throws Exception {
        when(multipartFile.isEmpty()).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            fileService.uploadFile(multipartFile);
        });
    }

    @Test
    public void testUploadFile_LargeFile() throws Exception {
        byte[] largeFileContent = new byte[10 * 1024 * 1024]; // 10 MB
        when(multipartFile.getBytes()).thenReturn(largeFileContent);
        when(multipartFile.getOriginalFilename()).thenReturn("largefile.txt");

        String result = fileService.uploadFile(multipartFile);

        assertNotNull(result);
        assertTrue(result.startsWith("/uploads/"));
        assertTrue(result.endsWith("_largefile.txt"));
    }

    @Test
    public void testUploadFile_UnsupportedFileType() throws Exception {
        when(multipartFile.getOriginalFilename()).thenReturn("unsupported.exe");

        assertThrows(IllegalArgumentException.class, () -> {
            fileService.uploadFile(multipartFile);
        });
    }
}
