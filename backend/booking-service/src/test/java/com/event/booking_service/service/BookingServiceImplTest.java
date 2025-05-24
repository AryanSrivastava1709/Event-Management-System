package com.event.booking_service.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import com.event.booking_service.dto.*;
import com.event.booking_service.exception.*;
import com.event.booking_service.feign.*;
import com.event.booking_service.mapper.BookingMapper;
import com.event.booking_service.model.Bookings;
import com.event.booking_service.model.Status;
import com.event.booking_service.repository.BookingRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class BookingServiceImplTest {

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private BookingMapper bookingMapper;
    @Mock
    private UserInterface userInterface;
    @Mock
    private EventInterface eventInterface;
    @Mock
    private NotificationInterface notificationInterface;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // @Test
    // void createBooking_validBooking_shouldReturnCreatedResponse() throws Exception {
    //     BookingDto bookingDto = new BookingDto(null, 1, 1, 2, null, null, null);
    //     UserDto userDto = new UserDto(1, "John", "john@example.com", "123", "USER");
    //     EventDto eventDto = new EventDto(1L, "Concert", "Desc", "Venue", LocalDate.now(), LocalTime.now(), 100.0, 10,
    //             "Org", "", "ACTIVE");

    //     Bookings savedBooking = new Bookings(); // Assume valid model
    //     BookingDto savedDto = new BookingDto(1L, 1, 1, 2, LocalDate.now(), LocalTime.now(), Status.PENDING);

    //     when(userInterface.getUserById("1")).thenReturn(userDto);
    //     when(eventInterface.getEventById(1L)).thenReturn(eventDto);
    //     when(bookingMapper.toEntity(any())).thenReturn(savedBooking);
    //     when(bookingRepository.save(savedBooking)).thenReturn(savedBooking);
    //     when(bookingMapper.toDto(savedBooking)).thenReturn(savedDto);

    //     ResponseEntity<BookingDto> response = bookingService.createBooking(bookingDto);

    //     assertEquals(HttpStatus.CREATED, response.getStatusCode());
    //     assertEquals(savedDto, response.getBody());
    //     verify(eventInterface).updateEvent(eq(1L), any(EventDto.class));
    // }

    @Test
void createBooking_validBooking_shouldReturnCreatedResponse() throws Exception {
    // Arrange
    BigDecimal totalAmount = new BigDecimal("200.00");
    BookingDto bookingDto = new BookingDto(null, 1, 1, 2, null, null, null, totalAmount);

    UserDto userDto = new UserDto(1, "John", "john@example.com", "123", "USER");

    EventDto eventDto = new EventDto(
        1L, "Concert", "Desc", "Venue",
        LocalDate.now(), LocalTime.now(),
        100.0, 10, "Org", "", "ACTIVE"
    );

    Bookings savedBooking = new Bookings(); // Mocked entity

    BookingDto savedDto = new BookingDto(
        1L, 1, 1, 2,
        LocalDate.now(), LocalTime.now(),
        Status.PENDING, totalAmount
    );

    // Mock dependencies
    when(userInterface.getUserById("1")).thenReturn(userDto);
    when(eventInterface.getEventById(1L)).thenReturn(eventDto);
    when(bookingMapper.toEntity(any())).thenReturn(savedBooking);
    when(bookingRepository.save(savedBooking)).thenReturn(savedBooking);
    when(bookingMapper.toDto(savedBooking)).thenReturn(savedDto);

    // Act
    ResponseEntity<BookingDto> response = bookingService.createBooking(bookingDto);

    // Assert
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(savedDto, response.getBody());
    verify(eventInterface).updateEvent(eq(1L), any(EventDto.class));
}


    @Test
    void getBookingById_validId_shouldReturnBooking() {
        Long bookingId = 1L;
        Bookings booking = new Bookings();
        BookingDto bookingDto = new BookingDto();
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingMapper.toDto(booking)).thenReturn(bookingDto);

        ResponseEntity<BookingDto> response = bookingService.getBookingById(bookingId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookingDto, response.getBody());
    }

    @Test
    void getBookingById_invalidId_shouldThrowException() {
        Long bookingId = 99L;
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class, () -> bookingService.getBookingById(bookingId));
    }

    @Test
    void updateBooking_sameSeats_shouldThrowInvalidBookingDetailsException() {
        Long bookingId = 1L;
        Bookings booking = new Bookings();
        booking.setSeatsBooked(3);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        InvalidBookingDetailsException ex = assertThrows(InvalidBookingDetailsException.class,
                () -> bookingService.updateBooking(bookingId, 3));
        assertEquals("No changes made to the booking", ex.getMessage());
    }

    @Test
    void deleteBooking_valid_shouldUpdateEventAndDeleteBooking() throws Exception {
        Long bookingId = 1L;
        Bookings booking = new Bookings();
        booking.setStatus(Status.PENDING);
        booking.setSeatsBooked(2);
        booking.setEventId(10);

        EventDto eventDto = new EventDto();
        eventDto.setId(10L); // ✅ Prevent NPE by setting the ID
        eventDto.setAvailableSeats(3);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(eventInterface.getEventById(10L)).thenReturn(eventDto);

        ResponseEntity<?> response = bookingService.deleteBooking(bookingId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(bookingRepository).deleteById(bookingId);
    }

    @Test
    void getBookingDetailsById_validId_shouldReturnBookingDetailsDto() throws Exception {
        Long bookingId = 1L;
        Bookings booking = new Bookings();
        booking.setId(bookingId);
        booking.setUserId(1);
        booking.setEventId(2);
        booking.setSeatsBooked(3);
        booking.setBookingDate(LocalDate.now());
        booking.setBookingTime(LocalTime.now());
        booking.setStatus(Status.PENDING);

        UserDto userDto = new UserDto(1, "Alice", "alice@example.com", "pass", "USER");
        EventDto eventDto = new EventDto();
        eventDto.setPrice(50.0);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(userInterface.getUserById("1")).thenReturn(userDto);
        when(eventInterface.getEventById(2L)).thenReturn(eventDto);

        BookingDetailsDto bookingDetailsDto = new BookingDetailsDto(
                booking.getId(),
                userDto,
                eventDto,
                booking.getSeatsBooked(),
                booking.getBookingDate(),
                booking.getBookingTime(),
                booking.getStatus(),
                BigDecimal.valueOf(eventDto.getPrice() * booking.getSeatsBooked()));

        when(bookingMapper.toDetailsDto(booking, userDto, eventDto)).thenReturn(bookingDetailsDto);

        ResponseEntity<BookingDetailsDto> response = bookingService.getBookingDetailsById(bookingId);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        BookingDetailsDto details = response.getBody();
        assertNotNull(details);
        assertEquals(bookingId, details.getId());
        assertEquals(userDto, details.getUser());
        assertEquals(eventDto, details.getEvent());
        assertEquals(3, details.getSeatsBooked());
        assertEquals(Status.PENDING, details.getStatus());
        assertEquals(BigDecimal.valueOf(150.0), details.getTotalAmount());
    }

    @Test
    void getAllBookings_shouldReturnListOfBookings() {
        Bookings booking = new Bookings();
        BookingDto bookingDto = new BookingDto();

        when(bookingRepository.findAll()).thenReturn(List.of(booking));
        when(bookingMapper.toDto(booking)).thenReturn(bookingDto);

        ResponseEntity<Map<String, List<BookingDto>>> response = bookingService.getAllBookings();

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Map<String, List<BookingDto>> bookingMap = response.getBody();
        assertNotNull(bookingMap);
        assertTrue(bookingMap.containsKey("bookings"));

        List<BookingDto> bookingsList = bookingMap.get("bookings");
        assertEquals(1, bookingsList.size());
        assertEquals(bookingDto, bookingsList.get(0));
    }

    @Test
    void getAllBookingDetails_shouldReturnListOfBookingDetails() throws Exception {
        Bookings booking = new Bookings();
        booking.setUserId(1);
        booking.setEventId(2);

        UserDto userDto = new UserDto(1, "User", "email", "pass", "USER");
        EventDto eventDto = new EventDto();

        BookingDetailsDto bookingDetailsDto = new BookingDetailsDto();
        bookingDetailsDto.setUser(userDto);
        bookingDetailsDto.setEvent(eventDto);
        bookingDetailsDto.setSeatsBooked(2);
        bookingDetailsDto.setId(1L);

        when(bookingRepository.findAll()).thenReturn(List.of(booking));
        when(userInterface.getUserById("1")).thenReturn(userDto);
        when(eventInterface.getEventById(2L)).thenReturn(eventDto);
        when(bookingMapper.toDetailsDto(booking, userDto, eventDto)).thenReturn(bookingDetailsDto);

        ResponseEntity<Map<String, List<BookingDetailsDto>>> response = bookingService.getAllBookingDetails();

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Map<String, List<BookingDetailsDto>> bookingMap = response.getBody();
        assertNotNull(bookingMap);
        assertTrue(bookingMap.containsKey("bookings"));

        List<BookingDetailsDto> bookingDetailsList = bookingMap.get("bookings");
        assertEquals(1, bookingDetailsList.size());

        BookingDetailsDto actualBookingDetails = bookingDetailsList.get(0);
        assertEquals(bookingDetailsDto.getId(), actualBookingDetails.getId());
        assertEquals(userDto, actualBookingDetails.getUser());
        assertEquals(eventDto, actualBookingDetails.getEvent());
    }

    @Test
    void getBookingsByUserId_validUser_shouldReturnBookings() {
        int userId = 1;
        Bookings booking = new Bookings();
        BookingDto bookingDto = new BookingDto();

        when(bookingRepository.findByUserId(userId)).thenReturn(List.of(booking));
        when(bookingMapper.toDto(booking)).thenReturn(bookingDto);

        ResponseEntity<Map<String, List<BookingDto>>> response = bookingService.getBookingsByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Map<String, List<BookingDto>> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.containsKey("bookings"));

        List<BookingDto> bookingList = responseBody.get("bookings");
        assertEquals(1, bookingList.size());
    }

    @Test
    void getBookingsByEventId_validEvent_shouldReturnBookings() {
        int eventId = 1;
        Bookings booking = new Bookings();
        BookingDto bookingDto = new BookingDto();

        when(bookingRepository.findByEventId(eventId)).thenReturn(List.of(booking));
        when(bookingMapper.toDto(booking)).thenReturn(bookingDto);

        ResponseEntity<Map<String, List<BookingDto>>> response = bookingService.getBookingsByEventId(eventId);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Map<String, List<BookingDto>> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.containsKey("bookings"));

        List<BookingDto> bookingList = responseBody.get("bookings");
        assertEquals(1, bookingList.size());
    }

    @Test
    void updateBooking_validSeats_shouldUpdateAndReturnBooking() throws Exception {
        Long bookingId = 1L;
        int newSeats = 5;
        Bookings booking = new Bookings();
        booking.setSeatsBooked(2);
        booking.setEventId(10);
        booking.setStatus(Status.PENDING);

        EventDto eventDto = new EventDto();
        eventDto.setId(10L); // Set this to prevent NPE
        eventDto.setAvailableSeats(10);

        BookingDto updatedDto = new BookingDto();

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(eventInterface.getEventById(10L)).thenReturn(eventDto);
        when(bookingRepository.save(any(Bookings.class))).thenReturn(booking);
        when(bookingMapper.toDto(booking)).thenReturn(updatedDto);

        ResponseEntity<BookingDto> response = bookingService.updateBooking(bookingId, newSeats);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedDto, response.getBody());
    }

    @Test
    void confirmBooking_validPending_shouldConfirmBookingAndSendNotification() throws Exception {
        Long bookingId = 1L;
        Bookings booking = new Bookings();
        booking.setStatus(Status.PENDING);
        booking.setUserId(1);
        booking.setEventId(2);

        UserDto userDto = new UserDto(1, "User", "user@mail.com", "pass", "USER");
        EventDto eventDto = new EventDto();
        eventDto.setPrice(100.0);  // <-- Set a valid price here
        BookingDto confirmedDto = new BookingDto();

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(userInterface.getUserById("1")).thenReturn(userDto);
        when(eventInterface.getEventById(2L)).thenReturn(eventDto);
        when(bookingRepository.save(any())).thenReturn(booking);
        when(bookingMapper.toDto(booking)).thenReturn(confirmedDto);

        ResponseEntity<BookingDto> response = bookingService.confirmBooking(bookingId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(notificationInterface).sendEmail(any(EmailRequest.class));
    }

}
