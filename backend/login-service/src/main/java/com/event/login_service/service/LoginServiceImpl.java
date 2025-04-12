package com.event.login_service.service;

import com.event.login_service.dto.LoginRequest;
import com.event.login_service.dto.LoginResponse;
import com.event.login_service.exception.UserNotFoundException;
import com.event.login_service.model.User;
import com.event.login_service.repository.UserRepository;
import com.event.login_service.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest)throws Exception {
        
        try {
            User user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new UserNotFoundException("User not found with the email"));

            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                throw new UserNotFoundException("Invalid password");
            }

            Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
                
                SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtUtils.generateToken(user.getEmail(), user.getRole().toString());
             String refreshToken = jwtUtils.generateRefreshToken(user.getEmail());

             LoginResponse loginResponse = new LoginResponse();
                loginResponse.setFullName(user.getFullName());
                loginResponse.setEmail(user.getEmail());
                loginResponse.setMobile(user.getMobile());
                loginResponse.setToken(jwt);
                loginResponse.setRefreshToken(refreshToken);
                loginResponse.setRole(user.getRole());

            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } catch (Exception ex) {
            throw ex;
        }
    }
    
}