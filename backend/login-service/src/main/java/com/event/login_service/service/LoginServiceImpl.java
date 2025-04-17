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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;


/*
 * This class is used to implement the login service
 * It implements the LoginService interface and overrides the login method
 * It uses the UserRepository to get the user details from the database
 * It uses the JwtUtils to generate the JWT token
 * It uses the AuthenticationManager to authenticate the user
 * It returns the response in the form of ResponseEntity<LoginResponse>
 * If the user is not found in the database, it throws the UserNotFoundException
 * If the password is invalid, it throws the BadCredentialsException
 */

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest)throws Exception {
        
        try {
            User user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new UserNotFoundException("User not found with the email"));

            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                    )
                );
                

            String jwt = jwtUtils.generateToken(user.getEmail(), user.getRole().toString());

             LoginResponse loginResponse = new LoginResponse();
                loginResponse.setFullName(user.getFullName());
                loginResponse.setEmail(user.getEmail());
                loginResponse.setMobile(user.getMobile());
                loginResponse.setToken(jwt);
                loginResponse.setRole(user.getRole());

            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Invalid Password");
        } catch (Exception ex) {
            throw ex;
        }
    }
    
}