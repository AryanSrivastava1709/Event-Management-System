package com.event.payment_service.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.event.payment_service.dto.OrderResponse;
import com.event.payment_service.dto.PaymentRequest;
import com.event.payment_service.dto.PaymentResponse;
import com.event.payment_service.dto.PaymentVerificationRequest;
import com.event.payment_service.exception.PaymentException;
import com.event.payment_service.feign.BookingInterface;
import com.event.payment_service.mapper.PaymentMapper;
import com.event.payment_service.model.Payment;
import com.event.payment_service.repository.PaymentRepository;

@Service
public class PaymentServiceImpl implements PaymentService {


    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RazorpayService razorpayService;

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private BookingInterface bookingInterface;

    @Override
    public ResponseEntity<OrderResponse> initiateOrder(PaymentRequest paymentRequest) throws Exception {
    try {
        // First check if payment with the same bookingId already exists
        Optional<Payment> existingPayment = paymentRepository.findByBookingId(paymentRequest.getBookingId());
        
        Payment payment;
        OrderResponse orderResponse = new OrderResponse();
        
        if (existingPayment.isPresent()) {
            System.out.println("Using existing payment");
            // Use existing payment if found
            payment = existingPayment.get();
            
            // Only create a new order if the existing payment is still PENDING
            if ("PENDING".equals(payment.getStatus())) {
                orderResponse = razorpayService.createOrder(payment.getAmount());
                orderResponse.setOrderId(payment.getRazorpayOrderId());
            }else{
                throw new PaymentException("Payment already completed or failed");
            }
        } else {
            // Create new payment if no existing payment found
            payment = new Payment();
            payment.setBookingId(paymentRequest.getBookingId());
            payment.setUserId(paymentRequest.getUserId());
            payment.setEventId(paymentRequest.getEventId());
            payment.setAmount(paymentRequest.getAmount());
            payment.setMethod("RAZORPAY");
            payment.setStatus("PENDING");
            payment.setDescription(paymentRequest.getDescription());
            
            payment = paymentRepository.save(payment);
            
            orderResponse = razorpayService.createOrder(paymentRequest.getAmount());
            payment.setRazorpayOrderId(orderResponse.getOrderId());
            paymentRepository.save(payment);
        }

        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    } catch (Exception ex) {
        throw ex;
    }
}

    @Override
    public ResponseEntity<PaymentResponse> verifyPayment(PaymentVerificationRequest verificationRequest) throws Exception {
        try {

            Payment payment = paymentRepository.findByRazorpayOrderId(verificationRequest.getRazorpayOrderId());
            if(payment == null){
                throw new PaymentException("Payment not found");
            }

            boolean isValid = razorpayService.verifyPaymentSignature(verificationRequest.getRazorpayOrderId(), verificationRequest.getRazorpayPaymentId(), verificationRequest.getRazorpaySignature());

            if(isValid){
                payment.setStatus("COMPLETED");
                payment.setRazorpayPaymentId(verificationRequest.getRazorpayPaymentId());
                payment.setRazorpaySignature(verificationRequest.getRazorpaySignature());
                payment.setPaymentTime(LocalDateTime.now());
                payment = paymentRepository.save(payment);

                bookingInterface.confirmBooking(payment.getBookingId());

                PaymentResponse paymentResponse = paymentMapper.toPayResponse(payment);
                return new ResponseEntity<>(paymentResponse, HttpStatus.OK);
            }else{
                payment.setStatus("FAILED");
                payment.setFailureReason("Payment signature verification failed");
                payment = paymentRepository.save(payment);
                throw new PaymentException("Payment verification failed");
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public ResponseEntity<PaymentResponse> getPaymentById(Long paymentId) throws Exception {
        try {
            Optional<Payment> payment = paymentRepository.findById(paymentId);

            if(payment.isEmpty()){
                throw new PaymentException("Payment not found");
            }

            PaymentResponse paymentResponse = paymentMapper.toPayResponse(payment.get());

            return new ResponseEntity<>(paymentResponse, HttpStatus.OK);

        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public ResponseEntity<Map<String,List<PaymentResponse>>> getAllPayments() throws Exception {
        try {
            
            List<Payment> payments = paymentRepository.findAll();

            if(payments.isEmpty()){
                throw new PaymentException("No payments found");
            }

            List<PaymentResponse> paymentResponses = payments.stream().map(paymentMapper::toPayResponse).toList();

            Map<String, List<PaymentResponse>> paymentResponsesMap = new HashMap<>();
            paymentResponsesMap.put("payments", paymentResponses);

            return new ResponseEntity<>(paymentResponsesMap, HttpStatus.OK);
        } catch (Exception ex) {
            throw ex;
        }
    }

    

    @Override
    public ResponseEntity<Map<String,List<PaymentResponse>>> getPaymentsByUserId(Long userId) throws Exception {
        try {
            
            List<Payment> payments = paymentRepository.findByUserId(userId);

            if(payments.isEmpty()){
                throw new PaymentException("No payments found");
            }

            List<PaymentResponse> paymentResponses = payments.stream().map(paymentMapper::toPayResponse).toList();

            Map<String, List<PaymentResponse>> paymentResponsesMap = new HashMap<>();
            paymentResponsesMap.put("payments", paymentResponses);

            return new ResponseEntity<>(paymentResponsesMap, HttpStatus.OK);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public ResponseEntity<Map<String,List<PaymentResponse>>> getPaymentsByEventId(Long eventId) throws Exception {
        try {
            
            List<Payment> payments = paymentRepository.findByEventId(eventId);

            if(payments.isEmpty()){
                throw new PaymentException("No payments found");
            }

            List<PaymentResponse> paymentResponses = payments.stream().map(paymentMapper::toPayResponse).toList();

            Map<String, List<PaymentResponse>> paymentResponsesMap = new HashMap<>();
            paymentResponsesMap.put("payments", paymentResponses);

            return new ResponseEntity<>(paymentResponsesMap, HttpStatus.OK);
        } catch (Exception ex) {
            throw ex;
        }
    }

   

    @Override
    public ResponseEntity<PaymentResponse> getPaymentByBookingId(Long bookingId) throws Exception {
        try {
            
            Optional<Payment> paymentOptional = paymentRepository.findByBookingId(bookingId);
        
            if(paymentOptional.isEmpty()){
                throw new PaymentException("No payments found");
            }
            
            Payment payment = paymentOptional.get();
            PaymentResponse paymentResponse = paymentMapper.toPayResponse(payment);
            return new ResponseEntity<>(paymentResponse, HttpStatus.OK);
            
        } catch (Exception ex) {
            throw ex;
        }
    }

    
    
}
