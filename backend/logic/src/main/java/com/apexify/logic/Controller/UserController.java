package com.apexify.logic.Controller;

import com.apexify.logic.DTO.*;
import com.apexify.logic.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register/content-creator")
    public ResponseEntity<UserResponseDTO> registerContentCreator(@RequestBody ContentCreatorRequestDTO requestDTO) {
        UserResponseDTO createdContentCreator = userService.registerContentCreator(requestDTO);
        return ResponseEntity.ok(createdContentCreator);
    }

    @PostMapping("/register/company")
    public ResponseEntity<UserResponseDTO> registerCompany(@RequestBody CompanyRequestDTO requestDTO) {
        UserResponseDTO createdCompany = userService.registerCompany(requestDTO);
        return ResponseEntity.ok(createdCompany);
    }

    @PostMapping("/login/content-creator")
    public ResponseEntity<UserResponseDTO> loginContentCreator(@RequestBody LoginRequestDTO requestDTO) {
        UserResponseDTO response = userService.loginContentCreator(requestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login/company")
    public ResponseEntity<UserResponseDTO> loginCompany(@RequestBody LoginRequestDTO requestDTO) {
        UserResponseDTO response = userService.loginCompany(requestDTO);
        return ResponseEntity.ok(response);
    }

}