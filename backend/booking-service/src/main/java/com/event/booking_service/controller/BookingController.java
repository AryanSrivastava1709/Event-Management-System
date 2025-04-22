package com.event.booking_service.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.event.booking_service.dto.BookingDto;

import com.event.booking_service.service.BookingService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/add")
    public ResponseEntity<?> createBooking(@Valid @RequestBody BookingDto bookingDto) throws Exception {
        return bookingService.createBooking(bookingDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<?> getBookingDetailsById(@PathVariable Long id) throws Exception {
        return bookingService.getBookingDetailsById(id);
    }
    

    @GetMapping("/all")
    public ResponseEntity<?> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/all/details")
    public ResponseEntity<?> getMethodName() throws Exception {
        return bookingService.getAllBookingDetails();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getMethodName(@PathVariable Integer id) {
        return bookingService.getBookingsByUserId(id);
    }

    @GetMapping("/event/{id}")
    public ResponseEntity<?> getBookingsByEventId(@PathVariable Integer id) {
        return bookingService.getBookingsByEventId(id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id) throws Exception {
        return bookingService.deleteBooking(id);
    }

    @PutMapping("/confirm/{id}")
    public ResponseEntity<?> confirmBooking(@PathVariable Long id) {
        return bookingService.confirmBooking(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable Long id,@RequestParam int updatedSeats) throws Exception {
        return bookingService.updateBooking(id, updatedSeats);
    }
    
}
