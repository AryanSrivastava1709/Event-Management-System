package com.event.payment_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.event.payment_service.config.FeignConfig;

@FeignClient(name = "EVENT-SERVICE", configuration = FeignConfig.class)
public interface EventInterface {

    @GetMapping("/api/events/my-events")
    public ResponseEntity<?> getMyEvents(@RequestParam String organizerName) throws Exception;
}
