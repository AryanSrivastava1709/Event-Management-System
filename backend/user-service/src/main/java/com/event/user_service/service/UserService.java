package com.event.user_service.service;

import org.springframework.http.ResponseEntity;
import com.event.user_service.dto.Response;

public interface UserService {

    ResponseEntity<Response> getUserById(String id) throws Exception;

    ResponseEntity<Response> getUserByEmail(String email) throws Exception;
}
