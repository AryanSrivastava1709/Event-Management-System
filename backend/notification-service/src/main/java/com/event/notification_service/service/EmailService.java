package com.event.notification_service.service;

import org.springframework.http.ResponseEntity;
import com.event.notification_service.dto.EmailRequest;

public interface  EmailService {
    ResponseEntity<String> sendBookingConfirmationEmaik(EmailRequest emailRequest) throws Exception;
}
