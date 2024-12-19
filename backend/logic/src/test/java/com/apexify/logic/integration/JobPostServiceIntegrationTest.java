package com.apexify.logic.integration;

import com.apexify.logic.DTO.CompanyRequestDTO;
import com.apexify.logic.DTO.JobPostRequestDTO;
import com.apexify.logic.DTO.UserResponseDTO;
import com.apexify.logic.Service.JobPostService;
import com.apexify.logic.Service.UserService;
import com.apexifyconnect.DAO.interfaces.JobPostDAO;
import com.apexifyconnect.DAO.interfaces.UserDAO;
import com.apexifyconnect.Model.Company;
import com.apexifyconnect.Model.JobPost;
import com.apexifyconnect.Model.JobStatus;
import com.apexifyconnect.Repository.ApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class JobPostServiceIntegrationTest {

    @Autowired
    private JobPostService jobPostService;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserService userService;

    @Autowired
    private JobPostDAO jobPostDAO;

    @Autowired
    private ApplicationRepository applicationRepository;

    private Company testCompany;
    private JobPost testJobPost;

    @BeforeEach
    void setUp() {
        // Create test company using registerCompany
        CompanyRequestDTO companyRequestDTO = new CompanyRequestDTO();
        companyRequestDTO.setEmail("test@company.com");
        companyRequestDTO.setPassword("password123");
        companyRequestDTO.setCompanyName("Test Company");
        companyRequestDTO.setBusinessLicense("License123");

        UserResponseDTO response = userService.registerCompany(companyRequestDTO);
        testCompany = (Company) userDAO.findByEmail(response.getEmail())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        // Create test job post
        testJobPost = new JobPost();
        testJobPost.setTitle("Test Job");
        testJobPost.setDescription("Test Description");
        testJobPost.setBudget(1000.0);
        testJobPost.setDeadline(LocalDateTime.now().plusDays(14));
        testJobPost.setCompany(testCompany);
    }


    @Test
    void createJobPost_FullFlow_Success() {
        // Create job post with all valid fields
        JobPost result = jobPostService.createJobPost(testJobPost, testCompany, Arrays.asList("Design", "Video"));

        assertNotNull(result.getId());
        assertEquals("Test Job", result.getTitle());
        assertEquals(2, result.getTags().size());
        assertEquals(JobStatus.OPEN, result.getStatus());
    }

    @Test
    void createJobPost_WithMinimumFields_Success() {
        testJobPost.setContentType(null);
        testJobPost.setDeadline(null);

        JobPost result = jobPostService.createJobPost(testJobPost, testCompany, null);

        assertNotNull(result);
        assertEquals("General", result.getContentType());
        assertTrue(result.getDeadline().isAfter(LocalDateTime.now()));
    }

    @Test
    void getJobPostsByCompany_MultiplePostsExist() {
        // Create multiple job posts
        jobPostService.createJobPost(testJobPost, testCompany, null);

        JobPost secondPost = new JobPost();
        secondPost.setTitle("Second Job");
        secondPost.setDescription("Another Description");
        secondPost.setCompany(testCompany);
        jobPostService.createJobPost(secondPost, testCompany, null);

        List<JobPost> companyPosts = jobPostService.getJobPostsByCompany(testCompany.getId());
        assertEquals(2, companyPosts.size());
    }

    @Test
    void getJobPostsByCompany_NoPostsExist() {
        List<JobPost> companyPosts = jobPostService.getJobPostsByCompany(testCompany.getId());
        assertTrue(companyPosts.isEmpty());
    }

    @Test
    void createJobPost_WithPastDeadline_ThrowsException() {
        testJobPost.setDeadline(LocalDateTime.now().minusDays(1));

        assertThrows(IllegalArgumentException.class, () ->
                jobPostService.createJobPost(testJobPost, testCompany, null)
        );
    }

    @Test
    void createJobPost_WithInvalidBudget_ThrowsException() {
        testJobPost.setBudget(-100.0);

        assertThrows(IllegalArgumentException.class, () ->
                jobPostService.createJobPost(testJobPost, testCompany, null)
        );
    }

    @Test
    void getAllJobPosts_OrderedByCreationDate() {
        testJobPost.setCreatedAt(LocalDateTime.now().minusDays(1));
        JobPost olderPost = jobPostService.createJobPost(testJobPost, testCompany, null);

        JobPost newerPost = new JobPost();
        newerPost.setTitle("Newer Post");
        newerPost.setDescription("New Description");
        newerPost.setCompany(testCompany);
        newerPost = jobPostService.createJobPost(newerPost, testCompany, null);

        List<JobPost> allPosts = jobPostService.getAllJobPosts();
        assertTrue(newerPost.getCreatedAt().isAfter(olderPost.getCreatedAt()));
    }

    @Test
    void createJobPost_WithDuplicateTags_HandlesProperly() {
        List<String> duplicateTags = Arrays.asList("Design", "Design", "Video");
        JobPost result = jobPostService.createJobPost(testJobPost, testCompany, duplicateTags);
        assertEquals(3, result.getTags().size());
    }

}
