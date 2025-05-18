package com.event.event_service.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import com.event.event_service.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {
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
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
