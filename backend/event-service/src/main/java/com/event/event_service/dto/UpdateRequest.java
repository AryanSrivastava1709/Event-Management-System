package com.event.event_service.dto;

import com.event.event_service.model.Status;
import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRequest {


    private String eventName;
    

    private String description;


    private String location;


    @FutureOrPresent(message = "Date must be today or greater")
    private LocalDate date;


    private LocalTime time;


    @Positive(message = "Price must be positive")
    private Double price;

    private Integer availableSeats;


    private String organizername;


    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private Status status;
    
}
