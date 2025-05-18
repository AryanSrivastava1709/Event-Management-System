package com.event.payment_service.service;

import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import com.event.payment_service.dto.OrderResponse;
import com.event.payment_service.dto.PaymentRequest;
import com.event.payment_service.dto.PaymentResponse;
import com.event.payment_service.dto.PaymentVerificationRequest;

public interface PaymentService {

    ResponseEntity<OrderResponse> initiateOrder(PaymentRequest paymentRequest) throws Exception;

    ResponseEntity<PaymentResponse> verifyPayment(PaymentVerificationRequest verificationRequest) throws Exception;

    ResponseEntity<PaymentResponse> getPaymentById(Long paymentId) throws Exception;

    ResponseEntity<Map<String,List<PaymentResponse>>> getAllPayments() throws Exception;

    ResponseEntity<Map<String,List<PaymentResponse>>> getPaymentsByUserId(Long userId) throws Exception;

    ResponseEntity<Map<String,List<PaymentResponse>>> getPaymentsByEventId(Long eventId) throws Exception;

    ResponseEntity<PaymentResponse> getPaymentByBookingId(Long bookingId) throws Exception;
    
}
