package com.event.booking_service.mapper;

import java.math.BigDecimal;
import org.springframework.stereotype.Component;
import com.event.booking_service.dto.BookingDetailsDto;
import com.event.booking_service.dto.BookingDto;
import com.event.booking_service.dto.EventDto;
import com.event.booking_service.dto.UserDto;
import com.event.booking_service.model.Bookings;


@Component
public class BookingMapper {

    public BookingDto toDto(Bookings bookings){
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(bookings.getId());
        bookingDto.setUserId(bookings.getUserId());
        bookingDto.setEventId(bookings.getEventId());
        bookingDto.setSeatsBooked(bookings.getSeatsBooked());
        bookingDto.setBookingTime(bookings.getBookingTime());
        bookingDto.setBookingDate(bookings.getBookingDate());
        bookingDto.setStatus(bookings.getStatus());
        bookingDto.setTotalAmount(bookings.getTotalAmount());
        return bookingDto;
    }

    public Bookings toEntity(BookingDto bookingDto){
        Bookings bookings = new Bookings();
        bookings.setUserId(bookingDto.getUserId());
        bookings.setEventId(bookingDto.getEventId());
        bookings.setSeatsBooked(bookingDto.getSeatsBooked());
        bookings.setBookingTime(bookingDto.getBookingTime());
        bookings.setBookingDate(bookingDto.getBookingDate());
        bookings.setStatus(bookingDto.getStatus());
        bookings.setTotalAmount(bookingDto.getTotalAmount());
        return bookings;
    }

    public BookingDetailsDto toDetailsDto(Bookings bookings,UserDto userDto,EventDto eventDto){
        BookingDetailsDto bookingDetailsDto = new BookingDetailsDto();
        bookingDetailsDto.setId(bookings.getId());
        bookingDetailsDto.setUser(userDto);
        bookingDetailsDto.setEvent(eventDto);
        bookingDetailsDto.setSeatsBooked(bookings.getSeatsBooked());
        bookingDetailsDto.setBookingTime(bookings.getBookingTime());
        bookingDetailsDto.setBookingDate(bookings.getBookingDate());
        bookingDetailsDto.setStatus(bookings.getStatus());

        if (eventDto != null && eventDto.getPrice() != null) {
            bookingDetailsDto.setTotalAmount(BigDecimal.valueOf(eventDto.getPrice()).multiply(BigDecimal.valueOf(bookingDetailsDto.getSeatsBooked())));
        }
        
        return bookingDetailsDto;
    }
}
