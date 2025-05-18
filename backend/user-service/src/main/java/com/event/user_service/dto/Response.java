package com.event.user_service.dto;

import com.event.user_service.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {

    private long id;

    private String fullName;

    private String email;

    private String mobile;

    private Role role;
}
    

