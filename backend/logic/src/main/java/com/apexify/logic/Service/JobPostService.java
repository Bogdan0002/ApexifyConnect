package com.apexify.logic.Service;

import com.apexify.logic.DTO.JobPostRequestDTO;
import com.apexifyconnect.DAO.interfaces.JobPostDAO;
import com.apexifyconnect.Model.Company;
import com.apexifyconnect.Model.JobPost;
import com.apexifyconnect.Model.JobStatus;
import com.apexifyconnect.Model.Tag;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class responsible for managing job posts in the ApexifyConnect platform.
 * This service handles the creation, retrieval, and management of job posts,
 * including their associations with companies and tags.
 */
@Service
public class JobPostService {

    private final JobPostDAO jobPostDAO;
    private final TagService tagService;

    /**
     * Constructs a new JobPostService with required dependencies.
     *
     * @param jobPostDAO The data access object for job posts
     * @param tagService The service for managing tags
     */
    public JobPostService(JobPostDAO jobPostDAO, TagService tagService) {
        this.jobPostDAO = jobPostDAO;
        this.tagService = tagService;
    }

    /**
     * Creates a new job post with the specified details and associates it with a company and tags.
     * Sets default values for missing fields and performs validation checks.
     *
     * @param jobPost The job post entity to be created
     * @param company The company associated with the job post
     * @param tagNames List of tag names to be associated with the job post
     * @return The created JobPost entity
     * @throws IllegalArgumentException if the budget is negative or deadline is in the past
     */
    public JobPost createJobPost(JobPost jobPost, Company company, List<String> tagNames) {
        jobPost.setCompany(company);
        jobPost.setCreatedAt(LocalDateTime.now());

        if (jobPost.getContentType() == null) {
            jobPost.setContentType("General");
        }

        if (jobPost.getDeadline() == null) {
            jobPost.setDeadline(LocalDateTime.now().plusWeeks(2));
        }

        if (jobPost.getStatus() == null) {
            jobPost.setStatus(JobStatus.OPEN);
        }

        if (jobPost.getBudget() != null && jobPost.getBudget() < 0) {
            throw new IllegalArgumentException("Budget cannot be negative");
        }

        if (jobPost.getDeadline() != null && jobPost.getDeadline().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Deadline must be in the future");
        }

        List<Tag> tags = (tagNames != null) ? tagNames.stream()
                .map(tagService::findOrCreateTag)
                .collect(Collectors.toList())
                : Collections.emptyList();

        jobPost.setTags(tags);

        return jobPostDAO.save(jobPost);
    }

    /**
     * Retrieves all job posts associated with a specific company.
     *
     * @param companyId The ID of the company
     * @return List of job posts belonging to the specified company
     */
    public List<JobPost> getJobPostsByCompany(Long companyId) {
        return jobPostDAO.findByCompanyId(companyId);
    }

    /**
     * Retrieves all job posts in the system.
     *
     * @return List of all job posts
     */
    public List<JobPost> getAllJobPosts() {
        return jobPostDAO.findAll();
    }
}
