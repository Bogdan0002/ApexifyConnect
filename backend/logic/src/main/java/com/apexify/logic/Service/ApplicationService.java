package com.apexify.logic.Service;

import com.apexify.logic.DTO.ApplicationRequestDTO;
import com.apexify.logic.DTO.ApplicationResponseDTO;
import com.apexify.logic.util.JwtUtil;
import com.apexifyconnect.DAO.interfaces.JobPostDAO;
import com.apexifyconnect.DAO.interfaces.UserDAO;
import com.apexifyconnect.Model.Application;
import com.apexifyconnect.Model.ApplicationStatus;
import com.apexifyconnect.Model.ContentCreator;
import com.apexifyconnect.Model.JobPost;
import com.apexifyconnect.Repository.ApplicationRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final JobPostDAO jobPostDAO;
    private final UserDAO userDAO;

    public ApplicationService(ApplicationRepository applicationRepository, JobPostDAO jobPostDAO, UserDAO userDAO) {
        this.applicationRepository = applicationRepository;
        this.jobPostDAO = jobPostDAO;
        this.userDAO = userDAO;
    }

    public Application createApplication(Long jobPostId, Long creatorId) {
        JobPost jobPost = jobPostDAO.findById(jobPostId)
                .orElseThrow(() -> new RuntimeException("Job post not found"));
        ContentCreator creator = (ContentCreator) userDAO.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("Creator not found"));

        Application application = new Application();
        application.setJobPost(jobPost);
        application.setContentCreator(creator);
        application.setStatus(ApplicationStatus.APPLIED);
        application.setAppliedAt(LocalDateTime.now());

        return applicationRepository.save(application);
    }

    public List<Application> getApplicationsForJob(Long jobPostId) {
        return applicationRepository.findByJobPostId(jobPostId);
    }

    public Application updateApplicationStatus(Long applicationId, ApplicationStatus status) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        application.setStatus(status);
        application.setUpdatedAt(LocalDateTime.now());

        return applicationRepository.save(application);
    }
}
