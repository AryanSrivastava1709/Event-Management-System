package com.event.event_service.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.event.event_service.dto.EventRequest;
import com.event.event_service.dto.UpdateRequest;

import com.event.event_service.service.EventService;

import jakarta.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping(value = "/add",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createEvent(@Valid @ModelAttribute EventRequest eventRequest){
        return eventService.createEvent(eventRequest);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id, @Valid @RequestBody UpdateRequest updateRequest) throws Exception{
        return eventService.updateEvent(id, updateRequest);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) throws Exception{
        return eventService.deleteEvent(id);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllEvents() throws Exception {
        return eventService.getAllEvents();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getEventById(@PathVariable Long id) throws Exception {
        return eventService.getEventById(id);
    }
    
    @GetMapping("/search")
    public ResponseEntity<?> searchEvents(@RequestParam("param") String keyword) throws Exception {
        return eventService.searchEvents(keyword,keyword,keyword);
    }
    
    @GetMapping("/my-events")
    public ResponseEntity<?> getMyEvents(@RequestParam String organizerName) throws Exception {
        return eventService.getMyEvents(organizerName);
    }
    
}
