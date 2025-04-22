package com.event.booking_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.event.booking_service.model.Bookings;
import java.util.List;


@Repository
public interface BookingRepository extends JpaRepository<Bookings,Long> {

    List<Bookings> findByUserId(int userId);
    List<Bookings> findByEventId(int eventId);
    List<Bookings> findByUserIdAndEventId(int userId, int eventId);
    List<Bookings> findByStatus(String status);
    
}
