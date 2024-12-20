//package com.apexify.logic.unit;
//
//import com.apexify.logic.Controller.ApplicationController;
//import com.apexify.logic.Service.ApplicationService;
//import com.apexifyconnect.Model.Application;
//import com.apexifyconnect.Model.ApplicationStatus;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//class ApplicationControllerTest {
//
//    @Mock
//    private ApplicationService applicationService;
//
//    private ApplicationController applicationController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        applicationController = new ApplicationController(applicationService);
//    }
//
//    @Test
//    void apply_Success() {
//        // Arrange
//        Long jobPostId = 1L;
//        Long creatorId = 1L;
//        Application mockApplication = new Application();
//        when(applicationService.createApplication(jobPostId, creatorId)).thenReturn(mockApplication);
//
//        // Act
//        ResponseEntity<Application> response = applicationController.apply(jobPostId, creatorId, "Cover Letter");
//
//        // Assert
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        verify(applicationService).createApplication(jobPostId, creatorId);
//    }
//
//    @Test
//    void getApplicationsForJob_Success() {
//        // Arrange
//        Long jobId = 1L;
//        List<Application> mockApplications = Arrays.asList(new Application(), new Application());
//        when(applicationService.getApplicationsForJob(jobId)).thenReturn(mockApplications);
//
//        // Act
//        ResponseEntity<List<Application>> response = applicationController.getApplicationsForJob(jobId);
//
//        // Assert
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(2, response.getBody().size());
//    }
//
//    @Test
//    void getApplicationsForJob_NoApplications() {
//        // Arrange
//        Long jobId = 1L;
//        when(applicationService.getApplicationsForJob(jobId)).thenReturn(Collections.emptyList());
//
//        // Act
//        ResponseEntity<List<Application>> response = applicationController.getApplicationsForJob(jobId);
//
//        // Assert
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertTrue(response.getBody().isEmpty());
//    }
//
//    @Test
//    void updateStatus_Success() {
//        // Arrange
//        Long applicationId = 1L;
//        ApplicationStatus status = ApplicationStatus.ACCEPTED;
//        Application mockApplication = new Application();
//        when(applicationService.updateApplicationStatus(applicationId, status)).thenReturn(mockApplication);
//
//        // Act
//        ResponseEntity<Application> response = applicationController.updateStatus(applicationId, status);
//
//        // Assert
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        verify(applicationService).updateApplicationStatus(applicationId, status);
//    }
//
//    @Test
//    void updateStatus_InvalidStatus() {
//        // Arrange
//        Long applicationId = 1L;
//        ApplicationStatus status = ApplicationStatus.ACCEPTED;
//        when(applicationService.updateApplicationStatus(applicationId, status))
//                .thenThrow(new RuntimeException("Invalid status"));
//
//        // Act & Assert
//        assertThrows(RuntimeException.class, () ->
//                applicationController.updateStatus(applicationId, status));
//    }
//}
