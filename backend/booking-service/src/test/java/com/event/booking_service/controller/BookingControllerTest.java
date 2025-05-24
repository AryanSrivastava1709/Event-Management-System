package com.event.booking_service.controller;

import com.event.booking_service.dto.BookingDetailsDto;
import com.event.booking_service.dto.BookingDto;
import com.event.booking_service.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BookingControllerTest {

    @InjectMocks
    private BookingController bookingController;

    @Mock
    private BookingService bookingService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createBooking_shouldReturnCreatedBooking() throws Exception {
        BookingDto dto = new BookingDto();
        ResponseEntity<BookingDto> expectedResponse = new ResponseEntity<>(dto, HttpStatus.CREATED); // ✅ exact match
        when(bookingService.createBooking(dto)).thenReturn(expectedResponse);

        ResponseEntity<?> response = bookingController.createBooking(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void getAllBookings_shouldReturnList() {
        Map<String, List<BookingDto>> bookings = new HashMap<>();
        ResponseEntity<Map<String, List<BookingDto>>> expected = new ResponseEntity<>(bookings, HttpStatus.OK); // ✅
                                                                                                                // exact
                                                                                                                // type
        when(bookingService.getAllBookings()).thenReturn(expected);

        ResponseEntity<?> response = bookingController.getAllBookings();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookings, response.getBody()); // optional: validate body too
    }

    @Test
    void confirmBooking_shouldReturnConfirmedBooking() throws Exception {
        Long bookingId = 1L;
        BookingDto dto = new BookingDto();
        when(bookingService.confirmBooking(bookingId)).thenReturn(ResponseEntity.ok(dto));

        ResponseEntity<?> response = bookingController.confirmBooking(bookingId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getBookingById_shouldReturnBooking() {
        Long bookingId = 1L;
        BookingDto dto = new BookingDto();
        when(bookingService.getBookingById(bookingId)).thenReturn(ResponseEntity.ok(dto));

        ResponseEntity<?> response = bookingController.getBookingById(bookingId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void getBookingDetailsById_shouldReturnBookingDetails() throws Exception {
        Long bookingId = 1L;
        BookingDetailsDto bookingDetails = new BookingDetailsDto(); // Use your actual DTO here

        when(bookingService.getBookingDetailsById(bookingId)).thenReturn(ResponseEntity.ok(bookingDetails));

        ResponseEntity<?> response = bookingController.getBookingDetailsById(bookingId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookingDetails, response.getBody());
    }

    @Test
    void getAllBookingDetails_shouldReturnAllBookingDetails() throws Exception {
        Map<String, List<BookingDetailsDto>> allBookingDetails = new HashMap<>();
        // Optionally put sample data in the map if needed
        when(bookingService.getAllBookingDetails()).thenReturn(ResponseEntity.ok(allBookingDetails));

        ResponseEntity<?> response = bookingController.getMethodName();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(allBookingDetails, response.getBody());
    }

    @Test
    void getBookingsByUserId_shouldReturnBookingsForUser() {
        Integer userId = 1;
        Map<String, List<BookingDto>> bookings = new HashMap<>();
        // Optionally populate with sample data, e.g.:
        bookings.put("bookings", List.of(new BookingDto()));

        when(bookingService.getBookingsByUserId(userId)).thenReturn(ResponseEntity.ok(bookings));

        ResponseEntity<?> response = bookingController.getMethodName(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookings, response.getBody());
    }

    @Test
    void getBookingsByEventId_shouldReturnBookingsForEvent() {
        Integer eventId = 1;
        Map<String, List<BookingDto>> bookings = new HashMap<>();
        // Optionally add sample data
        bookings.put("bookings", List.of(new BookingDto()));

        when(bookingService.getBookingsByEventId(eventId)).thenReturn(ResponseEntity.ok(bookings));

        ResponseEntity<?> response = bookingController.getBookingsByEventId(eventId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookings, response.getBody());
    }

    @Test
    void cancelBooking_shouldReturnResponse() throws Exception {
        Long bookingId = 1L;
        ResponseEntity<Void> expectedResponse = ResponseEntity.ok().build();

        // Use explicit generic for Mockito when()
        Mockito.<ResponseEntity<?>>when(bookingService.deleteBooking(bookingId)).thenReturn(expectedResponse);

        ResponseEntity<?> response = bookingController.cancelBooking(bookingId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateBooking_shouldReturnUpdatedBooking() throws Exception {
        Long bookingId = 1L;
        int updatedSeats = 3;
        BookingDto updatedBooking = new BookingDto();
        when(bookingService.updateBooking(bookingId, updatedSeats)).thenReturn(ResponseEntity.ok(updatedBooking));

        ResponseEntity<?> response = bookingController.updateBooking(bookingId, updatedSeats);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedBooking, response.getBody());
    }

}
