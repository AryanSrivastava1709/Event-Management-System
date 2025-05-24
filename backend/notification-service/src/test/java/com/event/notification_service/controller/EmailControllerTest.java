package com.event.notification_service.controller;

import com.event.notification_service.dto.EmailRequest;
import com.event.notification_service.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EmailControllerTest {

    @InjectMocks
    private EmailController emailController;

    @Mock
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendEmail_shouldReturnSuccessResponse() throws Exception {
        EmailRequest emailRequest = new EmailRequest("test@example.com", "Test Subject", new HashMap<>());
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Email sent successfully", HttpStatus.OK);

        when(emailService.sendBookingConfirmationEmaik(emailRequest)).thenReturn(expectedResponse);

        ResponseEntity<?> response = emailController.sendEmail(emailRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Email sent successfully", response.getBody());
    }

    @Test
    void sendEmail_shouldThrowException() throws Exception {
        EmailRequest emailRequest = new EmailRequest("test@example.com", "Test Subject", new HashMap<>());

        when(emailService.sendBookingConfirmationEmaik(emailRequest)).thenThrow(new RuntimeException("SMTP failure"));

        try {
            emailController.sendEmail(emailRequest);
        } catch (Exception ex) {
            assertEquals("SMTP failure", ex.getMessage());
        }
    }
}
