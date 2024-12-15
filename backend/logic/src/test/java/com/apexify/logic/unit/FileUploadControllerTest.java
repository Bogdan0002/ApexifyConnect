package com.apexify.logic.unit;

import com.apexify.logic.Controller.FileUploadController;
import com.apexify.logic.Service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
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

//    @Test
//    public void testUploadProfilePicture_Success() throws IOException {
//        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test content".getBytes());
//
//        when(fileService.uploadFile(any(MultipartFile.class))).thenReturn("/Uploads/test.txt");
//
//        ResponseEntity<String> response = fileUploadController.uploadProfilePicture(file);
//
//        verify(fileService, times(1)).uploadFile(any(MultipartFile.class));
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("/Uploads/test.txt", response.getBody());
//    }




    @Test
    public void testUploadProfilePicture_Failure() {
        MockMultipartFile file = new MockMultipartFile("file", "", "text/plain", "".getBytes());
        ResponseEntity<String> response = fileUploadController.uploadProfilePicture(file);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testUploadProfilePicture_UnsupportedFileType() throws Exception {
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getOriginalFilename()).thenReturn("unsupported.exe");

        ResponseEntity<String> response = fileUploadController.uploadProfilePicture(multipartFile);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("File upload failed: Unsupported file type", response.getBody());
    }

//    @Test
//    public void testUploadProfilePicture_LargeFile() throws IOException {
//        byte[] largeFileContent = new byte[1024 * 1024 * 10]; // 10MB
//        MockMultipartFile file = new MockMultipartFile("file", "largefile.txt", "text/plain", largeFileContent);
//
//        when(fileService.uploadFile(any(MultipartFile.class))).thenReturn("/Uploads/largefile.txt");
//
//        ResponseEntity<String> response = fileUploadController.uploadProfilePicture(file);
//
//        verify(fileService, times(1)).uploadFile(any(MultipartFile.class));
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("/Uploads/largefile.txt", response.getBody());
//    }




    @Test
    public void testUploadProfilePicture_EmptyFile() throws Exception {
        when(multipartFile.isEmpty()).thenReturn(true);

        ResponseEntity<String> response = fileUploadController.uploadProfilePicture(multipartFile);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("File upload failed: File is empty", response.getBody());
    }
}