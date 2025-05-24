package com.event.registration_service.controller;

import com.event.registration_service.dto.RegisterRequest;
import com.event.registration_service.dto.RegisterResponse;
import com.event.registration_service.service.RegistrationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.event.registration_service.model.Role;

@WebMvcTest(RegistrationController.class)
@AutoConfigureMockMvc(addFilters = false) // ✅ disables security filters
public class RegistrationControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private RegistrationService registrationService;

        @Autowired
        private ObjectMapper objectMapper; // To serialize/deserialize JSON

        @Test
        void registerUser_Success() throws Exception {
                RegisterRequest request = new RegisterRequest("John Doe", "john@example.com", "password123",
                                "1234567890", null);
                RegisterResponse response = new RegisterResponse("John Doe", "john@example.com", "1234567890", null);

                Mockito.when(registrationService.registerUser(Mockito.any(RegisterRequest.class)))
                                .thenReturn(new ResponseEntity<>(response, HttpStatus.CREATED));

                mockMvc.perform(post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isCreated())
                                .andExpect(content().json(objectMapper.writeValueAsString(response)));
        }

        @Test
        void registerUser_InvalidInput_ShouldReturnBadRequest() throws Exception {
                // Missing email to trigger validation error
                RegisterRequest request = new RegisterRequest("John Doe", "", "password123", "1234567890", null);

                mockMvc.perform(post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void registerUser_WithAdminRole_Success() throws Exception {
                RegisterRequest request = new RegisterRequest("Admin User", "admin@example.com", "adminpass",
                                "9876543210", Role.ADMIN);
                RegisterResponse response = new RegisterResponse("Admin User", "admin@example.com", "9876543210",
                                Role.ADMIN);

                Mockito.when(registrationService.registerUser(Mockito.any(RegisterRequest.class)))
                                .thenReturn(new ResponseEntity<>(response, HttpStatus.CREATED));

                mockMvc.perform(post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isCreated())
                                .andExpect(content().json(objectMapper.writeValueAsString(response)));
        }

        @Test
        void registerUser_MissingPassword_ShouldReturnBadRequest() throws Exception {
                RegisterRequest request = new RegisterRequest("John Doe", "john@example.com", "", "1234567890",
                                Role.USER);

                mockMvc.perform(post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void registerUser_InvalidEmailFormat_ShouldReturnBadRequest() throws Exception {
                RegisterRequest request = new RegisterRequest("Jane Doe", "invalid-email", "securepass", "1234567890",
                                Role.USER);

                mockMvc.perform(post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest());
        }

}
