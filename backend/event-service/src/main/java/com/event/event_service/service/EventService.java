package com.event.event_service.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.event.event_service.dto.EventRequest;
import com.event.event_service.dto.EventResponse;
import com.event.event_service.dto.UpdateRequest;

public interface EventService {

    ResponseEntity<EventResponse> createEvent(EventRequest eventRequest);

    ResponseEntity<EventResponse> updateEvent(Long id, UpdateRequest updateRequest) throws Exception;

    ResponseEntity<?> deleteEvent(Long id) throws Exception;

    ResponseEntity<Map<String,List<EventResponse>>> getAllEvents() throws Exception;

    ResponseEntity<EventResponse> getEventById(Long id) throws Exception;

    ResponseEntity<Map<String, List<EventResponse>>> searchEvents(String eventName,String description, String location) throws Exception;

    ResponseEntity<Map<String, List<EventResponse>>> getMyEvents(String organizerName) throws Exception;
}
