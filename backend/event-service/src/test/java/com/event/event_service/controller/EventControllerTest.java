package com.event.event_service.controller;

import com.event.event_service.dto.EventRequest;
import com.event.event_service.dto.EventResponse;
import com.event.event_service.dto.UpdateRequest;
import com.event.event_service.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EventControllerTest {

    @InjectMocks
    private EventController eventController;

    @Mock
    private EventService eventService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEvent() {
        EventRequest request = EventRequest.builder()
                .eventName("Event")
                .description("desc")
                .location("loc")
                .price(100)
                .availableSeats(100)
                .organizername("org")
                .date(java.time.LocalDate.now().plusDays(1))
                .time(java.time.LocalTime.NOON)
                .image(new MockMultipartFile("file", "img.jpg", "image/jpeg", new byte[1]))
                .build();

        EventResponse response = new EventResponse();
        when(eventService.createEvent(request)).thenReturn(new ResponseEntity<>(response, HttpStatus.CREATED));

        ResponseEntity<?> resp = eventController.createEvent(request);

        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
    }

    @Test
    void testUpdateEvent() throws Exception {
        Long id = 1L;
        UpdateRequest updateRequest = new UpdateRequest();
        EventResponse response = new EventResponse();

        when(eventService.updateEvent(id, updateRequest)).thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        ResponseEntity<?> resp = eventController.updateEvent(id, updateRequest);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
    }

    @Test
    void testGetAllEvents() throws Exception {
        when(eventService.getAllEvents()).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> resp = eventController.getAllEvents();

        assertEquals(HttpStatus.OK, resp.getStatusCode());
    }

    @Test
    void testGetEventById() throws Exception {
        Long id = 1L;
        when(eventService.getEventById(id)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> resp = eventController.getEventById(id);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
    }

    @Test
    void testSearchEvents() throws Exception {
        String keyword = "test";
        when(eventService.searchEvents(keyword, keyword, keyword)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> resp = eventController.searchEvents(keyword);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
    }

    @Test
    void testGetMyEvents() throws Exception {
        String organizerName = "organizer";
        when(eventService.getMyEvents(organizerName)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> resp = eventController.getMyEvents(organizerName);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
    }

    
}
