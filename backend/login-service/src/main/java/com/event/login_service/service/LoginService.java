package com.event.login_service.service;

import com.event.login_service.dto.LoginRequest;
import com.event.login_service.dto.LoginResponse;
import org.springframework.http.ResponseEntity;
public interface LoginService {
    
    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) throws Exception;
}