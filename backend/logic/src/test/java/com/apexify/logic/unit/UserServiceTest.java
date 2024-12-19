package com.apexify.logic.unit;

import com.apexify.logic.DTO.*;
import com.apexify.logic.Service.UserService;
import com.apexifyconnect.DAO.interfaces.UserDAO;
import com.apexifyconnect.Model.Company;
import com.apexifyconnect.Model.ContentCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserDAO userDAO;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterContentCreator_EmailAlreadyInUse() {
        ContentCreatorRequestDTO requestDTO = new ContentCreatorRequestDTO("test@example.com", "password", "ProfilePicture", "Bio", "Bogdan", "Pavliuc");
        when(userDAO.findByEmail(requestDTO.getEmail())).thenReturn(Optional.of(new ContentCreator()));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.registerContentCreator(requestDTO);
        });

        assertEquals("Email already in use", exception.getMessage());
    }

    @Test
    public void testRegisterCompany_EmailAlreadyInUse() {
        CompanyRequestDTO requestDTO = new CompanyRequestDTO("test@example.com", "password", "CompanyName", "BusinessLicense");
        when(userDAO.findByEmail(requestDTO.getEmail())).thenReturn(Optional.of(new Company()));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.registerCompany(requestDTO);
        });

        assertEquals("Email already in use", exception.getMessage());
    }

    @Test
    public void testLoginCompany_InvalidEmailOrPassword() {
        LoginRequestDTO requestDTO = new LoginRequestDTO("test@example.com", "password");
        when(userDAO.findByEmail(requestDTO.getEmail())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.loginCompany(requestDTO);
        });

        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    public void testLoginContentCreator_InvalidEmailOrPassword() {
        LoginRequestDTO requestDTO = new LoginRequestDTO("test@example.com", "password");
        when(userDAO.findByEmail(requestDTO.getEmail())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.loginContentCreator(requestDTO);
        });

        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    public void testRegisterContentCreator() {
        ContentCreatorRequestDTO requestDTO = new ContentCreatorRequestDTO("test@example.com", "password", "ProfilePicture", "Bio", "Bogdan", "Pavliuc");
        ContentCreator savedCreator = new ContentCreator();
        savedCreator.setEmail(requestDTO.getEmail());
        when(userDAO.save(any(ContentCreator.class))).thenReturn(savedCreator);
        when(passwordEncoder.encode(requestDTO.getPassword())).thenReturn("encodedPassword");

        UserResponseDTO response = userService.registerContentCreator(requestDTO);

        assertEquals("test@example.com", response.getEmail());
        assertEquals("Content Creator", response.getRole());
    }

    @Test
    public void testRegisterCompany() {
        CompanyRequestDTO requestDTO = new CompanyRequestDTO("test@example.com", "password", "CompanyName", "BusinessLicense");
        Company savedCompany = new Company();
        savedCompany.setEmail(requestDTO.getEmail());
        when(userDAO.save(any(Company.class))).thenReturn(savedCompany);
        when(passwordEncoder.encode(requestDTO.getPassword())).thenReturn("encodedPassword");

        UserResponseDTO response = userService.registerCompany(requestDTO);

        assertEquals("test@example.com", response.getEmail());
        assertEquals("Company", response.getRole());
    }
}