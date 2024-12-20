package com.apexify.logic.Controller;

import com.apexify.logic.DTO.ApplicationResponseDTO;
import com.apexifyconnect.Model.Application;
import com.apexifyconnect.Model.ApplicationStatus;
import com.apexify.logic.Service.ApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
public class ApplicationController {
    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping("/apply")
    public ResponseEntity<Application> apply(
            @RequestParam Long jobPostId,
            @RequestParam Long creatorId,
            @RequestParam String coverLetter) {
        Application application = applicationService.createApplication(jobPostId, creatorId, coverLetter);
        return ResponseEntity.ok(application);
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<Application>> getApplicationsForJob(@PathVariable Long jobId) {
        List<Application> applications = applicationService.getApplicationsForJob(jobId);
        return ResponseEntity.ok(applications);
    }


    @GetMapping("/creator/{creatorId}/applications")
    public ResponseEntity<List<ApplicationResponseDTO>> getApplicationsForCreator(@PathVariable Long creatorId) {
        List<ApplicationResponseDTO> applications = applicationService.getApplicationsForCreator(creatorId);
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/company/jobpost/{jobPostId}")
    public ResponseEntity<List<ApplicationResponseDTO>> getApplicationsForJobPost(@PathVariable Long jobPostId) {
        List<ApplicationResponseDTO> applications = applicationService.getApplicationsForJobPost(jobPostId);
        return ResponseEntity.ok(applications);
    }

    @PatchMapping("/{applicationId}/status")
    public ResponseEntity<ApplicationResponseDTO> updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestParam ApplicationStatus status) {
        ApplicationResponseDTO updatedApplication = applicationService.updateApplicationStatus(applicationId, status);
        return ResponseEntity.ok(updatedApplication);
    }






}
