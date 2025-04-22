package com.event.booking_service.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {

    private Long id;
    private String eventName;
    private String description;
    private String location;
    private LocalDate date;
    private LocalTime time;
    private Double price;
    private Integer availableSeats;
    private String organizerName;
    private String imageUrl;
    private String status;
}
