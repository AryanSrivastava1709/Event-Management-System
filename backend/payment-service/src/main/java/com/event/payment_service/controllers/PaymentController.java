package com.event.payment_service.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.event.payment_service.dto.PaymentRequest;
import com.event.payment_service.dto.PaymentVerificationRequest;
import com.event.payment_service.service.PaymentService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/initiate")
    public ResponseEntity<?> initiatePayment(@RequestBody PaymentRequest paymentRequest) throws Exception {
        return paymentService.initiateOrder(paymentRequest);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody PaymentVerificationRequest verificationRequest) throws Exception {
        return paymentService.verifyPayment(verificationRequest);
    }
    

    @GetMapping("/payment/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable Long id) throws Exception {
        return paymentService.getPaymentById(id);
    }
    
    
    
}
