package com.apexify.logic.Controller;

import com.apexify.logic.DTO.*;
import com.apexify.logic.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling user-related requests.
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
public class UserController {

    private final UserService userService;

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
    public ResponseEntity<UserResponseDTO> loginContentCreator(@RequestBody LoginRequestDTO requestDTO) {
        UserResponseDTO response = userService.loginContentCreator(requestDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Logs in a company.
     *
     * @param requestDTO the login request DTO
     * @return the response entity containing the user response DTO
     */
    @PostMapping("/login/company")
    public ResponseEntity<UserResponseDTO> loginCompany(@RequestBody LoginRequestDTO requestDTO) {
        UserResponseDTO response = userService.loginCompany(requestDTO);
        return ResponseEntity.ok(response);
    }
}