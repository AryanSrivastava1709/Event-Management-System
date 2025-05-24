package com.event.notification_service.service;

import com.event.notification_service.dto.EmailRequest;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailServiceImplTest {

    @InjectMocks
    private EmailServiceImpl emailService;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private SpringTemplateEngine templateEngine;

    @Mock
    private MimeMessage mimeMessage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendBookingConfirmationEmaik_shouldSendEmailSuccessfully() throws Exception {
        EmailRequest emailRequest = new EmailRequest("recipient@example.com", "Booking Confirmed", new HashMap<>());

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("booking-confirmation"), any(Context.class))).thenReturn("<html>Email</html>");

        ResponseEntity<String> response = emailService.sendBookingConfirmationEmaik(emailRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Email sent successfully", response.getBody());
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void sendBookingConfirmationEmaik_shouldThrowExceptionOnFailure() {
        EmailRequest emailRequest = new EmailRequest("recipient@example.com", "Booking Confirmed", new HashMap<>());

        when(mailSender.createMimeMessage()).thenThrow(new RuntimeException("SMTP Error"));

        Exception exception = assertThrows(Exception.class, () -> {
            emailService.sendBookingConfirmationEmaik(emailRequest);
        });

        assertEquals("SMTP Error", exception.getMessage());
    }
}
