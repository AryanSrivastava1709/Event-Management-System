package com.event.registration_service.service;

import com.event.registration_service.dto.RegisterRequest;
import com.event.registration_service.dto.RegisterResponse;
import com.event.registration_service.exception.UserAlreadyExistsException;
import com.event.registration_service.model.User;
import com.event.registration_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.event.registration_service.model.Role;

public class RegistrationServiceImplTest {

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_Success() throws Exception {
        RegisterRequest request = new RegisterRequest("John Doe", "john@example.com", "password123", "1234567890",
                null);

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<RegisterResponse> response = registrationService.registerUser(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(request.getFullName(), response.getBody().getFullName());
        assertEquals(request.getEmail(), response.getBody().getEmail());

        verify(userRepository, times(1)).findByEmail(request.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode(request.getPassword());
    }

    @Test
    void registerUser_UserAlreadyExists() {
        RegisterRequest request = new RegisterRequest("John Doe", "john@example.com", "password123", "1234567890",
                null);
        User existingUser = new User();
        existingUser.setEmail(request.getEmail());

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existingUser));

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> {
            registrationService.registerUser(request);
        });

        assertEquals("User with this email already exists", exception.getMessage());

        verify(userRepository, times(1)).findByEmail(request.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerUser_ExceptionThrown() {
        RegisterRequest request = new RegisterRequest("John Doe", "john@example.com", "password123", "1234567890",
                null);

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        // Simulate exception when saving user
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(Exception.class, () -> {
            registrationService.registerUser(request);
        });

        assertEquals("Database error", exception.getMessage());

        verify(userRepository, times(1)).findByEmail(request.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_WithAdminRole_Success() throws Exception {
        RegisterRequest request = new RegisterRequest("Admin", "admin@example.com", "adminpass", "9876543210",
                Role.ADMIN);

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedAdminPass");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<RegisterResponse> response = registrationService.registerUser(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(Role.ADMIN, response.getBody().getRole());
    }

    @Test
    void registerUser_NullRole_ShouldStillRegister() throws Exception {
        RegisterRequest request = new RegisterRequest("User NullRole", "nullrole@example.com", "securepass",
                "1234567890", null);

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<RegisterResponse> response = registrationService.registerUser(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNull(response.getBody().getRole());
    }

}
