package com.apexify.logic.unit;


import com.apexify.logic.Controller.FileUploadController;
import com.apexify.logic.Service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FileUploadControllerTest {

    @InjectMocks
    private FileUploadController fileUploadController;

    @Mock
    private FileService fileService;

    @Mock
    private MultipartFile multipartFile;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUploadProfilePicture_Success() throws Exception {
        String fileUrl = "/uploads/test.txt";
        when(fileService.uploadFile(multipartFile)).thenReturn(fileUrl);

        ResponseEntity<String> response = fileUploadController.uploadProfilePicture(multipartFile);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(fileUrl, response.getBody());
    }

    @Test
    public void testUploadProfilePicture_Failure() throws Exception {
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getOriginalFilename()).thenReturn("testfile.txt");
        doThrow(new IOException("File upload failed")).when(fileService).uploadFile(multipartFile);

        ResponseEntity<String> response = fileUploadController.uploadProfilePicture(multipartFile);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("File upload failed: File upload failed", response.getBody());
    }

    @Test
    public void testUploadProfilePicture_UnsupportedFileType() throws Exception {
        when(multipartFile.getOriginalFilename()).thenReturn("unsupported.exe");

        ResponseEntity<String> response = fileUploadController.uploadProfilePicture(multipartFile);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("File upload failed: Unsupported file type", response.getBody());
    }

    @Test
    public void testUploadProfilePicture_LargeFile() throws Exception {
        byte[] largeFileContent = new byte[10 * 1024 * 1024]; // 10 MB
        when(multipartFile.getBytes()).thenReturn(largeFileContent);
        when(multipartFile.getOriginalFilename()).thenReturn("largefile.txt");

        String fileUrl = "/uploads/largefile.txt";
        when(fileService.uploadFile(multipartFile)).thenReturn(fileUrl);

        ResponseEntity<String> response = fileUploadController.uploadProfilePicture(multipartFile);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(fileUrl, response.getBody());
    }

    @Test
    public void testUploadProfilePicture_EmptyFile() throws Exception {
        when(multipartFile.isEmpty()).thenReturn(true);

        ResponseEntity<String> response = fileUploadController.uploadProfilePicture(multipartFile);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("File upload failed: File is empty", response.getBody());
    }
}

