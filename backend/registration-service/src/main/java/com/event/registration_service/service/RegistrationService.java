package com.event.registration_service.service;

import org.springframework.http.ResponseEntity;

import com.event.registration_service.dto.RegisterRequest;
import com.event.registration_service.dto.RegisterResponse;


public interface RegistrationService {
     ResponseEntity<RegisterResponse> registerUser(RegisterRequest registerRequest) throws Exception ;
}
