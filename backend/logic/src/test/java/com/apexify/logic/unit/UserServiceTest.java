package com.apexify.logic.unit;

import com.apexify.logic.DTO.*;
import com.apexify.logic.Service.UserService;
import com.apexifyconnect.Model.Company;
import com.apexifyconnect.Model.ContentCreator;
import com.apexifyconnect.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterContentCreator() {
        ContentCreatorRequestDTO requestDTO = new ContentCreatorRequestDTO();
        requestDTO.setEmail("test@example.com");
        requestDTO.setPassword("password");
        requestDTO.setProfilePicture("profile.jpg");
        requestDTO.setBio("Bio");

        when(userRepository.findByEmail(requestDTO.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(requestDTO.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(ContentCreator.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponseDTO response = userService.registerContentCreator(requestDTO);

        assertNotNull(response);
        assertEquals("test@example.com", response.getEmail());
        assertEquals("Content Creator", response.getRole());
        verify(userRepository, times(1)).save(any(ContentCreator.class));
    }

    @Test
    public void testRegisterCompany() {
        CompanyRequestDTO requestDTO = new CompanyRequestDTO();
        requestDTO.setEmail("test@example.com");
        requestDTO.setPassword("password");
        requestDTO.setCompanyName("Company");
        requestDTO.setBusinessLicense("License");

        when(userRepository.findByEmail(requestDTO.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(requestDTO.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(Company.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponseDTO response = userService.registerCompany(requestDTO);

        assertNotNull(response);
        assertEquals("test@example.com", response.getEmail());
        assertEquals("Company", response.getRole());
        verify(userRepository, times(1)).save(any(Company.class));
    }

    @Test
    public void testLoginContentCreator() {
        LoginRequestDTO requestDTO = new LoginRequestDTO();
        requestDTO.setEmail("test@example.com");
        requestDTO.setPassword("password");

        ContentCreator contentCreator = new ContentCreator();
        contentCreator.setEmail("test@example.com");
        contentCreator.setPassword("encodedPassword");

        when(userRepository.findByEmail(requestDTO.getEmail())).thenReturn(Optional.of(contentCreator));
        when(passwordEncoder.matches(requestDTO.getPassword(), contentCreator.getPassword())).thenReturn(true);

        UserResponseDTO response = userService.loginContentCreator(requestDTO);

        assertNotNull(response);
        assertEquals("test@example.com", response.getEmail());
        assertEquals("Content Creator", response.getRole());
    }

    @Test
    public void testLoginCompany() {
        LoginRequestDTO requestDTO = new LoginRequestDTO();
        requestDTO.setEmail("test@example.com");
        requestDTO.setPassword("password");

        Company company = new Company();
        company.setEmail("test@example.com");
        company.setPassword("encodedPassword");

        when(userRepository.findByEmail(requestDTO.getEmail())).thenReturn(Optional.of(company));
        when(passwordEncoder.matches(requestDTO.getPassword(), company.getPassword())).thenReturn(true);

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

        when(userRepository.findByEmail(requestDTO.getEmail())).thenReturn(Optional.of(new ContentCreator()));

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

        when(userRepository.findByEmail(requestDTO.getEmail())).thenReturn(Optional.of(new Company()));

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

        when(userRepository.findByEmail(requestDTO.getEmail())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.loginContentCreator(requestDTO);
        });

        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    public void testLoginCompany_InvalidPassword() {
        LoginRequestDTO requestDTO = new LoginRequestDTO();
        requestDTO.setEmail("test@example.com");
        requestDTO.setPassword("wrongpassword");

        Company company = new Company();
        company.setEmail("test@example.com");
        company.setPassword("encodedPassword");

        when(userRepository.findByEmail(requestDTO.getEmail())).thenReturn(Optional.of(company));
        when(passwordEncoder.matches(requestDTO.getPassword(), company.getPassword())).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.loginCompany(requestDTO);
        });

        assertEquals("Invalid email or password", exception.getMessage());
    }

}