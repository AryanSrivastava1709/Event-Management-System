package com.event.booking_service.exception;

public class InvalidBookingDetailsException extends RuntimeException {
    public InvalidBookingDetailsException(String message) {
        super(message);
    }
    
}
