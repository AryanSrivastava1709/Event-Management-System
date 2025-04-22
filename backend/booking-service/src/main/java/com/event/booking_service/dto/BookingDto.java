package com.event.booking_service.dto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.event.booking_service.model.Status;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private Long id;
    
    @NotNull(message = "User ID is required")
    private Integer userId;
    
    @NotNull(message = "Event ID is required")
    private Integer eventId;
    
    @NotNull(message = "Number of seats is required")
    @Min(value = 1, message = "At least one seat must be booked")
    private Integer seatsBooked;
    
    private LocalDate bookingDate;
    private LocalTime bookingTime;
    private Status status;
}
