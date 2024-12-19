package com.apexify.logic.unit;

import com.apexify.logic.Controller.JobPostController;
import com.apexify.logic.DTO.JobPostRequestDTO;
import com.apexify.logic.Service.ApplicationService;
import com.apexify.logic.Service.JobPostService;
import com.apexify.logic.Service.UserService;
import com.apexifyconnect.Model.Application;
import com.apexifyconnect.Model.Company;
import com.apexifyconnect.Model.JobPost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class JobPostControllerTest {

    @Mock
    private JobPostService jobPostService;

    @Mock
    private ApplicationService applicationService;

    @Mock
    private UserService userService;

    private JobPostController jobPostController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jobPostController = new JobPostController(jobPostService, applicationService, userService);
    }

    @Test
    void createJobPost_Success() {
        // Arrange
        String token = "Bearer valid-token";
        JobPostRequestDTO requestDTO = new JobPostRequestDTO();
        Company company = new Company();
        JobPost jobPost = new JobPost();

        when(userService.getCompanyByToken(token)).thenReturn(company);
        when(jobPostService.createJobPost(any(JobPost.class), eq(company), any())).thenReturn(jobPost);

        // Act
        ResponseEntity<JobPost> response = jobPostController.createJobPost(requestDTO, token);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(userService).getCompanyByToken(token);
        verify(jobPostService).createJobPost(any(JobPost.class), eq(company), any());
    }

    @Test
    void createJobPost_InvalidToken_ThrowsException() {
        // Arrange
        String invalidToken = "Bearer invalid-token";
        JobPostRequestDTO requestDTO = new JobPostRequestDTO();

        when(userService.getCompanyByToken(invalidToken))
                .thenThrow(new RuntimeException("Invalid token"));

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                jobPostController.createJobPost(requestDTO, invalidToken));
    }

    @Test
    void getAllJobPosts_Success() {
        // Arrange
        List<JobPost> jobPosts = Arrays.asList(new JobPost(), new JobPost());
        when(jobPostService.getAllJobPosts()).thenReturn(jobPosts);

        // Act
        ResponseEntity<List<JobPost>> response = jobPostController.getAllJobPosts();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getAllJobPosts_EmptyList() {
        // Arrange
        when(jobPostService.getAllJobPosts()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<JobPost>> response = jobPostController.getAllJobPosts();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void getJobPostsByCompany_Success() {
        // Arrange
        Long companyId = 1L;
        List<JobPost> companyPosts = Arrays.asList(new JobPost(), new JobPost());
        when(jobPostService.getJobPostsByCompany(companyId)).thenReturn(companyPosts);

        // Act
        ResponseEntity<List<JobPost>> response = jobPostController.getJobPostsByCompany(companyId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void applyToJobPost_Success() {
        // Arrange
        Long jobPostId = 1L;
        Long creatorId = 1L;
        Application application = new Application();
        when(applicationService.createApplication(jobPostId, creatorId)).thenReturn(application);

        // Act
        ResponseEntity<Application> response = jobPostController.applyToJobPost(jobPostId, creatorId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getApplicationsForJobPost_Success() {
        // Arrange
        Long jobPostId = 1L;
        List<Application> applications = Arrays.asList(new Application(), new Application());
        when(applicationService.getApplicationsForJob(jobPostId)).thenReturn(applications);

        // Act
        ResponseEntity<List<Application>> response = jobPostController.getApplicationsForJobPost(jobPostId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getApplicationsForJobPost_NoApplications() {
        // Arrange
        Long jobPostId = 1L;
        when(applicationService.getApplicationsForJob(jobPostId)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<Application>> response = jobPostController.getApplicationsForJobPost(jobPostId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }
}
