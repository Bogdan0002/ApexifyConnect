package com.apexify.logic.Controller;

import com.apexify.logic.Service.ApplicationService;
import com.apexify.logic.Service.JobPostService;
import com.apexifyconnect.Model.Application;
import com.apexifyconnect.Model.ApplicationStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping("/job/{jobPostId}")
    public ResponseEntity<List<Application>> getApplicationsForJobPost(@PathVariable Long jobPostId) {
        List<Application> applications = applicationService.getApplicationsForJob(jobPostId);
        return ResponseEntity.ok(applications);
    }
    @PostMapping("/apply")
    public ResponseEntity<Application> applyToJobPost(@RequestParam Long jobPostId, @RequestParam Long creatorId) {
        Application application = applicationService.createApplication(jobPostId, creatorId);
        return ResponseEntity.ok(application);
    }
    @PatchMapping("/{applicationId}/status")
    public ResponseEntity<Application> updateApplicationStatus(@PathVariable Long applicationId, @RequestParam ApplicationStatus status) {
        Application updatedApplication = applicationService.updateApplicationStatus(applicationId, status);
        return ResponseEntity.ok(updatedApplication);
    }

}
