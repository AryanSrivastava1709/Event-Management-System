package com.event.payment_service.dto;


import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private Long id;
    private Long bookingId;
    private Long userId;
    private Long eventId;
    private Double amount;
    private String method;
    private String status;
    private LocalDateTime paymentTime;
    private String razorpayOrderId;
    private String description;
}
