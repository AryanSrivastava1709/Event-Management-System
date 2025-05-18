package com.event.payment_service.model;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long bookingId;
    
    @Column(nullable = false)
    private Long userId;
    
    @Column(nullable = false)
    private Long eventId;
    
    @Column(nullable = false)
    private Double amount;
    
    @Column(nullable = false)
    private String method;
    
    @Column(nullable = false)
    private String status;
    
    @Column(name = "payment_time")
    private LocalDateTime paymentTime;
    
    @Column(name = "razorpay_payment_id")
    private String razorpayPaymentId;
    
    @Column(name = "razorpay_order_id")
    private String razorpayOrderId;
    
    @Column(name = "razorpay_signature")
    private String razorpaySignature;
    
    @Column(length = 500)
    private String description;
    
    @Column(name = "failure_reason")
    private String failureReason;
}
