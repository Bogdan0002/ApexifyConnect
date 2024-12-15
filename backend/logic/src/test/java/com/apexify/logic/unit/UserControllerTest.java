package com.apexify.logic.unit;


import com.apexify.logic.Controller.UserController;
import com.apexify.logic.DTO.*;
import com.apexify.logic.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterContentCreator() {
        ContentCreatorRequestDTO requestDTO = new ContentCreatorRequestDTO();
        UserResponseDTO responseDTO = new UserResponseDTO("test@example.com", "Content Creator");

        when(userService.registerContentCreator(requestDTO)).thenReturn(responseDTO);

        ResponseEntity<UserResponseDTO> response = userController.registerContentCreator(requestDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    public void testRegisterCompany() {
        CompanyRequestDTO requestDTO = new CompanyRequestDTO();
        UserResponseDTO responseDTO = new UserResponseDTO("test@example.com", "Company");

        when(userService.registerCompany(requestDTO)).thenReturn(responseDTO);

        ResponseEntity<UserResponseDTO> response = userController.registerCompany(requestDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    public void testLoginContentCreator() {
        LoginRequestDTO requestDTO = new LoginRequestDTO();
        UserResponseDTO responseDTO = new UserResponseDTO("test@example.com", "Content Creator");

        when(userService.loginContentCreator(requestDTO)).thenReturn(responseDTO);

        ResponseEntity<UserResponseDTO> response = userController.loginContentCreator(requestDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    public void testLoginCompany() {
        LoginRequestDTO requestDTO = new LoginRequestDTO();
        UserResponseDTO responseDTO = new UserResponseDTO("test@example.com", "Company");

        when(userService.loginCompany(requestDTO)).thenReturn(responseDTO);

        ResponseEntity<UserResponseDTO> response = userController.loginCompany(requestDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    public void testRegisterContentCreator_NullInput() {
        ContentCreatorRequestDTO requestDTO = null;

        assertThrows(NullPointerException.class, () -> {
            userController.registerContentCreator(requestDTO);
        });
    }

    @Test
    public void testRegisterContentCreator_InvalidData() {
        ContentCreatorRequestDTO requestDTO = new ContentCreatorRequestDTO();
        // Set invalid data in requestDTO

        when(userService.registerContentCreator(requestDTO)).thenThrow(new IllegalArgumentException("Invalid data"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userController.registerContentCreator(requestDTO);
        });

        assertEquals("Invalid data", exception.getMessage());
    }

}