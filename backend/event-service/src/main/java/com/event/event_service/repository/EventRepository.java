package com.event.event_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.event.event_service.model.Event;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event,Long>{

    //to search with particular keyword (case insensitive)
    List<Event> findByEventNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrLocationContainingIgnoreCase(String eventName,String description,String location);

    List<Event> findByOrganizername(String organizerName);
}
