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

@Service
public class JobPostService {

    private final JobPostDAO jobPostDAO;
    private final TagService tagService; // Inject TagService here

    public JobPostService(JobPostDAO jobPostDAO, TagService tagService) {
        this.jobPostDAO = jobPostDAO;
        this.tagService = tagService; // Ensure TagService is provided by Spring
    }

    public JobPost createJobPost(JobPost jobPost, Company company, List<String> tagNames) {
        // Associate the company
        jobPost.setCompany(company);
        jobPost.setCreatedAt(LocalDateTime.now());

        // Set default values for missing fields
        if (jobPost.getContentType() == null) {
            jobPost.setContentType("General"); // Default content type
        }

        if (jobPost.getDeadline() == null) {
            jobPost.setDeadline(LocalDateTime.now().plusWeeks(2)); // Default to 2 weeks from now
        }

        if (jobPost.getStatus() == null) {
            jobPost.setStatus(JobStatus.OPEN); // Default status
        }

        if (jobPost.getBudget() != null && jobPost.getBudget() < 0) {
            throw new IllegalArgumentException("Budget cannot be negative");
        }

        if (jobPost.getDeadline() != null && jobPost.getDeadline().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Deadline must be in the future");
        }



        // Fetch tags by name or use empty list if tagNames is null
        List<Tag> tags = (tagNames != null) ? tagNames.stream()
                .map(tagService::findOrCreateTag) // Find or create each tag
                .collect(Collectors.toList())
                : Collections.emptyList();

        jobPost.setTags(tags);

        // Save the job post
        return jobPostDAO.save(jobPost);
    }

    public List<JobPost> getJobPostsByCompany(Long companyId) {
        return jobPostDAO.findByCompanyId(companyId);
    }

    public List<JobPost> getAllJobPosts() {
        return jobPostDAO.findAll();
    }
}
