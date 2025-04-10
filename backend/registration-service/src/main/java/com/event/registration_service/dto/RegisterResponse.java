package com.event.registration_service.dto;

import com.event.registration_service.model.Role;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {
    
    private String fullName;

    private String email;

    private String mobile;

    private Role role;
}
