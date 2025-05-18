package com.event.user_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.event.user_service.dto.Response;
import com.event.user_service.exception.UserNotFoundException;
import com.event.user_service.model.User;
import com.event.user_service.repository.UserRepository;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<Response> getUserById(String id) throws Exception {
        try {

            Long userId = Long.parseLong(id);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found with this id"));

            Response response = new Response();
            response.setId(user.getId());
            response.setFullName(user.getFullName());
            response.setEmail(user.getEmail());
            response.setMobile(user.getMobile());
            response.setRole(user.getRole());

            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public ResponseEntity<Response> getUserByEmail(String email) throws Exception {
        try {

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UserNotFoundException("User not found with this email"));

            Response response = new Response();
            response.setId(user.getId());
            response.setFullName(user.getFullName());
            response.setEmail(user.getEmail());
            response.setMobile(user.getMobile());
            response.setRole(user.getRole());
            
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            throw ex;
        }
    }
    
}
