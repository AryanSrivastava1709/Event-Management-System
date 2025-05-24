package com.event.login_service.service;

import com.event.login_service.config.JwtUtils;
import com.event.login_service.dto.LoginRequest;
import com.event.login_service.dto.LoginResponse;
import com.event.login_service.exception.UserNotFoundException;
import com.event.login_service.model.Role;
import com.event.login_service.model.User;
import com.event.login_service.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoginServiceImplTest {

    @InjectMocks
    private LoginServiceImpl loginService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test successful login with USER role
    @Test
    void testLogin_Success_UserRole() throws Exception {
        LoginRequest request = new LoginRequest("user@example.com", "password");

        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("encodedPassword");
        user.setFullName("Regular User");
        user.setMobile("1234567890");
        user.setRole(Role.USER);

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(jwtUtils.generateToken("user@example.com", "USER")).thenReturn("mock-jwt-token");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("user@example.com", "password"));

        ResponseEntity<LoginResponse> response = loginService.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("user@example.com", response.getBody().getEmail());
        assertEquals("mock-jwt-token", response.getBody().getToken());
        assertEquals(Role.USER, response.getBody().getRole());
        assertEquals("Regular User", response.getBody().getFullName());
    }

    // Test successful login with ADMIN role
    @Test
    void testLogin_Success_AdminRole() throws Exception {
        LoginRequest request = new LoginRequest("admin@example.com", "adminpass");

        User user = new User();
        user.setEmail("admin@example.com");
        user.setPassword("encodedPassword");
        user.setFullName("Admin User");
        user.setMobile("0987654321");
        user.setRole(Role.ADMIN);

        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(user));
        when(jwtUtils.generateToken("admin@example.com", "ADMIN")).thenReturn("admin-jwt-token");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("admin@example.com", "adminpass"));

        ResponseEntity<LoginResponse> response = loginService.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("admin@example.com", response.getBody().getEmail());
        assertEquals("admin-jwt-token", response.getBody().getToken());
        assertEquals(Role.ADMIN, response.getBody().getRole());
        assertEquals("Admin User", response.getBody().getFullName());
    }

    // Test user not found scenario
    @Test
    void testLogin_UserNotFound() {
        LoginRequest request = new LoginRequest("nouser@example.com", "pass");

        when(userRepository.findByEmail("nouser@example.com")).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            loginService.login(request);
        });

        assertEquals("User not found with the email", exception.getMessage());
    }

    // Test invalid password (BadCredentialsException)
    @Test
    void testLogin_InvalidPassword() {
        LoginRequest request = new LoginRequest("user@example.com", "wrongpass");

        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("encodedPassword");
        user.setRole(Role.USER);

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        // Simulate authenticationManager throwing BadCredentialsException
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid Password"));

        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
            loginService.login(request);
        });

        assertEquals("Invalid Password", exception.getMessage());
    }
}
