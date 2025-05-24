package com.event.login_service.controller;

import com.event.login_service.dto.LoginRequest;
import com.event.login_service.dto.LoginResponse;
import com.event.login_service.model.Role;
import com.event.login_service.service.LoginService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
@AutoConfigureMockMvc(addFilters = false) // ✅ disables security filters
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoginService loginService;

    @Autowired
    private ObjectMapper objectMapper;

    private LoginResponse sampleResponseUser;
    private LoginResponse sampleResponseAdmin;

    @BeforeEach
    void setUp() {
        sampleResponseUser = LoginResponse.builder()
                .email("user@example.com")
                .fullName("Regular User")
                .mobile("1234567890")
                .role(Role.USER)
                .token("mock-jwt-token")
                .build();

        sampleResponseAdmin = LoginResponse.builder()
                .email("admin@example.com")
                .fullName("Admin User")
                .mobile("0987654321")
                .role(Role.ADMIN)
                .token("admin-jwt-token")
                .build();
    }

    // Test successful login USER role
    @Test
    void testLogin_Success_User() throws Exception {
        LoginRequest request = new LoginRequest("user@example.com", "password");

        Mockito.when(loginService.login(any(LoginRequest.class)))
                .thenReturn(ResponseEntity.ok(sampleResponseUser));

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@example.com"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.token").value("mock-jwt-token"));
    }

    // Test successful login ADMIN role
    @Test
    void testLogin_Success_Admin() throws Exception {
        LoginRequest request = new LoginRequest("admin@example.com", "adminpass");

        Mockito.when(loginService.login(any(LoginRequest.class)))
                .thenReturn(ResponseEntity.ok(sampleResponseAdmin));

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("admin@example.com"))
                .andExpect(jsonPath("$.role").value("ADMIN"))
                .andExpect(jsonPath("$.token").value("admin-jwt-token"));
    }

    // Test validation error - missing email
    @Test
    void testLogin_ValidationError_MissingEmail() throws Exception {
        LoginRequest request = new LoginRequest("", "password");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Email is required"));
    }

    // Test validation error - invalid email format
    @Test
    void testLogin_ValidationError_InvalidEmail() throws Exception {
        LoginRequest request = new LoginRequest("invalid-email", "password");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Email is not valid"));
    }

    // Test validation error - missing password
    @Test
    void testLogin_ValidationError_MissingPassword() throws Exception {
        LoginRequest request = new LoginRequest("user@example.com", "");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password").value("Password is required"));
    }
}
