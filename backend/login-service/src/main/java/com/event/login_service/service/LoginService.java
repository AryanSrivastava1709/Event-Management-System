package com.event.login_service.service;

import com.event.login_service.dto.LoginRequest;
import com.event.login_service.dto.LoginResponse;
import com.event.login_service.model.User;
import com.event.login_service.repository.UserRepository;
import com.event.login_service.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        String jwt = jwtUtils.generateToken(user.getEmail(), user.getRole().toString());
        String refreshToken = jwtUtils.generateRefreshToken(user.getEmail());
        
        return LoginResponse.builder()
                .token(jwt)
                .refreshToken(refreshToken)
                .email(user.getEmail())
                .role(user.getRole().toString())
                .fullName(user.getFullName())
                .build();
    }
    
    public boolean verifyToken(String token) {
        return jwtUtils.validateToken(token);
    }
}