package com.event.login_service.controller;

import com.event.login_service.dto.LoginRequest;
import com.event.login_service.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/auth")
public class LoginController {

    //This is a controller to get the login request and call the login service and also it validates the request

    @Autowired
    private LoginService loginService;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) throws Exception {
        return loginService.login(loginRequest);
    }
    
}