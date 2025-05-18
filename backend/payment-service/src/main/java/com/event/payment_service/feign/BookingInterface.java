package com.event.payment_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import com.event.payment_service.config.FeignConfig;

@FeignClient(name = "BOOKING-SERVICE", configuration = FeignConfig.class)
public interface BookingInterface {

   @PutMapping("/api/bookings/confirm/{id}")
    public ResponseEntity<?> confirmBooking(@PathVariable Long id) throws Exception;
}
