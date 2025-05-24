package com.event.user_service.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.event.user_service.dto.Response;
import com.event.user_service.exception.UserNotFoundException;
import com.event.user_service.model.Role;
import com.event.user_service.model.User;
import com.event.user_service.repository.UserRepository;
import com.event.user_service.service.UserServiceImpl;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserById_Success() throws Exception {
        // Arrange
        long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setFullName("John Doe");
        user.setEmail("john@example.com");
        user.setMobile("1234567890");
        user.setRole(Role.USER);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        ResponseEntity<Response> responseEntity = userService.getUserById(String.valueOf(userId));

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Response response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals(userId, response.getId());
        assertEquals("John Doe", response.getFullName());
        assertEquals("john@example.com", response.getEmail());
        assertEquals("1234567890", response.getMobile());
        assertEquals(Role.USER, response.getRole());
    }

    @Test
    void testGetUserById_UserNotFound() {
        // Arrange
        long userId = 2L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(String.valueOf(userId));
        });
        assertEquals("User not found with this id", exception.getMessage());
    }

    @Test
    void testGetUserByEmail_Success() throws Exception {
        // Arrange
        String email = "jane@example.com";
        User user = new User();
        user.setId(10L);
        user.setFullName("Jane Smith");
        user.setEmail(email);
        user.setMobile("0987654321");
        user.setRole(Role.ADMIN);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        ResponseEntity<Response> responseEntity = userService.getUserByEmail(email);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Response response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("Jane Smith", response.getFullName());
        assertEquals(email, response.getEmail());
        assertEquals("0987654321", response.getMobile());
        assertEquals(Role.ADMIN, response.getRole());
    }

    @Test
    void testGetUserByEmail_UserNotFound() {
        // Arrange
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.getUserByEmail(email);
        });
        assertEquals("User not found with this email", exception.getMessage());
    }
}
