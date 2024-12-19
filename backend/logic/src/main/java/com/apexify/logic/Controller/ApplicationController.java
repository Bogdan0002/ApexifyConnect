package com.apexify.logic.Controller;

import com.apexify.logic.Service.ApplicationService;
import com.apexify.logic.Service.JobPostService;
import com.apexifyconnect.Model.Application;
import com.apexifyconnect.Model.ApplicationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {
    @Autowired
    private ApplicationService applicationService;

    @PostMapping("/apply")
    public ResponseEntity<ApplicationResponseDTO> apply(@RequestBody ApplicationRequestDTO request) {
        return ResponseEntity.ok(applicationService.createApplication(request));
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<ApplicationResponseDTO>> getApplicationsForJob(@PathVariable Long jobId) {
        return ResponseEntity.ok(applicationService.getApplicationsByJobId(jobId));
    }

    @GetMapping("/creator")
    public ResponseEntity<List<ApplicationResponseDTO>> getCreatorApplications() {
        return ResponseEntity.ok(applicationService.getCurrentCreatorApplications());
    }
}
