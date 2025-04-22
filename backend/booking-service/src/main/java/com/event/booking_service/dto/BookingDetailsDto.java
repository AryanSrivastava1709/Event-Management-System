package com.event.booking_service.dto;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.event.booking_service.model.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDetailsDto {

    private Long id;
    private UserDto user;
    private EventDto event;
    private Integer seatsBooked;
    private LocalDate bookingDate;
    private LocalTime bookingTime;
    private Status status;
    private BigDecimal totalAmount;
}
