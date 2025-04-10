package com.event.registration_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.event.registration_service.dto.RegisterRequest;
import com.event.registration_service.dto.RegisterResponse;
import com.event.registration_service.exception.UserAlreadyExistsException;
import com.event.registration_service.model.User;
import com.event.registration_service.repository.UserRepository;


@Service
public class RegistrationServiceImpl implements RegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public  ResponseEntity<RegisterResponse> registerUser(RegisterRequest registerRequest) throws Exception {

        try {
            if(userRepository.findByEmail(registerRequest.getEmail()).isPresent()){
                throw new UserAlreadyExistsException("User with this email already exists");
            }

            User user =  new User();
            user.setFullName(registerRequest.getFullName());
            user.setEmail(registerRequest.getEmail()); 
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user.setMobile(registerRequest.getMobile());
            user.setRole(registerRequest.getRole());


            RegisterResponse registerResponse = new RegisterResponse();
            registerResponse.setFullName(user.getFullName());
            registerResponse.setEmail(user.getEmail());
            registerResponse.setMobile(user.getMobile());
            registerResponse.setRole(user.getRole());


            userRepository.save(user);

            return new ResponseEntity<>(registerResponse, HttpStatus.CREATED);

        } catch (Exception ex) {
            throw ex;
        }
    }
    
}
