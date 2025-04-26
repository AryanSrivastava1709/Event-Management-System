package com.event.booking_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.event.booking_service.dto.EmailRequest;

import jakarta.validation.Valid;

@FeignClient(name = "NOTIFICATION-SERVICE")
public interface NotificationInterface {
    @PostMapping("/api/notification/send-email")
    String sendEmail(@Valid @RequestBody EmailRequest emailRequest) throws Exception;
}
