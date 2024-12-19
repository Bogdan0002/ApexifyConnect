package com.apexify.logic.unit;

import com.apexify.logic.DTO.*;
import com.apexify.logic.Service.UserService;
import com.apexify.logic.util.JwtUtil;
import com.apexifyconnect.DAO.interfaces.UserDAO;
import com.apexifyconnect.Model.Company;
import com.apexifyconnect.Model.ContentCreator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
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
    private JwtUtil jwtUtil;

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

    @Test
    public void testGetProfilePicture_Success() {
        ContentCreator creator = new ContentCreator();
        creator.setProfilePicture("profile.jpg");
        when(userDAO.findByEmail("test@example.com")).thenReturn(Optional.of(creator));

        String result = userService.getProfilePicture("test@example.com");

        assertEquals("http://localhost:8080/uploads/profile.jpg", result);
    }

    @Test
    public void testGetProfilePicture_UserNotFound() {
        when(userDAO.findByEmail("test@example.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.getProfilePicture("test@example.com");
        });

        assertEquals("User not found or not a content creator", exception.getMessage());
    }

    @Test
    public void testUpdateProfilePicture_Success() {
        ContentCreator creator = new ContentCreator();
        when(userDAO.findByEmail("test@example.com")).thenReturn(Optional.of(creator));
        when(userDAO.save(any(ContentCreator.class))).thenReturn(creator);

        userService.updateProfilePicture("test@example.com", "newpicture.jpg");

        verify(userDAO).save(any(ContentCreator.class));
    }

    @Test
    public void testGetUserProfile_Success() {
        String token = "valid-token";
        String secretKey = "yourTestSecretKey123";
        Claims claims = mock(Claims.class);

        when(claims.getSubject()).thenReturn("test@example.com");
        when(jwtUtil.getSecretKey()).thenReturn(Arrays.toString(secretKey.getBytes()));

        ContentCreator creator = new ContentCreator();
        creator.setEmail("test@example.com");
        when(userDAO.findByEmail("test@example.com")).thenReturn(Optional.of(creator));

        try (MockedStatic<Jwts> jwts = Mockito.mockStatic(Jwts.class)) {
            JwtParser parser = mock(JwtParser.class);
            Jws<Claims> jws = mock(Jws.class);

            jwts.when(Jwts::parser).thenReturn(parser);
            when(parser.setSigningKey(any(String.class))).thenReturn(parser);
            when(parser.parseClaimsJws(token)).thenReturn(jws);
            when(jws.getBody()).thenReturn(claims);

            UserResponseDTO response = userService.getUserProfile(token);
            assertEquals("test@example.com", response.getEmail());
        }
    }

    @Test
    public void testGetCreatorProfile_Success() {
        String token = "Bearer valid-token";
        String secretKey = "yourTestSecretKey123";
        Claims claims = mock(Claims.class);

        when(claims.getSubject()).thenReturn("test@example.com");
        when(jwtUtil.getSecretKey()).thenReturn(Arrays.toString(secretKey.getBytes()));

        ContentCreator creator = new ContentCreator();
        creator.setEmail("test@example.com");
        creator.setFirstName("John");
        creator.setLastName("Doe");
        when(userDAO.findByEmail("test@example.com")).thenReturn(Optional.of(creator));

        try (MockedStatic<Jwts> jwts = Mockito.mockStatic(Jwts.class)) {
            JwtParser parser = mock(JwtParser.class);
            Jws<Claims> jws = mock(Jws.class);

            jwts.when(Jwts::parser).thenReturn(parser);
            when(parser.setSigningKey(any(String.class))).thenReturn(parser);
            when(parser.parseClaimsJws("valid-token")).thenReturn(jws);
            when(jws.getBody()).thenReturn(claims);

            ContentCreatorResponseDTO response = userService.getCreatorProfile(token);
            assertEquals("test@example.com", response.getEmail());
            assertEquals("John", response.getFirstName());
            assertEquals("Doe", response.getLastName());
        }
    }

    @Test
    public void testGetCompanyByToken_Success() {
        String token = "Bearer valid-token";
        String secretKey = "yourTestSecretKey123";
        Claims claims = mock(Claims.class);

        when(claims.getSubject()).thenReturn("company@example.com");
        when(jwtUtil.getSecretKey()).thenReturn(Arrays.toString(secretKey.getBytes()));

        Company company = new Company();
        company.setEmail("company@example.com");
        when(userDAO.findByEmail("company@example.com")).thenReturn(Optional.of(company));

        try (MockedStatic<Jwts> jwts = Mockito.mockStatic(Jwts.class)) {
            JwtParser parser = mock(JwtParser.class);
            Jws<Claims> jws = mock(Jws.class);

            jwts.when(Jwts::parser).thenReturn(parser);
            when(parser.setSigningKey(any(String.class))).thenReturn(parser);
            when(parser.parseClaimsJws("valid-token")).thenReturn(jws);
            when(jws.getBody()).thenReturn(claims);

            Company result = userService.getCompanyByToken(token);
            assertEquals("company@example.com", result.getEmail());
        }
    }



}