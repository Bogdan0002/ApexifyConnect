//package com.apexify.logic.unit;
//
//import com.apexify.logic.Service.ApplicationService;
//import com.apexifyconnect.DAO.interfaces.ApplicationDAO;
//import com.apexifyconnect.DAO.interfaces.JobPostDAO;
//import com.apexifyconnect.DAO.interfaces.UserDAO;
//import com.apexifyconnect.Model.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//class ApplicationServiceTest {
//
//    @Mock
//    private ApplicationDAO applicationDAO;
//
//    @Mock
//    private JobPostDAO jobPostDAO;
//
//    @Mock
//    private UserDAO userDAO;
//
//    private ApplicationService applicationService;
//    private JobPost testJobPost;
//    private ContentCreator testCreator;
//    private Application testApplication;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        applicationService = new ApplicationService(applicationDAO, jobPostDAO, userDAO);
//
//        testJobPost = new JobPost();
//        testJobPost.setId(1L);
//
//        testCreator = new ContentCreator();
//        testCreator.setId(1L);
//
//        testApplication = new Application();
//        testApplication.setJobPost(testJobPost);
//        testApplication.setContentCreator(testCreator);
//        testApplication.setStatus(ApplicationStatus.APPLIED);
//        testApplication.setAppliedAt(LocalDateTime.now());
//    }
//
//    @Test
//    void createApplication_Success() {
//        when(jobPostDAO.findById(1L)).thenReturn(Optional.of(testJobPost));
//        when(userDAO.findById(1L)).thenReturn(Optional.of(testCreator));
//        when(applicationDAO.save(any(Application.class))).thenReturn(testApplication);
//
//        Application result = applicationService.createApplication(1L, 1L);
//
//        assertNotNull(result);
//        assertEquals(ApplicationStatus.APPLIED, result.getStatus());
//        verify(applicationDAO).save(any(Application.class));
//    }
//
//    @Test
//    void createApplication_JobPostNotFound() {
//        when(jobPostDAO.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(RuntimeException.class, () ->
//                applicationService.createApplication(1L, 1L)
//        );
//    }
//
//    @Test
//    void createApplication_CreatorNotFound() {
//        when(jobPostDAO.findById(1L)).thenReturn(Optional.of(testJobPost));
//        when(userDAO.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(RuntimeException.class, () ->
//                applicationService.createApplication(1L, 1L)
//        );
//    }
//
//    @Test
//    void getApplicationsForJob_Success() {
//        List<Application> applications = Arrays.asList(testApplication);
//        when(applicationDAO.findByJobPostId(1L)).thenReturn(applications);
//
//        List<Application> result = applicationService.getApplicationsForJob(1L);
//
//        assertEquals(1, result.size());
//        verify(applicationDAO).findByJobPostId(1L);
//    }
//
//    @Test
//    void getApplicationsForJob_NoApplications() {
//        when(applicationDAO.findByJobPostId(1L)).thenReturn(Collections.emptyList());
//
//        List<Application> result = applicationService.getApplicationsForJob(1L);
//
//        assertTrue(result.isEmpty());
//    }
//
//    @Test
//    void updateApplicationStatus_Success() {
//        when(applicationDAO.findById(1L)).thenReturn(Optional.of(testApplication));
//        when(applicationDAO.save(any(Application.class))).thenReturn(testApplication);
//
//        Application result = applicationService.updateApplicationStatus(1L, ApplicationStatus.ACCEPTED);
//
//        assertEquals(ApplicationStatus.ACCEPTED, result.getStatus());
//        assertNotNull(result.getUpdatedAt());
//    }
//
//    @Test
//    void updateApplicationStatus_ApplicationNotFound() {
//        when(applicationDAO.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(RuntimeException.class, () ->
//                applicationService.updateApplicationStatus(1L, ApplicationStatus.ACCEPTED)
//        );
//    }
//}
