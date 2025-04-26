package com.event.notification_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.event.notification_service.dto.EmailRequest;
import com.event.notification_service.service.EmailService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/api/notification")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send-email")
    public ResponseEntity<?> sendEmail(@Valid @RequestBody EmailRequest emailRequest) throws Exception {
        return emailService.sendBookingConfirmationEmaik(emailRequest);
    }
    
}
