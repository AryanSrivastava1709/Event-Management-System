package com.event.booking_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.event.booking_service.dto.UserDto;

@FeignClient(name = "USER-SERVICE")
public interface UserInterface {

    @GetMapping("/api/user/id/")
    public UserDto getUserById(@RequestParam String id) throws Exception;

    @GetMapping("/api/user/email/")
    public UserDto getUserByEmail(@RequestParam String email) throws Exception;
}
