package com.apexify.logic.unit;

import com.apexify.logic.DTO.JobPostRequestDTO;
import com.apexify.logic.Service.JobPostService;
import com.apexify.logic.Service.TagService;
import com.apexifyconnect.DAO.interfaces.JobPostDAO;
import com.apexifyconnect.Model.Company;
import com.apexifyconnect.Model.JobPost;
import com.apexifyconnect.Model.JobStatus;
import com.apexifyconnect.Model.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JobPostServiceTest {

    @Mock
    private JobPostDAO jobPostDAO;

    @Mock
    private TagService tagService;

    private JobPostService jobPostService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jobPostService = new JobPostService(jobPostDAO, tagService);
    }

    @Test
    void createJobPost_Success() {
        // Arrange
        JobPost jobPost = new JobPost();
        jobPost.setTitle("Test Job");
        jobPost.setDescription("Test Description");
        jobPost.setBudget(1000.0);
        jobPost.setContentType("Video");
        jobPost.setDeadline(LocalDateTime.now().plusDays(14));

        Company company = new Company();
        Tag designTag = new Tag();
        designTag.setName("Design");
        Tag videoTag = new Tag();
        videoTag.setName("Video");
        List<String> tagNames = Arrays.asList("Design", "Video");

        when(tagService.findOrCreateTag("Design")).thenReturn(designTag);
        when(tagService.findOrCreateTag("Video")).thenReturn(videoTag);
        when(jobPostDAO.save(any(JobPost.class))).thenReturn(jobPost);

        // Act
        JobPost result = jobPostService.createJobPost(jobPost, company, tagNames);

        // Assert
        assertNotNull(result);
        assertEquals(JobStatus.OPEN, result.getStatus());
        verify(jobPostDAO).save(any(JobPost.class));
    }


    @Test
    void getJobPostsByCompany_Success() {
        // Arrange
        Long companyId = 1L;
        List<JobPost> expectedPosts = Arrays.asList(new JobPost(), new JobPost());
        when(jobPostDAO.findByCompanyId(companyId)).thenReturn(expectedPosts);

        // Act
        List<JobPost> result = jobPostService.getJobPostsByCompany(companyId);

        // Assert
        assertEquals(expectedPosts.size(), result.size());
        verify(jobPostDAO).findByCompanyId(companyId);
    }

    @Test
    void getAllJobPosts_Success() {
        // Arrange
        List<JobPost> expectedPosts = Arrays.asList(new JobPost(), new JobPost());
        when(jobPostDAO.findAll()).thenReturn(expectedPosts);

        // Act
        List<JobPost> result = jobPostService.getAllJobPosts();

        // Assert
        assertEquals(expectedPosts.size(), result.size());
        verify(jobPostDAO).findAll();
    }

    @Test
    void createJobPost_WithDefaultValues_Success() {
        // Arrange
        JobPost jobPost = new JobPost();
        Company company = new Company();

        when(jobPostDAO.save(any(JobPost.class))).thenReturn(jobPost);

        // Act
        JobPost result = jobPostService.createJobPost(jobPost, company, null);

        // Assert
        assertNotNull(result);
        assertEquals("General", result.getContentType());
        assertEquals(JobStatus.OPEN, result.getStatus());
        assertNotNull(result.getDeadline());
        assertTrue(result.getTags().isEmpty());
    }
}
