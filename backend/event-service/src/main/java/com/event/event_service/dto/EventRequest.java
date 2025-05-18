package com.event.event_service.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.web.multipart.MultipartFile;
import com.event.event_service.model.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {

    @NotBlank(message = "Event name is required")
    private String eventName;
    
    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Date is required")
    @FutureOrPresent(message = "Date must be today or greater")
    private LocalDate date;

    @NotNull(message = "Time is required")
    private LocalTime time;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private double price;

    @NotNull(message = "Available seats is required")
    @Min(value = 50, message = "Available seats must be at least 50")
    private int availableSeats;

    @NotBlank(message = "Organizer name is required")
    private String organizername;

    @NotNull(message = "Image is required")
    private MultipartFile image;

    @Enumerated(EnumType.STRING)
    private Status status;
    
}
