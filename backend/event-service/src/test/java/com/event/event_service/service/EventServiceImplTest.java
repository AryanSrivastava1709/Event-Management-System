package com.event.event_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.event.event_service.dto.EventRequest;
import com.event.event_service.dto.EventResponse;
import com.event.event_service.dto.UpdateRequest;
import com.event.event_service.exception.EventNotFoundException;
import com.event.event_service.exception.SearchResultNotFoundException;
import com.event.event_service.feign.ImageInterface;
import com.event.event_service.mapper.EventMapper;
import com.event.event_service.model.Event;
import com.event.event_service.repository.EventRepository;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

public class EventServiceImplTest {

    @InjectMocks
    private EventServiceImpl eventService;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ImageInterface imageInterface;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEvent_withImage_success() {
        MockMultipartFile image = new MockMultipartFile("file", "image.jpg", "image/jpeg", "dummy-image".getBytes());
        EventRequest request = EventRequest.builder()
                .eventName("Test Event")
                .description("desc")
                .location("loc")
                .date(java.time.LocalDate.now().plusDays(1))
                .time(java.time.LocalTime.NOON)
                .price(100)
                .availableSeats(100)
                .organizername("Organizer")
                .image(image)
                .build();

        Map<String, String> imageResponse = new HashMap<>();
        imageResponse.put("imageUrl", "http://image.url/image.jpg");

        when(imageInterface.uploadImage(any())).thenReturn(new ResponseEntity<>(imageResponse, HttpStatus.OK));
        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<EventResponse> response = eventService.createEvent(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("http://image.url/image.jpg", response.getBody().getImageUrl());
    }

    @Test
    void testUpdateEvent_success() throws Exception {
        Long eventId = 1L;
        UpdateRequest updateRequest = new UpdateRequest();
        Event existingEvent = new Event();
        existingEvent.setId(eventId);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(existingEvent));
        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<EventResponse> response = eventService.updateEvent(eventId, updateRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(eventRepository).save(existingEvent);
    }

    @Test
    void testUpdateEvent_notFound() {
        Long eventId = 1L;
        UpdateRequest updateRequest = new UpdateRequest();

        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> eventService.updateEvent(eventId, updateRequest));
    }

    @Test
    void testDeleteEvent_success() throws Exception {
        Long eventId = 1L;
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(new Event()));
        doNothing().when(eventRepository).deleteById(eventId);

        ResponseEntity<?> response = eventService.deleteEvent(eventId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteEvent_notFound() {
        Long eventId = 1L;
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> eventService.deleteEvent(eventId));
    }

    @Test
    void testGetAllEvents_success() throws Exception {
        Event event = new Event();
        when(eventRepository.findAll()).thenReturn(List.of(event));

        ResponseEntity<Map<String, List<EventResponse>>> response = eventService.getAllEvents();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().containsKey("events"));
    }

    @Test
    void testGetAllEvents_noEvents() {
        when(eventRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(EventNotFoundException.class, () -> eventService.getAllEvents());
    }

    @Test
    void testGetEventById_success() throws Exception {
        Long id = 1L;
        Event event = new Event();
        when(eventRepository.findById(id)).thenReturn(Optional.of(event));

        ResponseEntity<EventResponse> response = eventService.getEventById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetEventById_notFound() {
        Long id = 1L;
        when(eventRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> eventService.getEventById(id));
    }

    @Test
    void testSearchEvents_success() throws Exception {
        Event event = new Event();
        when(eventRepository.findByEventNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrLocationContainingIgnoreCase(anyString(), anyString(), anyString()))
            .thenReturn(List.of(event));

        ResponseEntity<Map<String, List<EventResponse>>> response = eventService.searchEvents("a", "a", "a");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testSearchEvents_noResults() {
        when(eventRepository.findByEventNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrLocationContainingIgnoreCase(anyString(), anyString(), anyString()))
            .thenReturn(Collections.emptyList());

        assertThrows(SearchResultNotFoundException.class, () -> eventService.searchEvents("a", "a", "a"));
    }

    @Test
    void testGetMyEvents_success() throws Exception {
        Event event = new Event();
        when(eventRepository.findByOrganizername("organizer")).thenReturn(List.of(event));

        ResponseEntity<Map<String, List<EventResponse>>> response = eventService.getMyEvents("organizer");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetMyEvents_noEvents() {
        when(eventRepository.findByOrganizername("organizer")).thenReturn(Collections.emptyList());

        assertThrows(EventNotFoundException.class, () -> eventService.getMyEvents("organizer"));
    }
}
