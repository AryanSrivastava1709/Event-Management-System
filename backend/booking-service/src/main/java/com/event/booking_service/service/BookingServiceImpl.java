package com.event.booking_service.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.event.booking_service.dto.BookingDetailsDto;
import com.event.booking_service.dto.BookingDto;
import com.event.booking_service.dto.EmailRequest;
import com.event.booking_service.dto.EventDto;
import com.event.booking_service.dto.UserDto;
import com.event.booking_service.exception.BookingNotFoundException;
import com.event.booking_service.exception.InvalidBookingDetailsException;
import com.event.booking_service.feign.EventInterface;
import com.event.booking_service.feign.NotificationInterface;
import com.event.booking_service.feign.UserInterface;
import com.event.booking_service.mapper.BookingMapper;
import com.event.booking_service.model.Bookings;
import com.event.booking_service.model.Status;
import com.event.booking_service.repository.BookingRepository;

import feign.FeignException.FeignClientException;


@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private UserInterface userInterface;

    @Autowired
    private EventInterface eventInterface;

    @Autowired
    private NotificationInterface notificationInterface;


    @Override
    public ResponseEntity<BookingDto> createBooking(BookingDto bookingDto) throws Exception {

        // 1. Fetch user details
        try {
            UserDto userDto = userInterface.getUserById(String.valueOf(bookingDto.getUserId()));
        } catch (FeignClientException ex) {
            throw new InvalidBookingDetailsException("User not found");
        }

        // 2. Fetch event details
        EventDto eventDto;
        try {
            eventDto = eventInterface.getEventById(bookingDto.getEventId().longValue());
        } catch (FeignClientException ex) {
            throw new InvalidBookingDetailsException("Event not found");
        }

        // 3. Validate event status
        if (!"ACTIVE".equalsIgnoreCase(eventDto.getStatus())) {
            throw new InvalidBookingDetailsException("Event is not active or the seats are full");
        }

        // 4. Validate available seats
        if (eventDto.getAvailableSeats() < bookingDto.getSeatsBooked()) {
            throw new InvalidBookingDetailsException("Not enough seats available");
        }

        // 5. Update event if this booking fills the last seat
        int updatedAvailableSeats = eventDto.getAvailableSeats() - bookingDto.getSeatsBooked();
        if (updatedAvailableSeats <= 0) {
            eventDto.setStatus("UPCOMING");
            eventDto.setAvailableSeats(0);
        } else {
            eventDto.setAvailableSeats(updatedAvailableSeats);
        }

        // 6. Set booking metadata
        bookingDto.setBookingTime(LocalTime.now());
        bookingDto.setBookingDate(LocalDate.now());
        bookingDto.setStatus(Status.PENDING);

        // 7. Save booking
        Bookings booking = bookingMapper.toEntity(bookingDto);
        bookingRepository.save(booking);

        // 8. Update event with new status and seat count
        eventInterface.updateEvent(eventDto.getId().longValue(), eventDto);

        // 9. Return response
        return new ResponseEntity<>(bookingMapper.toDto(booking), HttpStatus.CREATED);
    }


    @Override
    public ResponseEntity<BookingDto> getBookingById(Long bookingId) {

        try {
            Bookings bookings = bookingRepository.findById(bookingId).orElseThrow(()-> new BookingNotFoundException("Booking not found"));

            BookingDto bookingDto = bookingMapper.toDto(bookings);
            return new ResponseEntity<>(bookingDto, HttpStatus.OK);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public ResponseEntity<BookingDetailsDto> getBookingDetailsById(Long bookingId) throws Exception {
        Bookings booking = bookingRepository.findById(bookingId).orElseThrow(()-> new BookingNotFoundException("Booking not found"));

        try{
            UserDto userDto = userInterface.getUserById(String.valueOf(booking.getUserId()));
            EventDto eventDto = eventInterface.getEventById((long) booking.getEventId());
            return new ResponseEntity<>(bookingMapper.toDetailsDto(booking, userDto, eventDto), HttpStatus.OK);
        }catch (Exception ex) {
            throw ex;
        } 
    }

    @Override
    public ResponseEntity<Map<String, List<BookingDto>>> getAllBookings() {

        try {
            List<Bookings> bookings = bookingRepository.findAll();

            if (bookings.isEmpty()) {
                throw new BookingNotFoundException("No bookings found");
            }
            List<BookingDto> bookingDtos = new ArrayList<>();

            Map<String, List<BookingDto>> bookingMap = new HashMap<>();
            for (Bookings booking : bookings) {
                BookingDto bookingDto = bookingMapper.toDto(booking);
                bookingDtos.add(bookingDto);
            }

            bookingMap.put("bookings", bookingDtos);

            return new ResponseEntity<>(bookingMap, HttpStatus.OK);
            
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public ResponseEntity<Map<String, List<BookingDetailsDto>>> getAllBookingDetails() throws Exception {
        try {
            List<Bookings> bookings = bookingRepository.findAll();

            if (bookings.isEmpty()) {
                throw new BookingNotFoundException("No bookings found");
            }
            List<BookingDetailsDto> bookingDetailsDtos = new ArrayList<>();

            Map<String, List<BookingDetailsDto>> bookingMap = new HashMap<>();
            for (Bookings booking : bookings) {
                UserDto userDto = userInterface.getUserById(String.valueOf(booking.getUserId()));
                EventDto eventDto = eventInterface.getEventById((long) booking.getEventId());
                BookingDetailsDto bookingDetailsDto = bookingMapper.toDetailsDto(booking, userDto, eventDto);
                bookingDetailsDtos.add(bookingDetailsDto);
            }

            bookingMap.put("bookings", bookingDetailsDtos);

            return new ResponseEntity<>(bookingMap, HttpStatus.OK);
            
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public ResponseEntity<Map<String, List<BookingDto>>> getBookingsByUserId(Integer userId) {
        
        try {

            List<Bookings> bookings = bookingRepository.findByUserId(userId);

            if (bookings.isEmpty()) {
                throw new BookingNotFoundException("No bookings found for this user");
            }

            List<BookingDto> bookingDtos = new ArrayList<>();
            Map<String, List<BookingDto>> bookingMap = new HashMap<>();
            for (Bookings booking : bookings) {
                BookingDto bookingDto = bookingMapper.toDto(booking);
                bookingDtos.add(bookingDto);
            }

            bookingMap.put("bookings", bookingDtos);

            return new ResponseEntity<>(bookingMap, HttpStatus.OK);
            
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public ResponseEntity<Map<String, List<BookingDto>>> getBookingsByEventId(Integer eventId) {
        try {

            List<Bookings> bookings = bookingRepository.findByEventId(eventId);

            if (bookings.isEmpty()) {
                throw new BookingNotFoundException("No bookings found for this event");
            }

            List<BookingDto> bookingDtos = new ArrayList<>();
            Map<String, List<BookingDto>> bookingMap = new HashMap<>();
            for (Bookings booking : bookings) {
                BookingDto bookingDto = bookingMapper.toDto(booking);
                bookingDtos.add(bookingDto);
            }

            bookingMap.put("bookings", bookingDtos);

            return new ResponseEntity<>(bookingMap, HttpStatus.OK);
            
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public ResponseEntity<BookingDto> updateBooking(Long bookingId, int updatedSeats) throws Exception {
        try {
            if(updatedSeats <= 0) {
                throw new InvalidBookingDetailsException("Please enter seats greater than 0");
            }

            Bookings existingBooking = bookingRepository.findById(bookingId).orElseThrow(()-> new BookingNotFoundException("Booking not found"));

            if(!(existingBooking.getSeatsBooked() == updatedSeats)){
                EventDto eventDto = eventInterface.getEventById((long) existingBooking.getEventId());

                int seatDifference = updatedSeats - existingBooking.getSeatsBooked();

                if(seatDifference > 0 && eventDto.getAvailableSeats() < seatDifference) {
                    throw new InvalidBookingDetailsException("Not enough seats available");
                }

                eventDto.setAvailableSeats(eventDto.getAvailableSeats() - seatDifference);
                if (eventDto.getAvailableSeats() <= 0) {
                    eventDto.setStatus("UPCOMING");
                    eventDto.setAvailableSeats(0);
                } else {
                    eventDto.setStatus("ACTIVE");
                }

                eventInterface.updateEvent(eventDto.getId().longValue(), eventDto);

                existingBooking.setSeatsBooked(updatedSeats);
                bookingRepository.save(existingBooking);
                BookingDto updatedBookingDto = bookingMapper.toDto(existingBooking);
                return new ResponseEntity<>(updatedBookingDto, HttpStatus.OK);
            }else{
                throw new InvalidBookingDetailsException("No changes made to the booking");
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public ResponseEntity<BookingDto> confirmBooking(Long bookingId) throws Exception {
        try {
            Bookings booking = bookingRepository.findById(bookingId).orElseThrow(()-> new BookingNotFoundException("Booking not found"));
            if (Status.PENDING == booking.getStatus()) {
                booking.setStatus(Status.CONFIRMED);
                bookingRepository.save(booking);
            } else {
                throw new InvalidBookingDetailsException("Booking is already confirmed or cancelled");
            }

            //sending confirmation email
            UserDto userDto = userInterface.getUserById(String.valueOf(booking.getUserId()));
            EventDto eventDto = eventInterface.getEventById((long) booking.getEventId());

            EmailRequest emailRequest = new EmailRequest();
            emailRequest.setTo(userDto.getEmail());
            emailRequest.setSubject("Booking Confirmation");
            
            Map<String, Object> templateData = new HashMap<>();
            templateData.put("customerName", userDto.getFullName());
            templateData.put("bookingId", booking.getId());
            templateData.put("eventName", eventDto.getEventName());
            templateData.put("eventDate", eventDto.getDate());
            templateData.put("eventTime", eventDto.getTime());
            templateData.put("ticketCount", booking.getSeatsBooked());

            double totalPrice = eventDto.getPrice() * booking.getSeatsBooked();
            templateData.put("totalAmount", totalPrice);
            templateData.put("eventLocation", eventDto.getLocation());

            emailRequest.setTemplateData(templateData);
            notificationInterface.sendEmail(emailRequest);

            BookingDto bookingDto = bookingMapper.toDto(booking);
            return new ResponseEntity<>(bookingDto, HttpStatus.OK);
        } catch (Exception ex) {
           throw ex;
        }
    }

    @Override
    public ResponseEntity<?> deleteBooking(Long bookingId) throws Exception {
        try {
            Bookings bookings = bookingRepository.findById(bookingId).orElseThrow(()-> new BookingNotFoundException("Booking not found"));

            if(!(Status.CANCELLED == bookings.getStatus())) {
               EventDto eventDto = eventInterface.getEventById((long) bookings.getEventId());
               int updatedAvailableSeats = eventDto.getAvailableSeats() + bookings.getSeatsBooked();
               eventDto.setAvailableSeats(updatedAvailableSeats);
                if (updatedAvailableSeats > 0) {
                     eventDto.setStatus("ACTIVE");
                } else {
                     eventDto.setStatus("UPCOMING");
                }
                eventInterface.updateEvent(eventDto.getId().longValue(), eventDto);

                bookingRepository.deleteById(bookingId);
            }
            return new ResponseEntity<>("", HttpStatus.OK);
        }catch (Exception ex) {
            throw ex;
        }
    }
    
}
