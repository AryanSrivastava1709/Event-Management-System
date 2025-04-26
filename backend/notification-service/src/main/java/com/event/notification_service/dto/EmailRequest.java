package com.event.notification_service.dto;

import java.util.Map;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest {
    @NotBlank(message = "Email recipient cannot be blank")
    @Email(message = "Invalid email format")
    private String to;
    
    @NotBlank(message = "Subject cannot be blank")
    private String subject;
    
    private Map<String, Object> templateData;
}
