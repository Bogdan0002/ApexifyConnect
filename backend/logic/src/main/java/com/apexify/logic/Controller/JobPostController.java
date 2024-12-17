package com.apexify.logic.Controller;

import com.apexify.logic.DTO.JobPostRequestDTO;
import com.apexify.logic.Service.UserService;
import com.apexifyconnect.Model.Application;
import com.apexifyconnect.Model.Company;
import com.apexifyconnect.Model.JobPost;
import com.apexify.logic.Service.JobPostService;
import com.apexify.logic.Service.ApplicationService;
import com.apexifyconnect.Model.JobStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/job-posts")
@CrossOrigin(origins = "http://localhost:5173")
public class JobPostController {

    private final JobPostService jobPostService;
    private final ApplicationService applicationService;
    private final UserService userService;

    public JobPostController(JobPostService jobPostService, ApplicationService applicationService, UserService userService) {
        this.jobPostService = jobPostService;
        this.applicationService = applicationService;
        this.userService = userService;
    }

    // Endpoint for companies to create job posts
    @PostMapping("/create")
    public ResponseEntity<JobPost> createJobPost(@RequestBody JobPostRequestDTO jobPostRequestDTO,
                                                 @RequestHeader("Authorization") String token) {
        // Use the token to fetch the company
        Company company = userService.getCompanyByToken(token);

        // Create a new JobPost and populate its fields
        JobPost jobPost = new JobPost();
        jobPost.setTitle(jobPostRequestDTO.getTitle());
        jobPost.setDescription(jobPostRequestDTO.getDescription());
        jobPost.setBudget(jobPostRequestDTO.getBudget());
        jobPost.setContentType(jobPostRequestDTO.getContentType());
        jobPost.setCreatedAt(LocalDateTime.now());

        // Parse and set the deadline if provided
        if (jobPostRequestDTO.getDeadline() != null) {
            try {
                String deadlineWithTime = jobPostRequestDTO.getDeadline().contains("T")
                        ? jobPostRequestDTO.getDeadline()
                        : jobPostRequestDTO.getDeadline() + "T00:00:00";
                jobPost.setDeadline(LocalDateTime.parse(deadlineWithTime));
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid date format for deadline. Use ISO format: yyyy-MM-dd or yyyy-MM-ddTHH:mm:ss");
            }
        }

        jobPost.setStatus(JobStatus.OPEN); // Default to OPEN status

        // Use the service method to create the job post
        JobPost createdJobPost = jobPostService.createJobPost(jobPost, company, jobPostRequestDTO.getTagNames());

        return ResponseEntity.ok(createdJobPost);
    }




    // Endpoint for content creators to view all job posts
    @GetMapping("/all")
    public ResponseEntity<List<JobPost>> getAllJobPosts() {
        return ResponseEntity.ok(jobPostService.getAllJobPosts());
    }

    // Endpoint for companies to view their own job posts
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<JobPost>> getJobPostsByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(jobPostService.getJobPostsByCompany(companyId));
    }

    @PostMapping("/apply")
    public ResponseEntity<Application> applyToJobPost(@RequestParam Long jobPostId, @RequestParam Long creatorId) {
        Application application = applicationService.createApplication(jobPostId, creatorId);
        return ResponseEntity.ok(application);
    }

    @GetMapping("/job/{jobPostId}")
    public ResponseEntity<List<Application>> getApplicationsForJobPost(@PathVariable Long jobPostId) {
        List<Application> applications = applicationService.getApplicationsForJob(jobPostId);
        return ResponseEntity.ok(applications);
    }


}
