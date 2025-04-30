package com.event.payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    
    private Long bookingId;
    private Long userId;
    private Long eventId;
    private Double amount;
    private String description;
}
