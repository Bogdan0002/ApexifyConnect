package com.apexify.logic.Controller;

import com.apexify.logic.DTO.*;
import com.apexify.logic.Service.UserService;
import com.apexify.logic.util.JwtUtil;
import com.apexifyconnect.Model.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling user-related requests.
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
public class UserController {

    private final UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Constructs a UserController with the specified UserService.
     *
     * @param userService the user service
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers a new content creator.
     *
     * @param requestDTO the content creator request DTO
     * @return the response entity containing the user response DTO
     */
    @PostMapping("/register/content-creator")
    public ResponseEntity<UserResponseDTO> registerContentCreator(@RequestBody ContentCreatorRequestDTO requestDTO) {
        if (requestDTO == null) {
            throw new NullPointerException("ContentCreatorRequestDTO is null");
        }
        UserResponseDTO createdContentCreator = userService.registerContentCreator(requestDTO);
        return ResponseEntity.ok(createdContentCreator);
    }

    /**
     * Registers a new company.
     *
     * @param requestDTO the company request DTO
     * @return the response entity containing the user response DTO
     */
    @PostMapping("/register/company")
    public ResponseEntity<UserResponseDTO> registerCompany(@RequestBody CompanyRequestDTO requestDTO) {
        UserResponseDTO createdCompany = userService.registerCompany(requestDTO);
        return ResponseEntity.ok(createdCompany);
    }

    /**
     * Logs in a content creator.
     *
     * @param requestDTO the login request DTO
     * @return the response entity containing the user response DTO
     */
    @PostMapping("/login/content-creator")
    public ResponseEntity<?> loginContentCreator(@RequestBody LoginRequestDTO requestDTO) {
        try {
            UserResponseDTO response = userService.loginContentCreator(requestDTO);
            String token = jwtUtil.generateToken(response.getEmail());
            return ResponseEntity.ok(new JwtResponseDTO(token, response));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PutMapping("/profile/company/jobs/{jobPostId}/complete")
    public ResponseEntity<JobPostResponseDTO> completeJob(@PathVariable Long jobPostId) {
        JobPostResponseDTO response = userService.completeJob(jobPostId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile/company")
    public ResponseEntity<CompanyResponseDTO> getCompanyProfile(@RequestHeader("Authorization") String token) {
        CompanyResponseDTO response = userService.getCompanyProfile(token);
        return ResponseEntity.ok(response);
    }


    /**
     * Logs in a company.
     *
     * @param requestDTO the login request DTO
     * @return the response entity containing the user response DTO
     */
    @PostMapping("/login/company")
    public ResponseEntity<?> loginCompany(@RequestBody LoginRequestDTO requestDTO) {
        try {
            UserResponseDTO response = userService.loginCompany(requestDTO);
            String token = jwtUtil.generateToken(response.getEmail());
            return ResponseEntity.ok(new JwtResponseDTO(token, response));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }


    @GetMapping("/profile-picture")
    public ResponseEntity<?> getProfilePicture(@RequestParam String email) {
        try {
            String profilePictureUrl = userService.getProfilePicture(email);
            return ResponseEntity.ok(profilePictureUrl);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/profile-picture")
    public ResponseEntity<?> updateProfilePicture(@RequestParam String email, @RequestParam String profilePictureUrl) {
        try {
            userService.updateProfilePicture(email, profilePictureUrl);
            return ResponseEntity.ok("Profile picture updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponseDTO> getUserProfile(@RequestHeader("Authorization") String token) {
        UserResponseDTO response = userService.getUserProfile(token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile/creator")
    public ResponseEntity<ContentCreatorResponseDTO> getCreatorProfile(@RequestHeader("Authorization") String token) {
        ContentCreatorResponseDTO response = userService.getCreatorProfile(token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile/company/jobs")
    public ResponseEntity<List<JobPostResponseDTO>> getCompanyJobPosts(@RequestHeader("Authorization") String token) {
        List<JobPostResponseDTO> jobPosts = userService.getCompanyJobPosts(token);
        return ResponseEntity.ok(jobPosts);
    }

    @GetMapping("/profile/company/collaborations")
    public ResponseEntity<List<JobPostResponseDTO>> getCompanyCollaborations(@RequestHeader("Authorization") String token) {
        List<JobPostResponseDTO> collaborations = userService.getCompanyCollaborations(token);
        return ResponseEntity.ok(collaborations);
    }

    @GetMapping("/profile/company/projects")
    public ResponseEntity<List<JobPostResponseDTO>> getCompanyProjects(@RequestHeader("Authorization") String token) {
        List<JobPostResponseDTO> projects = userService.getCompanyProjects(token);
        return ResponseEntity.ok(projects);
    }


}