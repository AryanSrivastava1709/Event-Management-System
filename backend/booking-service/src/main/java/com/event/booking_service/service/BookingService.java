package com.event.booking_service.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.event.booking_service.dto.BookingDetailsDto;
import com.event.booking_service.dto.BookingDto;

public interface BookingService {

    ResponseEntity<BookingDto> createBooking(BookingDto bookingDto) throws Exception;

    ResponseEntity<BookingDto> getBookingById(Long bookingId);

    ResponseEntity<BookingDetailsDto> getBookingDetailsById(Long bookingId) throws Exception;

    ResponseEntity<Map<String,List<BookingDto>>> getAllBookings();

    ResponseEntity<Map<String,List<BookingDetailsDto>>> getAllBookingDetails() throws Exception;

    ResponseEntity<Map<String,List<BookingDto>>> getBookingsByUserId(Integer userId);
    
    ResponseEntity<Map<String,List<BookingDto>>> getBookingsByEventId(Integer eventId);

    ResponseEntity<BookingDto> updateBooking(Long bookingId, int updatedSeats) throws Exception;

    ResponseEntity<BookingDto> confirmBooking(Long bookingId);

    ResponseEntity<?> deleteBooking(Long bookingId) throws Exception;

}
