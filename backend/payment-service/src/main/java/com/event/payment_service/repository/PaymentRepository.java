package com.event.payment_service.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.event.payment_service.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {

    List<Payment> findByUserId(Long userId);

    Payment findByRazorpayOrderId(String razorpayOrderId);
    
    List<Payment> findByEventId(Long eventId);
    
    Optional<Payment> findByBookingId(Long bookingId);
    
    List<Payment> findByStatus(String status);

}
