package com.apexify.logic.unit;

import com.apexify.logic.Controller.UserController;
import com.apexify.logic.DTO.*;
import com.apexify.logic.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    public void testRegisterContentCreator() {
//        ContentCreatorRequestDTO requestDTO = new ContentCreatorRequestDTO("test@example.com", "password", "ProfilePicture", "Bio", "Bogdan", "Pavliuc");
//        UserResponseDTO responseDTO = new UserResponseDTO("test@example.com", "Content Creator");
//        when(userService.registerContentCreator(requestDTO)).thenReturn(responseDTO);
//
//        ResponseEntity<UserResponseDTO> response = userController.registerContentCreator(requestDTO);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(responseDTO, response.getBody());
//    }

//    @Test
//    public void testRegisterCompany() {
//        CompanyRequestDTO requestDTO = new CompanyRequestDTO("test@example.com", "password", "CompanyName", "BusinessLicense");
//        UserResponseDTO responseDTO = new UserResponseDTO("test@example.com", "Company");
//        when(userService.registerCompany(requestDTO)).thenReturn(responseDTO);
//
//        ResponseEntity<UserResponseDTO> response = userController.registerCompany(requestDTO);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(responseDTO, response.getBody());
//    }

//    @Test
//    public void testLoginCompany() {
//        LoginRequestDTO requestDTO = new LoginRequestDTO("test@example.com", "password");
//        UserResponseDTO responseDTO = new UserResponseDTO("test@example.com", "Company");
//        when(userService.loginCompany(requestDTO)).thenReturn(responseDTO);
//
//        ResponseEntity<UserResponseDTO> response = (ResponseEntity<UserResponseDTO>) userController.loginCompany(requestDTO);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(responseDTO, response.getBody());
//    }
//
//    @Test
//    public void testLoginContentCreator() {
//        LoginRequestDTO requestDTO = new LoginRequestDTO("test@example.com", "password");
//        UserResponseDTO responseDTO = new UserResponseDTO("test@example.com", "Content Creator");
//        when(userService.loginContentCreator(requestDTO)).thenReturn(responseDTO);
//
//        ResponseEntity<UserResponseDTO> response = (ResponseEntity<UserResponseDTO>) userController.loginContentCreator(requestDTO);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(responseDTO, response.getBody());
//    }

    @Test
    public void testRegisterContentCreator_InvalidData() {
        doThrow(new IllegalArgumentException("Invalid data")).when(userService).registerContentCreator(any(ContentCreatorRequestDTO.class));

        assertThrows(IllegalArgumentException.class, () -> {
            userController.registerContentCreator(new ContentCreatorRequestDTO("invalid", "password", "ProfilePicture", "Bio", "Bogdan", "Pavliuc"));
        });
    }
}