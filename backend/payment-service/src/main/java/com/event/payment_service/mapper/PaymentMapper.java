package com.event.payment_service.mapper;

import org.springframework.stereotype.Component;
import com.event.payment_service.dto.PaymentResponse;
import com.event.payment_service.model.Payment;

@Component
public class PaymentMapper {
    

    public PaymentResponse toPayResponse(Payment payment){
        return new PaymentResponse(
            payment.getId(),
            payment.getBookingId(),
            payment.getUserId(),
            payment.getEventId(),
            payment.getAmount(),
            payment.getMethod(),
            payment.getStatus(),
            payment.getPaymentTime(),
            payment.getRazorpayOrderId(),
            payment.getDescription()
    );
    }
}
