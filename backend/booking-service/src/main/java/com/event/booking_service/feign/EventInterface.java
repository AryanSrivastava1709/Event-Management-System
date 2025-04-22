package com.event.booking_service.feign;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.event.booking_service.config.FeignConfig;
import com.event.booking_service.dto.EventDto;

@FeignClient(name = "EVENT-SERVICE",configuration = FeignConfig.class)
public interface EventInterface {
    @GetMapping("/api/events/get/{id}")
    EventDto getEventById(@PathVariable Long id) throws Exception;

    @GetMapping("/api/events/all")
    Map<String,List<EventDto>> getAllEvents() throws Exception;

    @PutMapping("/api/events/update/{id}")
    EventDto updateEvent(@PathVariable Long id,EventDto eventDto) throws Exception;

}
