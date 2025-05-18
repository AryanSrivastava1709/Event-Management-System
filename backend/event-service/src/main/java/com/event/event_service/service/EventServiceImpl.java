package com.event.event_service.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.event.event_service.dto.EventRequest;
import com.event.event_service.dto.EventResponse;
import com.event.event_service.dto.UpdateRequest;
import com.event.event_service.exception.EventNotFoundException;
import com.event.event_service.exception.SearchResultNotFoundException;
import com.event.event_service.feign.ImageInterface;
import com.event.event_service.mapper.EventMapper;
import com.event.event_service.model.Event;
import com.event.event_service.repository.EventRepository;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ImageInterface imageInterface;

    @Override
    public ResponseEntity<EventResponse> createEvent(EventRequest eventRequest) {

        //uploading the image

        String imageUrl = null;

        if(eventRequest.getImage() != null && !eventRequest.getImage().isEmpty()){
            ResponseEntity<?> response = imageInterface.uploadImage(eventRequest.getImage());

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String,String> responseBody = (Map<String, String>) response.getBody();
                imageUrl = responseBody.get("imageUrl");
            }
        }

        // Convert EventRequest to Event entity
        Event event = EventMapper.toEntity(eventRequest);

        // Set the image URL in the event entity
        event.setImageUrl(imageUrl);

        // Save the event to the database
        Event savedEvent = eventRepository.save(event);

        // Convert the saved event to EventResponse
        EventResponse eventResponse = EventMapper.toResponse(savedEvent);
        
        return new ResponseEntity<>(eventResponse, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<EventResponse> updateEvent(Long id, UpdateRequest updateRequest) throws Exception {
        try{

            Event event = eventRepository.findById(id)
                    .orElseThrow(() -> new EventNotFoundException("Event not found"));

            // Update the event properties with the values from updateRequest
            EventMapper.updateEntity(event, updateRequest);

            // Save the updated event to the database
            Event updatedEvent = eventRepository.save(event);

            // Convert the updated event to EventResponse
            EventResponse eventResponse = EventMapper.toResponse(updatedEvent);

            return new ResponseEntity<>(eventResponse, HttpStatus.OK);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public ResponseEntity<?> deleteEvent(Long id) throws Exception {

        try {
            eventRepository.findById(id)
                    .orElseThrow(() -> new EventNotFoundException("Event not found"));

            eventRepository.deleteById(id);
            return new ResponseEntity<>("",HttpStatus.OK);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public ResponseEntity<Map<String,List<EventResponse>>> getAllEvents() throws Exception {
        try {

            List<Event> events = eventRepository.findAll();

            if (events.isEmpty()) {
                throw new EventNotFoundException("No events found");
            }

            Map<String, List<EventResponse>> eventMap = new HashMap<>();
            List<EventResponse> eventResponses = new ArrayList<>();
            for (Event event : events){
                EventResponse eventResponse = EventMapper.toResponse(event);
                eventResponses.add(eventResponse);
            }

            eventMap.put("events", eventResponses);

            return new ResponseEntity<>(eventMap, HttpStatus.OK);

        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public ResponseEntity<EventResponse> getEventById(Long id) throws Exception {
        try {
            Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));
            
            EventResponse eventResponse = EventMapper.toResponse(event);

            return new ResponseEntity<>(eventResponse, HttpStatus.OK);
        } catch (Exception ex) {
            throw ex;
        }

    }

    @Override
    public ResponseEntity<Map<String, List<EventResponse>>> searchEvents(String eventName, String description,String location) throws Exception {
        try {
            List<Event> events = eventRepository.findByEventNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrLocationContainingIgnoreCase(eventName, description,location);

            if(events.isEmpty()){
                throw new SearchResultNotFoundException("No events found with the given keywords");
            }

            Map<String, List<EventResponse>> eventMap = new HashMap<>();
            List<EventResponse> eventResponses = new ArrayList<>();
            for (Event event : events){
                EventResponse eventResponse = EventMapper.toResponse(event);
                eventResponses.add(eventResponse);
            }

            eventMap.put("events", eventResponses);

            return new ResponseEntity<>(eventMap, HttpStatus.OK);
            
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public ResponseEntity<Map<String, List<EventResponse>>> getMyEvents(String organizerName) throws Exception {
        try {
            List<Event> events = eventRepository.findByOrganizername(organizerName);

            if(events.isEmpty()){
                throw new EventNotFoundException("You have not created any events");
            }

            Map<String, List<EventResponse>> eventMap = new HashMap<>();
            List<EventResponse> eventResponses = new ArrayList<>();
            for (Event event : events){
                EventResponse eventResponse = EventMapper.toResponse(event);
                eventResponses.add(eventResponse);
            }

            eventMap.put("events", eventResponses);

            return new ResponseEntity<>(eventMap, HttpStatus.OK);
            
        } catch (Exception ex) {
            throw ex;
        }
    }
    
}
