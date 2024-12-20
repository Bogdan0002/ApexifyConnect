package com.apexify.logic.Service;

import com.apexify.logic.DTO.ApplicationRequestDTO;
import com.apexify.logic.DTO.ApplicationResponseDTO;
import com.apexify.logic.DTO.JobPostResponseDTO;
import com.apexifyconnect.DAO.interfaces.ApplicationDAO;
import com.apexifyconnect.DAO.interfaces.JobPostDAO;
import com.apexifyconnect.DAO.interfaces.UserDAO;
import com.apexifyconnect.Model.*;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationDAO applicationDAO;
    private final JobPostDAO jobPostDAO;
    private final UserDAO userDAO;

    public ApplicationService(ApplicationDAO applicationDAO, JobPostDAO jobPostDAO, UserDAO userDAO) {
        this.applicationDAO = applicationDAO;
        this.jobPostDAO = jobPostDAO;
        this.userDAO = userDAO;
    }

    public Application createApplication(Long jobPostId, Long creatorId, String coverLetter) {
        // Check if application already exists for this job
        if (applicationDAO.existsByJobPostIdAndContentCreatorId(jobPostId, creatorId)) {
            throw new RuntimeException("You have already applied to this job");
        }
        JobPost jobPost = jobPostDAO.findById(jobPostId)
                .orElseThrow(() -> new RuntimeException("Job post not found"));
        ContentCreator creator = (ContentCreator) userDAO.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("Creator not found"));

        System.out.println("Creator data at application creation:");
        System.out.println("ID: " + creator.getId());
        System.out.println("First Name: " + creator.getFirstName());
        System.out.println("Last Name: " + creator.getLastName());

        Application application = new Application();
        application.setJobPost(jobPost);
        application.setContentCreator(creator);
        application.setCoverLetter(coverLetter);
        application.setStatus(ApplicationStatus.APPLIED);
        application.setAppliedAt(LocalDateTime.now());

        return applicationDAO.save(application);
    }



    public List<Application> getApplicationsForJob(Long jobPostId) {
        return applicationDAO.findByJobPostId(jobPostId);
    }

    public List<ApplicationResponseDTO> getApplicationsForCreator(Long creatorId) {
        ContentCreator creator = (ContentCreator) userDAO.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("Creator not found"));

        List<Application> applications = applicationDAO.findByContentCreatorId(creatorId);

        return applications.stream()
                .map(app -> new ApplicationResponseDTO(
                        app.getId(),
                        app.getJobPost().getId(),
                        app.getJobPost().getTitle(), // This ensures job title is included
                        creator.getFirstName() + " " + creator.getLastName(),
                        app.getCoverLetter(),
                        app.getStatus().toString(),
                        app.getAppliedAt(),
                        app.getUpdatedAt()
                ))
                .collect(Collectors.toList());
    }


    public List<ApplicationResponseDTO> getApplicationsForJobPost(Long jobPostId) {
        List<Application> applications = applicationDAO.findByJobPostId(jobPostId);

        return applications.stream()
                .map(app -> {
                    System.out.println("Creator: " + app.getContentCreator().getFirstName());
                    return new ApplicationResponseDTO(
                            app.getId(),
                            app.getJobPost().getId(),
                            app.getJobPost().getTitle(),
                            app.getContentCreator().getFirstName() + " " + app.getContentCreator().getLastName(),
                            app.getCoverLetter(),
                            app.getStatus().toString(),
                            app.getAppliedAt(),
                            app.getUpdatedAt()
                    );
                })
                .collect(Collectors.toList());
    }



    public ApplicationResponseDTO updateApplicationStatus(Long applicationId, ApplicationStatus status) {
        Application application = applicationDAO.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        application.setStatus(status);
        application.setUpdatedAt(LocalDateTime.now());

        if (status == ApplicationStatus.ACCEPTED) {
            JobPost jobPost = application.getJobPost();
            jobPost.setStatus(JobStatus.CLOSED);
            jobPostDAO.save(jobPost);
        }

        Application updatedApplication = applicationDAO.save(application);

        return new ApplicationResponseDTO(
                updatedApplication.getId(),
                updatedApplication.getJobPost().getId(),
                updatedApplication.getJobPost().getTitle(),
                updatedApplication.getContentCreator().getFirstName() + " " + updatedApplication.getContentCreator().getLastName(),
                updatedApplication.getCoverLetter(),
                updatedApplication.getStatus().toString(),
                updatedApplication.getAppliedAt(),
                updatedApplication.getUpdatedAt()
        );
    }

    public JobPostResponseDTO completeJob(Long jobPostId) {
        JobPost jobPost = jobPostDAO.findById(jobPostId)
                .orElseThrow(() -> new RuntimeException("Job post not found"));

        jobPost.setStatus(JobStatus.COMPLETED);
        JobPost updatedJobPost = jobPostDAO.save(jobPost);

        return new JobPostResponseDTO(
                updatedJobPost.getId(),
                updatedJobPost.getTitle(),
                updatedJobPost.getDescription(),
                updatedJobPost.getBudget(),
                updatedJobPost.getCompany().getCompanyName(),
                updatedJobPost.getCreatedAt(),
                updatedJobPost.getDeadline()
        );
    }


}
