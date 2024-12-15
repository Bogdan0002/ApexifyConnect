package com.apexify.logic.integration;

import com.apexify.logic.DTO.*;
import com.apexify.logic.Service.UserService;
import com.apexifyconnect.Model.Company;
import com.apexifyconnect.Model.ContentCreator;
import com.apexifyconnect.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    public void testRegisterContentCreator() {
        ContentCreatorRequestDTO requestDTO = new ContentCreatorRequestDTO();
        requestDTO.setEmail("test@example.com");
        requestDTO.setPassword("password");
        requestDTO.setProfilePicture("profile.jpg");
        requestDTO.setBio("Bio");

        UserResponseDTO response = userService.registerContentCreator(requestDTO);

        assertNotNull(response);
        assertEquals("test@example.com", response.getEmail());
        assertEquals("Content Creator", response.getRole());

        ContentCreator savedCreator = (ContentCreator) userRepository.findByEmail("test@example.com").orElse(null);
        assertNotNull(savedCreator);
        assertTrue(passwordEncoder.matches("password", savedCreator.getPassword()));
    }

    @Test
    public void testRegisterCompany() {
        CompanyRequestDTO requestDTO = new CompanyRequestDTO();
        requestDTO.setEmail("test@example.com");
        requestDTO.setPassword("password");
        requestDTO.setCompanyName("Company");
        requestDTO.setBusinessLicense("License");

        UserResponseDTO response = userService.registerCompany(requestDTO);

        assertNotNull(response);
        assertEquals("test@example.com", response.getEmail());
        assertEquals("Company", response.getRole());

        Company savedCompany = (Company) userRepository.findByEmail("test@example.com").orElse(null);
        assertNotNull(savedCompany);
        assertTrue(passwordEncoder.matches("password", savedCompany.getPassword()));
    }

    @Test
    public void testLoginContentCreator() {
        ContentCreator contentCreator = new ContentCreator();
        contentCreator.setEmail("test@example.com");
        contentCreator.setPassword(passwordEncoder.encode("password"));
        userRepository.save(contentCreator);

        LoginRequestDTO requestDTO = new LoginRequestDTO();
        requestDTO.setEmail("test@example.com");
        requestDTO.setPassword("password");

        UserResponseDTO response = userService.loginContentCreator(requestDTO);

        assertNotNull(response);
        assertEquals("test@example.com", response.getEmail());
        assertEquals("Content Creator", response.getRole());
    }

    @Test
    public void testLoginCompany() {
        Company company = new Company();
        company.setEmail("test@example.com");
        company.setPassword(passwordEncoder.encode("password"));
        userRepository.save(company);

        LoginRequestDTO requestDTO = new LoginRequestDTO();
        requestDTO.setEmail("test@example.com");
        requestDTO.setPassword("password");

        UserResponseDTO response = userService.loginCompany(requestDTO);

        assertNotNull(response);
        assertEquals("test@example.com", response.getEmail());
        assertEquals("Company", response.getRole());
    }

    @Test
    public void testRegisterContentCreator_EmailAlreadyInUse() {
        ContentCreatorRequestDTO requestDTO = new ContentCreatorRequestDTO();
        requestDTO.setEmail("test@example.com");
        requestDTO.setPassword("password");
        requestDTO.setProfilePicture("profile.jpg");
        requestDTO.setBio("Bio");

        userService.registerContentCreator(requestDTO);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.registerContentCreator(requestDTO);
        });

        assertEquals("Email already in use", exception.getMessage());
    }

    @Test
    public void testRegisterCompany_EmailAlreadyInUse() {
        CompanyRequestDTO requestDTO = new CompanyRequestDTO();
        requestDTO.setEmail("test@example.com");
        requestDTO.setPassword("password");
        requestDTO.setCompanyName("Company");
        requestDTO.setBusinessLicense("License");

        userService.registerCompany(requestDTO);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.registerCompany(requestDTO);
        });

        assertEquals("Email already in use", exception.getMessage());
    }

    @Test
    public void testLoginContentCreator_InvalidEmail() {
        LoginRequestDTO requestDTO = new LoginRequestDTO();
        requestDTO.setEmail("invalid@example.com");
        requestDTO.setPassword("password");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.loginContentCreator(requestDTO);
        });

        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    public void testLoginCompany_InvalidPassword() {
        Company company = new Company();
        company.setEmail("test@example.com");
        company.setPassword(passwordEncoder.encode("password"));
        userRepository.save(company);

        LoginRequestDTO requestDTO = new LoginRequestDTO();
        requestDTO.setEmail("test@example.com");
        requestDTO.setPassword("wrongpassword");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.loginCompany(requestDTO);
        });

        assertEquals("Invalid email or password", exception.getMessage());
    }
}