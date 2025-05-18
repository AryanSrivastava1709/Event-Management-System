package com.event.user_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import com.event.user_service.service.UserService;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/id/")
    public ResponseEntity<?> getUserById(@RequestParam String id) throws Exception{

        if(id == null || id.isEmpty()){
            return new ResponseEntity<>("User ID cannot be null or empty", HttpStatus.BAD_REQUEST);
        }

        return userService.getUserById(id);
    }

    @GetMapping("/email/")
    public ResponseEntity<?> getUserByEmail(@RequestParam String email) throws Exception {
        if(email == null || email.isEmpty()){
            return new ResponseEntity<>("Email cannot be null or empty", HttpStatus.BAD_REQUEST);
        }

        return userService.getUserByEmail(email);
    }
    
    
}
